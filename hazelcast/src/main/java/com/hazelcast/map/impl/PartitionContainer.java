/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.map.impl;

import com.hazelcast.internal.locksupport.LockSupportService;
import com.hazelcast.config.MapConfig;
import com.hazelcast.internal.eviction.ExpirationManager;
import com.hazelcast.map.impl.operation.MapClearExpiredOperation;
import com.hazelcast.map.impl.recordstore.RecordStore;
import com.hazelcast.query.impl.Indexes;
import com.hazelcast.spi.impl.executionservice.ExecutionService;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.internal.services.ObjectNamespace;
import com.hazelcast.spi.impl.operationservice.OperationService;
import com.hazelcast.internal.services.ServiceNamespace;
import com.hazelcast.internal.partition.IPartitionService;
import com.hazelcast.spi.properties.GroupProperty;
import com.hazelcast.spi.properties.HazelcastProperties;
import com.hazelcast.internal.util.ConcurrencyUtil;
import com.hazelcast.internal.util.ConstructorFunction;
import com.hazelcast.internal.util.ContextMutexFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.hazelcast.map.impl.MapKeyLoaderUtil.getMaxSizePerNode;

public class PartitionContainer {

    private final int partitionId;
    private final MapService mapService;
    private final ContextMutexFactory contextMutexFactory = new ContextMutexFactory();
    private final ConcurrentMap<String, RecordStore> maps = new ConcurrentHashMap<>(1000);
    private final ConcurrentMap<String, Indexes> indexes = new ConcurrentHashMap<>(10);
    private final ConstructorFunction<String, RecordStore> recordStoreConstructor
            = name -> {
                RecordStore recordStore = createRecordStore(name);
                recordStore.startLoading();
                return recordStore;
            };
    private final ConstructorFunction<String, RecordStore> recordStoreConstructorSkipLoading
            = this::createRecordStore;

    private final ConstructorFunction<String, RecordStore> recordStoreConstructorForHotRestart
            = this::createRecordStore;
    /**
     * Flag to check if there is a {@link MapClearExpiredOperation}
     * running on this partition at this moment or not.
     */
    private volatile boolean hasRunningCleanup;
    private volatile long lastCleanupTime;

    /**
     * Used when sorting partition containers in {@link ExpirationManager}
     * A non-volatile copy of lastCleanupTime is used with two reasons.
     * <p>
     * 1. We need an un-modified field during sorting.
     * 2. Decrease number of volatile reads.
     */
    private long lastCleanupTimeCopy;

    public PartitionContainer(final MapService mapService, final int partitionId) {
        this.mapService = mapService;
        this.partitionId = partitionId;
    }

    private RecordStore createRecordStore(String name) {
        MapServiceContext serviceContext = mapService.getMapServiceContext();
        MapContainer mapContainer = serviceContext.getMapContainer(name);
        MapConfig mapConfig = mapContainer.getMapConfig();
        NodeEngine nodeEngine = serviceContext.getNodeEngine();
        IPartitionService ps = nodeEngine.getPartitionService();
        OperationService opService = nodeEngine.getOperationService();
        ExecutionService execService = nodeEngine.getExecutionService();
        HazelcastProperties hazelcastProperties = nodeEngine.getProperties();

        MapKeyLoader keyLoader = new MapKeyLoader(name, opService, ps, nodeEngine.getClusterService(),
                execService, mapContainer.toData());
        keyLoader.setMaxBatch(hazelcastProperties.getInteger(GroupProperty.MAP_LOAD_CHUNK_SIZE));
        keyLoader.setMaxSize(getMaxSizePerNode(mapConfig.getMaxSizeConfig()));
        keyLoader.setHasBackup(mapConfig.getTotalBackupCount() > 0);
        keyLoader.setMapOperationProvider(serviceContext.getMapOperationProvider(name));

        if (!mapContainer.isGlobalIndexEnabled()) {
            Indexes indexesForMap = mapContainer.createIndexes(false);
            indexes.putIfAbsent(name, indexesForMap);
        }
        RecordStore recordStore = serviceContext.createRecordStore(mapContainer, partitionId, keyLoader);
        recordStore.init();
        return recordStore;
    }

    public ConcurrentMap<String, RecordStore> getMaps() {
        return maps;
    }

    public ConcurrentMap<String, Indexes> getIndexes() {
        return indexes;
    }

    public Collection<RecordStore> getAllRecordStores() {
        return maps.values();
    }

    public Collection<ServiceNamespace> getAllNamespaces(int replicaIndex) {
        Collection<ServiceNamespace> namespaces = new HashSet<>();

        for (RecordStore recordStore : maps.values()) {
            MapContainer mapContainer = recordStore.getMapContainer();
            MapConfig mapConfig = mapContainer.getMapConfig();
            if (mapConfig.getTotalBackupCount() < replicaIndex) {
                continue;
            }

            namespaces.add(mapContainer.getObjectNamespace());
        }

        return namespaces;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public MapService getMapService() {
        return mapService;
    }

    public RecordStore getRecordStore(String name) {
        return ConcurrencyUtil.getOrPutSynchronized(maps, name, contextMutexFactory, recordStoreConstructor);
    }

    public RecordStore getRecordStore(String name, boolean skipLoadingOnCreate) {
        return ConcurrencyUtil.getOrPutSynchronized(maps, name, this, skipLoadingOnCreate
                ? recordStoreConstructorSkipLoading : recordStoreConstructor);
    }

    public RecordStore getRecordStoreForHotRestart(String name) {
        return ConcurrencyUtil.getOrPutSynchronized(maps, name, contextMutexFactory, recordStoreConstructorForHotRestart);
    }

    @Nullable
    public RecordStore getExistingRecordStore(String mapName) {
        return maps.get(mapName);
    }

    public void destroyMap(MapContainer mapContainer) {
        String name = mapContainer.getName();
        RecordStore recordStore = maps.remove(name);
        if (recordStore != null) {
            // this call also clears and disposes Indexes for that partition
            recordStore.destroy();
        } else {
            // It can be that, map is used only for locking,
            // because of that RecordStore is not created.
            // We will try to remove/clear LockStore belonging to
            // this IMap partition.
            clearLockStore(name);
        }
        // getting rid of Indexes object in case it has been initialized
        indexes.remove(name);

        MapServiceContext mapServiceContext = mapService.getMapServiceContext();
        if (mapServiceContext.removeMapContainer(mapContainer)) {
            mapContainer.onDestroy();
        }
        mapServiceContext.removePartitioningStrategyFromCache(mapContainer.getName());
    }

    private void clearLockStore(String name) {
        final NodeEngine nodeEngine = mapService.getMapServiceContext().getNodeEngine();
        final LockSupportService lockService = nodeEngine.getServiceOrNull(LockSupportService.SERVICE_NAME);
        if (lockService != null) {
            final ObjectNamespace namespace = MapService.getObjectNamespace(name);
            lockService.clearLockStore(partitionId, namespace);
        }
    }

    public boolean hasRunningCleanup() {
        return hasRunningCleanup;
    }

    public void setHasRunningCleanup(boolean hasRunningCleanup) {
        this.hasRunningCleanup = hasRunningCleanup;
    }

    public long getLastCleanupTime() {
        return lastCleanupTime;
    }

    public void setLastCleanupTime(long lastCleanupTime) {
        this.lastCleanupTime = lastCleanupTime;
    }

    public long getLastCleanupTimeCopy() {
        return lastCleanupTimeCopy;
    }

    public void setLastCleanupTimeCopy(long lastCleanupTimeCopy) {
        this.lastCleanupTimeCopy = lastCleanupTimeCopy;
    }

    // -------------------------------------------------------------------------------------------------------------
    // IMPORTANT: never use directly! use MapContainer.getIndex() instead.
    // There are cases where a global index is used. In this case, the global-index is stored in the MapContainer.
    // By using this method in the context of global index an exception will be thrown.
    // -------------------------------------------------------------------------------------------------------------
    Indexes getIndexes(String name) {
        Indexes ixs = indexes.get(name);
        if (ixs == null) {
            MapServiceContext mapServiceContext = mapService.getMapServiceContext();
            MapContainer mapContainer = mapServiceContext.getMapContainer(name);
            if (mapContainer.isGlobalIndexEnabled()) {
                throw new IllegalStateException("Can't use a partitioned-index in the context of a global-index.");
            }

            Indexes indexesForMap = mapContainer.createIndexes(false);
            ixs = indexes.putIfAbsent(name, indexesForMap);
            if (ixs == null) {
                ixs = indexesForMap;
            }
        }
        return ixs;
    }
}
