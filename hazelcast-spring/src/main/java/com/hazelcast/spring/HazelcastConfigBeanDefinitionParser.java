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

package com.hazelcast.spring;

import com.hazelcast.config.AdvancedNetworkConfig;
import com.hazelcast.config.AliasedDiscoveryConfig;
import com.hazelcast.internal.config.AliasedDiscoveryConfigUtils;
import com.hazelcast.config.CRDTReplicationConfig;
import com.hazelcast.config.CachePartitionLostListenerConfig;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.CacheSimpleConfig.ExpiryPolicyFactoryConfig;
import com.hazelcast.config.CacheSimpleConfig.ExpiryPolicyFactoryConfig.DurationConfig;
import com.hazelcast.config.CacheSimpleConfig.ExpiryPolicyFactoryConfig.TimedExpiryPolicyFactoryConfig;
import com.hazelcast.config.CacheSimpleConfig.ExpiryPolicyFactoryConfig.TimedExpiryPolicyFactoryConfig.ExpiryPolicyType;
import com.hazelcast.config.CacheSimpleEntryListenerConfig;
import com.hazelcast.config.CardinalityEstimatorConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.CredentialsFactoryConfig;
import com.hazelcast.config.CustomWanPublisherConfig;
import com.hazelcast.config.DurableExecutorConfig;
import com.hazelcast.config.EncryptionAtRestConfig;
import com.hazelcast.config.EndpointConfig;
import com.hazelcast.config.EntryListenerConfig;
import com.hazelcast.config.EventJournalConfig;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.config.FlakeIdGeneratorConfig;
import com.hazelcast.config.HotRestartConfig;
import com.hazelcast.config.HotRestartPersistenceConfig;
import com.hazelcast.config.IcmpFailureDetectorConfig;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.InvalidConfigurationException;
import com.hazelcast.config.ItemListenerConfig;
import com.hazelcast.config.JavaKeyStoreSecureStoreConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.LoginModuleConfig;
import com.hazelcast.config.MCMutualAuthConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.AttributeConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapPartitionLostListenerConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.config.MemberAddressProviderConfig;
import com.hazelcast.config.MemberAttributeConfig;
import com.hazelcast.config.MemberGroupConfig;
import com.hazelcast.config.MemcacheProtocolConfig;
import com.hazelcast.config.MergePolicyConfig;
import com.hazelcast.config.MerkleTreeConfig;
import com.hazelcast.config.MultiMapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.OnJoinPermissionOperationName;
import com.hazelcast.config.PNCounterConfig;
import com.hazelcast.config.PartitionGroupConfig;
import com.hazelcast.config.PartitioningStrategyConfig;
import com.hazelcast.config.PermissionConfig;
import com.hazelcast.config.PermissionConfig.PermissionType;
import com.hazelcast.config.PermissionPolicyConfig;
import com.hazelcast.config.PredicateConfig;
import com.hazelcast.config.ProbabilisticSplitBrainProtectionConfigBuilder;
import com.hazelcast.config.QueryCacheConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.config.RecentlyActiveSplitBrainProtectionConfigBuilder;
import com.hazelcast.config.ReliableTopicConfig;
import com.hazelcast.config.ReplicatedMapConfig;
import com.hazelcast.config.RestApiConfig;
import com.hazelcast.config.RestEndpointGroup;
import com.hazelcast.config.RestServerEndpointConfig;
import com.hazelcast.config.RingbufferConfig;
import com.hazelcast.config.RingbufferStoreConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.config.ScheduledExecutorConfig;
import com.hazelcast.config.SecurityConfig;
import com.hazelcast.config.SecurityInterceptorConfig;
import com.hazelcast.config.ServerSocketEndpointConfig;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.config.ServicesConfig;
import com.hazelcast.config.SetConfig;
import com.hazelcast.config.SplitBrainProtectionConfig;
import com.hazelcast.config.SplitBrainProtectionConfigBuilder;
import com.hazelcast.config.SplitBrainProtectionListenerConfig;
import com.hazelcast.config.SymmetricEncryptionConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.config.VaultSecureStoreConfig;
import com.hazelcast.config.WanBatchReplicationPublisherConfig;
import com.hazelcast.config.WanConsumerConfig;
import com.hazelcast.config.WanReplicationConfig;
import com.hazelcast.config.WanReplicationRef;
import com.hazelcast.config.WanSyncConfig;
import com.hazelcast.config.cp.SemaphoreConfig;
import com.hazelcast.config.security.JaasAuthenticationConfig;
import com.hazelcast.config.security.LdapAuthenticationConfig;
import com.hazelcast.config.security.LdapRoleMappingMode;
import com.hazelcast.config.security.LdapSearchScope;
import com.hazelcast.config.security.RealmConfig;
import com.hazelcast.config.security.TlsAuthenticationConfig;
import com.hazelcast.config.security.TokenEncoding;
import com.hazelcast.config.security.TokenIdentityConfig;
import com.hazelcast.config.security.UsernamePasswordIdentityConfig;
import com.hazelcast.config.cp.CPSubsystemConfig;
import com.hazelcast.config.cp.FencedLockConfig;
import com.hazelcast.config.cp.RaftAlgorithmConfig;
import com.hazelcast.instance.EndpointQualifier;
import com.hazelcast.instance.ProtocolType;
import com.hazelcast.internal.services.ServiceConfigurationParser;
import com.hazelcast.map.eviction.MapEvictionPolicy;
import com.hazelcast.memory.MemorySize;
import com.hazelcast.memory.MemoryUnit;
import com.hazelcast.internal.nio.ClassLoaderUtil;
import com.hazelcast.splitbrainprotection.SplitBrainProtectionOn;
import com.hazelcast.internal.util.ExceptionUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.hazelcast.internal.config.DomConfigHelper.childElementWithName;
import static com.hazelcast.internal.config.DomConfigHelper.childElements;
import static com.hazelcast.internal.config.DomConfigHelper.cleanNodeName;
import static com.hazelcast.internal.config.DomConfigHelper.getBooleanValue;
import static com.hazelcast.internal.config.DomConfigHelper.getDoubleValue;
import static com.hazelcast.internal.config.DomConfigHelper.getIntegerValue;
import static com.hazelcast.internal.config.DomConfigHelper.getLongValue;
import static com.hazelcast.internal.util.ExceptionUtil.rethrow;
import static com.hazelcast.internal.util.Preconditions.checkHasText;
import static com.hazelcast.internal.util.StringUtil.isNullOrEmpty;
import static com.hazelcast.internal.util.StringUtil.upperCaseInternal;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static org.springframework.util.Assert.isTrue;

/**
 * BeanDefinitionParser for Hazelcast Config Configuration.
 * <p>
 * <b>Sample Spring XML for Hazelcast Config:</b>
 * <p>
 * <pre>{@code
 *   <hz:config>
 *     <hz:map name="map1">
 *       <hz:near-cache time-to-live-seconds="0" max-idle-seconds="60"
 *            eviction-policy="LRU" max-size="5000"  invalidate-on-change="true"/>
 *
 *     <hz:map-store enabled="true" class-name="com.foo.DummyStore"
 *            write-delay-seconds="0"/>
 *     </hz:map>
 *     <hz:map name="map2">
 *       <hz:map-store enabled="true" implementation="dummyMapStore"
 *          write-delay-seconds="0"/>
 *       </hz:map>
 *
 *     <bean id="dummyMapStore" class="com.foo.DummyStore" />
 *  </hz:config>
 * }</pre>
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:executablestatementcount", "checkstyle:cyclomaticcomplexity",
        "WeakerAccess"})
public class HazelcastConfigBeanDefinitionParser extends AbstractHazelcastBeanDefinitionParser {

    public AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        SpringXmlConfigBuilder springXmlConfigBuilder = new SpringXmlConfigBuilder(parserContext);
        springXmlConfigBuilder.handleConfig(element);
        return springXmlConfigBuilder.getBeanDefinition();
    }

    private class SpringXmlConfigBuilder extends SpringXmlBuilderHelper {

        private final ParserContext parserContext;

        private ManagedMap<String, AbstractBeanDefinition> mapConfigManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> cacheConfigManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> queueManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> ringbufferManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> reliableTopicManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> listManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> setManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> topicManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> multiMapManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> executorManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> durableExecutorManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> scheduledExecutorManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> cardinalityEstimatorManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> wanReplicationManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> replicatedMapManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> splitBrainProtectionManagedMap;
        private ManagedMap<String, AbstractBeanDefinition> flakeIdGeneratorConfigMap;
        private ManagedMap<String, AbstractBeanDefinition> pnCounterManagedMap;
        private ManagedMap<EndpointQualifier, AbstractBeanDefinition> endpointConfigsMap;

        private boolean hasNetwork;
        private boolean hasAdvancedNetworkEnabled;

        SpringXmlConfigBuilder(ParserContext parserContext) {
            this.parserContext = parserContext;
            this.configBuilder = BeanDefinitionBuilder.rootBeanDefinition(Config.class);
            this.mapConfigManagedMap = createManagedMap("mapConfigs");
            this.cacheConfigManagedMap = createManagedMap("cacheConfigs");
            this.queueManagedMap = createManagedMap("queueConfigs");
            this.ringbufferManagedMap = createManagedMap("ringbufferConfigs");
            this.reliableTopicManagedMap = createManagedMap("reliableTopicConfigs");
            this.listManagedMap = createManagedMap("listConfigs");
            this.setManagedMap = createManagedMap("setConfigs");
            this.topicManagedMap = createManagedMap("topicConfigs");
            this.multiMapManagedMap = createManagedMap("multiMapConfigs");
            this.executorManagedMap = createManagedMap("executorConfigs");
            this.durableExecutorManagedMap = createManagedMap("durableExecutorConfigs");
            this.scheduledExecutorManagedMap = createManagedMap("scheduledExecutorConfigs");
            this.cardinalityEstimatorManagedMap = createManagedMap("cardinalityEstimatorConfigs");
            this.wanReplicationManagedMap = createManagedMap("wanReplicationConfigs");
            this.replicatedMapManagedMap = createManagedMap("replicatedMapConfigs");
            this.splitBrainProtectionManagedMap = createManagedMap("splitBrainProtectionConfigs");
            this.flakeIdGeneratorConfigMap = createManagedMap("flakeIdGeneratorConfigs");
            this.pnCounterManagedMap = createManagedMap("PNCounterConfigs");
            this.endpointConfigsMap = new ManagedMap<>();
        }

        private ManagedMap<String, AbstractBeanDefinition> createManagedMap(String configName) {
            ManagedMap<String, AbstractBeanDefinition> managedMap = new ManagedMap<>();
            this.configBuilder.addPropertyValue(configName, managedMap);
            return managedMap;
        }

        public AbstractBeanDefinition getBeanDefinition() {
            return configBuilder.getBeanDefinition();
        }

        @SuppressWarnings({"checkstyle:methodlength", "checkstyle:npathcomplexity"})
        public void handleConfig(Element element) {
            if (element != null) {
                handleCommonBeanAttributes(element, configBuilder, parserContext);
                for (Node node : childElements(element)) {
                    String nodeName = cleanNodeName(node);
                    if ("network".equals(nodeName)) {
                        handleNetwork(node);
                    } else if ("advanced-network".equals(nodeName)) {
                        handleAdvancedNetwork(node);
                    } else if ("properties".equals(nodeName)) {
                        handleProperties(node);
                    } else if ("executor-service".equals(nodeName)) {
                        handleExecutor(node);
                    } else if ("durable-executor-service".equals(nodeName)) {
                        handleDurableExecutor(node);
                    } else if ("scheduled-executor-service".equals(nodeName)) {
                        handleScheduledExecutor(node);
                    } else if ("cardinality-estimator".equals(nodeName)) {
                        handleCardinalityEstimator(node);
                    } else if ("queue".equals(nodeName)) {
                        handleQueue(node);
                    } else if ("ringbuffer".equals(nodeName)) {
                        handleRingbuffer(node);
                    } else if ("reliable-topic".equals(nodeName)) {
                        handleReliableTopic(node);
                    } else if ("map".equals(nodeName)) {
                        handleMap(node);
                    } else if ("cache".equals(nodeName)) {
                        handleCache(node);
                    } else if ("multimap".equals(nodeName)) {
                        handleMultiMap(node);
                    } else if ("list".equals(nodeName)) {
                        handleList(node);
                    } else if ("set".equals(nodeName)) {
                        handleSet(node);
                    } else if ("topic".equals(nodeName)) {
                        handleTopic(node);
                    } else if ("replicatedmap".equals(nodeName)) {
                        handleReplicatedMap(node);
                    } else if ("wan-replication".equals(nodeName)) {
                        handleWanReplication(node);
                    } else if ("partition-group".equals(nodeName)) {
                        handlePartitionGroup(node);
                    } else if ("serialization".equals(nodeName)) {
                        handleSerialization(node);
                    } else if ("native-memory".equals(nodeName)) {
                        handleNativeMemory(node);
                    } else if ("security".equals(nodeName)) {
                        handleSecurity(node);
                    } else if ("member-attributes".equals(nodeName)) {
                        handleMemberAttributes(node);
                    } else if ("instance-name".equals(nodeName)
                            || "cluster-name".equals(nodeName)
                            || "license-key".equals(nodeName)) {
                        configBuilder.addPropertyValue(xmlToJavaName(nodeName), getTextContent(node));
                    } else if ("listeners".equals(nodeName)) {
                        List listeners = parseListeners(node, ListenerConfig.class);
                        configBuilder.addPropertyValue("listenerConfigs", listeners);
                    } else if ("lite-member".equals(nodeName)) {
                        handleLiteMember(node);
                    } else if ("management-center".equals(nodeName)) {
                        handleManagementCenter(node);
                    } else if ("services".equals(nodeName)) {
                        handleServices(node);
                    } else if ("spring-aware".equals(nodeName)) {
                        handleSpringAware();
                    } else if ("split-brain-protection".equals(nodeName)) {
                        handleSplitBrainProtection(node);
                    } else if ("hot-restart-persistence".equals(nodeName)) {
                        handleHotRestartPersistence(node);
                    } else if ("flake-id-generator".equals(nodeName)) {
                        handleFlakeIdGenerator(node);
                    } else if ("crdt-replication".equals(nodeName)) {
                        handleCRDTReplication(node);
                    } else if ("pn-counter".equals(nodeName)) {
                        handlePNCounter(node);
                    } else if ("cp-subsystem".equals(nodeName)) {
                        handleCPSubSystem(node);
                    }
                }
            }

            if (hasNetwork && hasAdvancedNetworkEnabled) {
                throw new InvalidConfigurationException("Ambiguous configuration: cannot include both <network> and "
                        + "an enabled <advanced-network> element. Configure network using one of <network> or "
                        + "<advanced-network enabled=\"true\">.");
            }
        }

        private void handleHotRestartPersistence(Node node) {
            BeanDefinitionBuilder hotRestartConfigBuilder = createBeanBuilder(HotRestartPersistenceConfig.class);
            fillAttributeValues(node, hotRestartConfigBuilder);

            for (Node child : childElements(node)) {
                String name = cleanNodeName(child);
                if ("base-dir".equals(name)) {
                    String value = getTextContent(child);
                    hotRestartConfigBuilder.addPropertyValue("baseDir", value);
                } else if ("backup-dir".equals(name)) {
                    hotRestartConfigBuilder.addPropertyValue("backupDir", getTextContent(child));
                } else if ("encryption-at-rest".equals(name)) {
                    handleEncryptionAtRest(hotRestartConfigBuilder, child);
                }
            }
            configBuilder.addPropertyValue("hotRestartPersistenceConfig", hotRestartConfigBuilder.getBeanDefinition());
        }

        private void handleEncryptionAtRest(BeanDefinitionBuilder hotRestartConfigBuilder, Node node) {
            BeanDefinitionBuilder encryptionAtRestConfigBuilder = createBeanBuilder(EncryptionAtRestConfig.class);
            fillAttributeValues(node, encryptionAtRestConfigBuilder);
            fillValues(node, encryptionAtRestConfigBuilder, "secureStore");
            Node secureStoreNode = childElementWithName(node, "secure-store");
            for (Node child : childElements(secureStoreNode)) {
                String nodeName = cleanNodeName(child);
                AbstractBeanDefinition secureStoreBeanDefinition = null;
                if ("keystore".equals(nodeName)) {
                    secureStoreBeanDefinition = handleKeyStoreSecureStoreConfig(child);
                } else if ("vault".equals(nodeName)) {
                    secureStoreBeanDefinition = handleVaultSecureStoreConfig(child);
                }
                if (secureStoreBeanDefinition != null) {
                    encryptionAtRestConfigBuilder.addPropertyValue("secureStoreConfig", secureStoreBeanDefinition);
                }
            }
            hotRestartConfigBuilder.addPropertyValue("encryptionAtRestConfig", encryptionAtRestConfigBuilder.getBeanDefinition());
        }

        private AbstractBeanDefinition handleKeyStoreSecureStoreConfig(Node node) {
            BeanDefinitionBuilder keyStoreConfigBuilder = createBeanBuilder(JavaKeyStoreSecureStoreConfig.class);
            Node pathNode = childElementWithName(node, "path");
            keyStoreConfigBuilder.addConstructorArgValue(new File(getTextContent(pathNode).trim()).getAbsoluteFile());
            fillValues(node, keyStoreConfigBuilder);
            return keyStoreConfigBuilder.getBeanDefinition();
        }

        private AbstractBeanDefinition handleVaultSecureStoreConfig(Node node) {
            BeanDefinitionBuilder vaultConfigBuilder = createBeanBuilder(VaultSecureStoreConfig.class);
            Node addressNode = childElementWithName(node, "address");
            Node secretPathNode = childElementWithName(node, "secrets-path");
            Node tokenNode = childElementWithName(node, "token");
            vaultConfigBuilder.addConstructorArgValue(getTextContent(addressNode).trim());
            vaultConfigBuilder.addConstructorArgValue(getTextContent(secretPathNode).trim());
            vaultConfigBuilder.addConstructorArgValue(getTextContent(tokenNode).trim());
            fillValues(node, vaultConfigBuilder, "ssl");
            Node ssl = childElementWithName(node, "ssl");
            if (ssl != null) {
                handleSSLConfig(ssl, vaultConfigBuilder);
            }
            return vaultConfigBuilder.getBeanDefinition();
        }

        private void handleFlakeIdGenerator(Node node) {
            BeanDefinitionBuilder configBuilder = createBeanBuilder(FlakeIdGeneratorConfig.class);
            fillAttributeValues(node, configBuilder);
            String name = getAttribute(node, "name");
            flakeIdGeneratorConfigMap.put(name, configBuilder.getBeanDefinition());
        }

        private void handleCRDTReplication(Node node) {
            final BeanDefinitionBuilder crdtReplicationConfigBuilder = createBeanBuilder(CRDTReplicationConfig.class);
            fillAttributeValues(node, crdtReplicationConfigBuilder);
            configBuilder.addPropertyValue("CRDTReplicationConfig", crdtReplicationConfigBuilder.getBeanDefinition());
        }

        private void handleCPSubSystem(Node node) {
            BeanDefinitionBuilder cpSubsystemConfigBuilder = createBeanBuilder(CPSubsystemConfig.class);

            fillValues(node, cpSubsystemConfigBuilder, "raftAlgorithm", "semaphores", "locks", "cpMemberCount",
                    "missingCpMemberAutoRemovalSeconds");

            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("raft-algorithm".equals(nodeName)) {
                    BeanDefinitionBuilder raftAlgorithmConfigBuilder = createBeanBuilder(RaftAlgorithmConfig.class);
                    fillValues(child, raftAlgorithmConfigBuilder);
                    cpSubsystemConfigBuilder.addPropertyValue("raftAlgorithmConfig",
                            raftAlgorithmConfigBuilder.getBeanDefinition());
                } else if ("semaphores".equals(nodeName)) {
                    ManagedMap<String, AbstractBeanDefinition> semaphores = new ManagedMap<String, AbstractBeanDefinition>();
                    handleSemaphores(semaphores, child);
                    cpSubsystemConfigBuilder.addPropertyValue("SemaphoreConfigs", semaphores);
                } else if ("locks".equals(nodeName)) {
                    ManagedMap<String, AbstractBeanDefinition> locks = new ManagedMap<>();
                    handleFencedLocks(locks, child);
                    cpSubsystemConfigBuilder.addPropertyValue("LockConfigs", locks);
                } else {
                    String value = getTextContent(child).trim();
                    if ("cp-member-count".equals(nodeName)) {
                        cpSubsystemConfigBuilder.addPropertyValue("CPMemberCount",
                                getIntegerValue("cp-member-count", value));
                    } else if ("missing-cp-member-auto-removal-seconds".equals(nodeName)) {
                        cpSubsystemConfigBuilder.addPropertyValue("missingCPMemberAutoRemovalSeconds",
                                getIntegerValue("missing-cp-member-auto-removal-seconds", value));
                    }
                }
            }

            configBuilder.addPropertyValue("CPSubsystemConfig", cpSubsystemConfigBuilder.getBeanDefinition());
        }

        private void handleSemaphores(ManagedMap<String, AbstractBeanDefinition> semaphores, Node node) {
            for (Node child : childElements(node)) {
                BeanDefinitionBuilder semaphoreConfigBuilder = createBeanBuilder(SemaphoreConfig.class);
                for (Node subChild : childElements(child)) {
                    String nodeName = cleanNodeName(subChild);
                    String value = getTextContent(subChild).trim();
                    if ("name".equals(nodeName)) {
                        semaphoreConfigBuilder.addPropertyValue("name", value);
                    } else if ("jdk-compatible".equals(nodeName)) {
                        semaphoreConfigBuilder.addPropertyValue("JDKCompatible", getBooleanValue(value));
                    } else if ("initial-permits".equals(nodeName)) {
                        semaphoreConfigBuilder.addPropertyValue("initialPermits", getIntegerValue("initial-permits", value));
                    }
                }
                AbstractBeanDefinition beanDefinition = semaphoreConfigBuilder.getBeanDefinition();
                String name = (String) beanDefinition.getPropertyValues().get("name");
                semaphores.put(name, beanDefinition);
            }
        }

        private void handleFencedLocks(ManagedMap<String, AbstractBeanDefinition> locks, Node node) {
            for (Node child : childElements(node)) {
                BeanDefinitionBuilder lockConfigBuilder = createBeanBuilder(FencedLockConfig.class);
                fillValues(child, lockConfigBuilder);
                AbstractBeanDefinition beanDefinition = lockConfigBuilder.getBeanDefinition();
                String name = (String) beanDefinition.getPropertyValues().get("name");
                locks.put(name, beanDefinition);
            }
        }

        private void handleSplitBrainProtection(Node node) {
            BeanDefinitionBuilder splitBrainProtectionBuilder = createBeanBuilder(SplitBrainProtectionConfig.class);
            AbstractBeanDefinition beanDefinition = splitBrainProtectionBuilder.getBeanDefinition();
            String name = getAttribute(node, "name");
            splitBrainProtectionBuilder.addPropertyValue("name", name);
            Node attrEnabled = node.getAttributes().getNamedItem("enabled");
            boolean enabled = attrEnabled != null && getBooleanValue(getTextContent(attrEnabled));
            splitBrainProtectionBuilder.addPropertyValue("enabled", enabled);
            // probabilistic-split-brain-protection and recently-active-split-brain-protection split brain protection
            // configs are constructed via SplitBrainProtectionConfigBuilder
            SplitBrainProtectionConfigBuilder configBuilder = null;
            // initialized to a placeholder value; we may need to use this value before actually parsing the minimum-cluster-size
            // node; it will anyway have the proper value in the final split brain protection config.
            int splitBrainProtectionSize = 3;
            String splitBrainProtectionClassName = null;

            for (Node n : childElements(node)) {
                String value = getTextContent(n).trim();
                String nodeName = cleanNodeName(n);
                if ("minimum-cluster-size".equals(nodeName)) {
                    splitBrainProtectionBuilder.addPropertyValue("minimumClusterSize",
                            getIntegerValue("minimum-cluster-size", value));
                } else if ("listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(n, SplitBrainProtectionListenerConfig.class);
                    splitBrainProtectionBuilder.addPropertyValue("listenerConfigs", listeners);
                } else if ("protect-on".equals(nodeName)) {
                    splitBrainProtectionBuilder.addPropertyValue("protectOn", SplitBrainProtectionOn.valueOf(value));
                } else if ("function-class-name".equals(nodeName)) {
                    splitBrainProtectionClassName = value;
                    splitBrainProtectionBuilder.addPropertyValue(xmlToJavaName(nodeName), value);
                } else if ("recently-active-split-brain-protection".equals(nodeName)) {
                    configBuilder = handleRecentlyActiveSplitBrainProtection(name, n, splitBrainProtectionSize);
                } else if ("probabilistic-split-brain-protection".equals(nodeName)) {
                    configBuilder = handleProbabilisticSplitBrainProtection(name, n, splitBrainProtectionSize);
                }
            }
            if (configBuilder != null) {
                boolean splitBrainProtectionFunctionDefinedByClassName = !isNullOrEmpty(splitBrainProtectionClassName);
                if (splitBrainProtectionFunctionDefinedByClassName) {
                    throw new InvalidConfigurationException("A split brain protection"
                            + " cannot simultaneously define probabilistic-split-brain-protection or "
                            + "recently-active-split-brain-protection and a split brain protection function class name.");
                }
                SplitBrainProtectionConfig constructedConfig = configBuilder.build();
                // set the constructed split brain protection function implementation in the bean definition
                splitBrainProtectionBuilder.addPropertyValue("functionImplementation",
                        constructedConfig.getFunctionImplementation());
            }
            splitBrainProtectionManagedMap.put(name, beanDefinition);
        }

        private SplitBrainProtectionConfigBuilder handleRecentlyActiveSplitBrainProtection(String name, Node node,
                                                                                           int splitBrainProtectionSize) {
            SplitBrainProtectionConfigBuilder splitBrainProtectionConfigBuilder;
            int heartbeatToleranceMillis = getIntegerValue("heartbeat-tolerance-millis",
                    getAttribute(node, "heartbeat-tolerance-millis"),
                    RecentlyActiveSplitBrainProtectionConfigBuilder.DEFAULT_HEARTBEAT_TOLERANCE_MILLIS);
            splitBrainProtectionConfigBuilder = SplitBrainProtectionConfig.newRecentlyActiveSplitBrainProtectionConfigBuilder(
                    name,
                    splitBrainProtectionSize,
                    heartbeatToleranceMillis);
            return splitBrainProtectionConfigBuilder;
        }

        private SplitBrainProtectionConfigBuilder handleProbabilisticSplitBrainProtection(String name, Node node,
                                                                                          int splitBrainProtectionSize) {
            SplitBrainProtectionConfigBuilder splitBrainProtectionConfigBuilder;
            long acceptableHeartPause = getLongValue("acceptable-heartbeat-pause-millis",
                    getAttribute(node, "acceptable-heartbeat-pause-millis"),
                    ProbabilisticSplitBrainProtectionConfigBuilder.DEFAULT_HEARTBEAT_PAUSE_MILLIS);
            double threshold = getDoubleValue("suspicion-threshold",
                    getAttribute(node, "suspicion-threshold"),
                    ProbabilisticSplitBrainProtectionConfigBuilder.DEFAULT_PHI_THRESHOLD);
            int maxSampleSize = getIntegerValue("max-sample-size",
                    getAttribute(node, "max-sample-size"),
                    ProbabilisticSplitBrainProtectionConfigBuilder.DEFAULT_SAMPLE_SIZE);
            long minStdDeviation = getLongValue("min-std-deviation-millis",
                    getAttribute(node, "min-std-deviation-millis"),
                    ProbabilisticSplitBrainProtectionConfigBuilder.DEFAULT_MIN_STD_DEVIATION);
            long heartbeatIntervalMillis = getLongValue("heartbeat-interval-millis",
                    getAttribute(node, "heartbeat-interval-millis"),
                    ProbabilisticSplitBrainProtectionConfigBuilder.DEFAULT_HEARTBEAT_INTERVAL_MILLIS);
            splitBrainProtectionConfigBuilder =
                    SplitBrainProtectionConfig.newProbabilisticSplitBrainProtectionConfigBuilder(name, splitBrainProtectionSize)
                            .withAcceptableHeartbeatPauseMillis(acceptableHeartPause)
                            .withSuspicionThreshold(threshold)
                            .withHeartbeatIntervalMillis(heartbeatIntervalMillis)
                            .withMinStdDeviationMillis(minStdDeviation)
                            .withMaxSampleSize(maxSampleSize);
            return splitBrainProtectionConfigBuilder;
        }

        private void handleMergePolicyConfig(Node node, BeanDefinitionBuilder builder) {
            BeanDefinitionBuilder mergePolicyConfigBuilder = createBeanBuilder(MergePolicyConfig.class);
            AbstractBeanDefinition beanDefinition = mergePolicyConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, mergePolicyConfigBuilder);
            mergePolicyConfigBuilder.addPropertyValue("policy", getTextContent(node).trim());
            builder.addPropertyValue("mergePolicyConfig", beanDefinition);
        }

        private void handleLiteMember(Node node) {
            Node attrEnabled = node.getAttributes().getNamedItem("enabled");
            configBuilder.addPropertyValue(xmlToJavaName(cleanNodeName(node)), getTextContent(attrEnabled));
        }

        public void handleServices(Node node) {
            BeanDefinitionBuilder servicesConfigBuilder = createBeanBuilder(ServicesConfig.class);
            AbstractBeanDefinition beanDefinition = servicesConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, servicesConfigBuilder);
            ManagedList<AbstractBeanDefinition> serviceConfigManagedList = new ManagedList<>();
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("service".equals(nodeName)) {
                    serviceConfigManagedList.add(handleService(child));
                }
            }
            servicesConfigBuilder.addPropertyValue("serviceConfigs", serviceConfigManagedList);
            configBuilder.addPropertyValue("servicesConfig", beanDefinition);
        }

        private AbstractBeanDefinition handleService(Node node) {
            BeanDefinitionBuilder serviceConfigBuilder = createBeanBuilder(ServiceConfig.class);
            AbstractBeanDefinition beanDefinition = serviceConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, serviceConfigBuilder);
            boolean classNameSet = false;
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("name".equals(nodeName)) {
                    serviceConfigBuilder.addPropertyValue(xmlToJavaName(nodeName), getTextContent(child));
                } else if ("class-name".equals(nodeName)) {
                    // log message about element deprecation..?
                    serviceConfigBuilder.addPropertyValue(xmlToJavaName(nodeName), getTextContent(child));
                    classNameSet = true;
                } else if ("properties".equals(nodeName)) {
                    handleProperties(child, serviceConfigBuilder);
                } else if ("configuration".equals(nodeName)) {
                    Node parser = child.getAttributes().getNamedItem("parser");
                    String name = getTextContent(parser);
                    try {
                        ServiceConfigurationParser serviceConfigurationParser =
                                ClassLoaderUtil.newInstance(getClass().getClassLoader(), name);
                        Object obj = serviceConfigurationParser.parse((Element) child);
                        serviceConfigBuilder.addPropertyValue(xmlToJavaName("config-object"), obj);
                    } catch (Exception e) {
                        ExceptionUtil.sneakyThrow(e);
                    }
                }
            }

            NamedNodeMap attributes = node.getAttributes();
            Node classNameNode = attributes.getNamedItem("class-name");
            if (classNameNode != null) {
                serviceConfigBuilder.addPropertyValue("className", getTextContent(classNameNode));
            }
            Node implNode = attributes.getNamedItem("implementation");
            if (implNode != null) {
                serviceConfigBuilder.addPropertyReference("implementation", getTextContent(implNode));
            }
            isTrue(classNameSet || classNameNode != null || implNode != null, "One of 'class-name' or 'implementation'"
                    + " attributes is required to create ServiceConfig!");
            return beanDefinition;
        }

        public void handleReplicatedMap(Node node) {
            BeanDefinitionBuilder replicatedMapConfigBuilder = createBeanBuilder(ReplicatedMapConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, replicatedMapConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("entry-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, EntryListenerConfig.class);
                    replicatedMapConfigBuilder.addPropertyValue("listenerConfigs", listeners);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    replicatedMapConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, replicatedMapConfigBuilder);
                }
            }
            replicatedMapManagedMap.put(name, replicatedMapConfigBuilder.getBeanDefinition());
        }

        public void handleNetwork(Node node) {
            hasNetwork = true;
            BeanDefinitionBuilder networkConfigBuilder = createBeanBuilder(NetworkConfig.class);
            AbstractBeanDefinition beanDefinition = networkConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, networkConfigBuilder);
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("join".equals(nodeName)) {
                    handleJoin(child, networkConfigBuilder);
                } else if ("interfaces".equals(nodeName)) {
                    handleInterfaces(child, networkConfigBuilder);
                } else if ("symmetric-encryption".equals(nodeName)) {
                    handleSymmetricEncryption(child, networkConfigBuilder);
                } else if ("ssl".equals(nodeName)) {
                    handleSSLConfig(child, networkConfigBuilder);
                } else if ("socket-interceptor".equals(nodeName)) {
                    handleSocketInterceptorConfig(child, networkConfigBuilder);
                } else if ("outbound-ports".equals(nodeName)) {
                    handleOutboundPorts(child, networkConfigBuilder);
                } else if ("reuse-address".equals(nodeName)) {
                    handleReuseAddress(child, networkConfigBuilder);
                } else if ("member-address-provider".equals(nodeName)) {
                    handleMemberAddressProvider(child, networkConfigBuilder);
                } else if ("failure-detector".equals(nodeName)) {
                    handleFailureDetector(child, networkConfigBuilder);
                } else if ("rest-api".equals(nodeName)) {
                    handleRestApi(child, networkConfigBuilder);
                } else if ("memcache-protocol".equals(nodeName)) {
                    handleMemcacheProtocol(child, networkConfigBuilder);
                }
            }
            configBuilder.addPropertyValue("networkConfig", beanDefinition);
        }

        void handleAdvancedNetwork(Node node) {
            BeanDefinitionBuilder advNetworkConfigBuilder = createBeanBuilder(AdvancedNetworkConfig.class);
            AbstractBeanDefinition beanDefinition = advNetworkConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, advNetworkConfigBuilder);
            String enabled = getAttribute(node, "enabled");
            if (getBooleanValue(enabled)) {
                hasAdvancedNetworkEnabled = true;
            }
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("join".equals(nodeName)) {
                    handleJoin(child, advNetworkConfigBuilder);
                } else if ("member-address-provider".equals(nodeName)) {
                    handleMemberAddressProvider(child, advNetworkConfigBuilder);
                } else if ("failure-detector".equals(nodeName)) {
                    handleFailureDetector(child, advNetworkConfigBuilder);
                } else if ("wan-endpoint-config".equals(nodeName)) {
                    handleWanEndpointConfig(child);
                } else if ("member-server-socket-endpoint-config".equals(nodeName)) {
                    handleMemberServerSocketEndpointConfig(child);
                } else if ("client-server-socket-endpoint-config".equals(nodeName)) {
                    handleClientServerSocketEndpointConfig(child);
                } else if ("wan-server-socket-endpoint-config".equals(nodeName)) {
                    handleWanServerSocketEndpointConfig(child);
                } else if ("rest-server-socket-endpoint-config".equals(nodeName)) {
                    handleRestServerSocketEndpointConfig(child);
                } else if ("memcache-server-socket-endpoint-config".equals(nodeName)) {
                    handleMemcacheServerSocketEndpointConfig(child);
                }
            }
            advNetworkConfigBuilder.addPropertyValue("endpointConfigs", endpointConfigsMap);
            configBuilder.addPropertyValue("advancedNetworkConfig", beanDefinition);
        }

        void handleWanEndpointConfig(Node node) {
            BeanDefinitionBuilder endpointConfigBuilder = createBeanBuilder(EndpointConfig.class);
            handleEndpointConfig(node, ProtocolType.WAN, endpointConfigBuilder);
        }

        void handleEndpointConfig(Node node, ProtocolType type, BeanDefinitionBuilder endpointConfigBuilder) {
            AbstractBeanDefinition beanDefinition = endpointConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, endpointConfigBuilder);
            for (Node child : childElements(node)) {
                handleEndpointConfigCommons(child, endpointConfigBuilder);
            }
            endpointConfigsMap.put(createEndpointQualifier(type, node), beanDefinition);
        }

        private void handleEndpointConfigCommons(Node node, BeanDefinitionBuilder endpointConfigBuilder) {
            String nodeName = cleanNodeName(node);
            if ("outbound-ports".equals(nodeName)) {
                handleOutboundPorts(node, endpointConfigBuilder);
            } else if ("interfaces".equals(nodeName)) {
                handleInterfaces(node, endpointConfigBuilder);
            } else if ("symmetric-encryption".equals(nodeName)) {
                handleSymmetricEncryption(node, endpointConfigBuilder);
            } else if ("ssl".equals(nodeName)) {
                handleSSLConfig(node, endpointConfigBuilder);
            } else if ("socket-interceptor".equals(nodeName)) {
                handleSocketInterceptorConfig(node, endpointConfigBuilder);
            } else if ("socket-options".equals(nodeName)) {
                handleEndpointSocketOptions(node, endpointConfigBuilder);
            }
        }

        void handleMemberServerSocketEndpointConfig(Node node) {
            BeanDefinitionBuilder endpointConfigBuilder = createBeanBuilder(ServerSocketEndpointConfig.class);
            handleServerSocketEndpointConfig(node, ProtocolType.MEMBER, endpointConfigBuilder);
        }

        void handleClientServerSocketEndpointConfig(Node node) {
            BeanDefinitionBuilder endpointConfigBuilder = createBeanBuilder(ServerSocketEndpointConfig.class);
            handleServerSocketEndpointConfig(node, ProtocolType.CLIENT, endpointConfigBuilder);
        }

        void handleWanServerSocketEndpointConfig(Node node) {
            BeanDefinitionBuilder endpointConfigBuilder = createBeanBuilder(ServerSocketEndpointConfig.class);
            handleServerSocketEndpointConfig(node, ProtocolType.WAN, endpointConfigBuilder);
        }

        void handleRestServerSocketEndpointConfig(Node node) {
            BeanDefinitionBuilder endpointConfigBuilder = createBeanBuilder(RestServerEndpointConfig.class);
            handleServerSocketEndpointConfig(node, ProtocolType.REST, endpointConfigBuilder);

            ManagedSet<RestEndpointGroup> groupSet = new ManagedSet<>();
            for (RestEndpointGroup group : RestEndpointGroup.values()) {
                if (group.isEnabledByDefault()) {
                    groupSet.add(group);
                }
            }

            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("endpoint-groups".equals(nodeName)) {
                    handleRestEndpointGroup(node, endpointConfigBuilder, groupSet);
                }
            }
        }

        void handleMemcacheServerSocketEndpointConfig(Node node) {
            BeanDefinitionBuilder endpointConfigBuilder = createBeanBuilder(ServerSocketEndpointConfig.class);
            handleServerSocketEndpointConfig(node, ProtocolType.MEMCACHE, endpointConfigBuilder);
        }

        void handleServerSocketEndpointConfig(Node node, ProtocolType type, BeanDefinitionBuilder endpointBuilder) {
            AbstractBeanDefinition beanDefinition = endpointBuilder.getBeanDefinition();
            fillAttributeValues(node, endpointBuilder);
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("reuse-address".equals(nodeName)) {
                    String value = getTextContent(child).trim();
                    endpointBuilder.addPropertyValue("reuseAddress", value);
                } else {
                    handleEndpointConfigCommons(child, endpointBuilder);
                }
            }
            endpointConfigsMap.put(createEndpointQualifier(type, node), beanDefinition);
        }

        void handleEndpointSocketOptions(Node node, BeanDefinitionBuilder endpointConfigBuilder) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                String textContent = getTextContent(child);
                if ("buffer-direct".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketBufferDirect",
                            getBooleanValue(textContent));
                } else if ("tcp-no-delay".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketTcpNoDelay",
                            getBooleanValue(textContent));
                } else if ("keep-alive".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketKeepAlive",
                            getBooleanValue(textContent));
                } else if ("connect-timeout-seconds".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketConnectTimeoutSeconds",
                            getIntegerValue("socketConnectTimeoutSeconds", textContent));
                } else if ("send-buffer-size-kb".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketSendBufferSizeKb",
                            getIntegerValue("socketSendBufferSizeKb", textContent));
                } else if ("receive-buffer-size-kb".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketRcvBufferSizeKb",
                            getIntegerValue("socketRcvBufferSizeKb", textContent));
                } else if ("linger-seconds".equals(nodeName)) {
                    endpointConfigBuilder.addPropertyValue("socketLingerSeconds",
                            getIntegerValue("socketLingerSeconds", textContent));
                }
            }
        }

        public void handleProperties(Node node) {
            handleProperties(node, configBuilder);
        }

        public void handleInterfaces(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            BeanDefinitionBuilder builder = createBeanBuilder(InterfacesConfig.class);
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                for (int a = 0; a < attributes.getLength(); a++) {
                    Node att = attributes.item(a);
                    String name = xmlToJavaName(att.getNodeName());
                    String value = att.getNodeValue();
                    builder.addPropertyValue(name, value);
                }
            }
            ManagedList<String> interfacesSet = new ManagedList<>();
            for (Node n : childElements(node)) {
                String name = xmlToJavaName(cleanNodeName(n));
                String value = getTextContent(n);
                if ("interface".equals(name)) {
                    interfacesSet.add(value);
                }
            }
            builder.addPropertyValue("interfaces", interfacesSet);
            networkConfigBuilder.addPropertyValue("interfaces", beanDefinition);
        }

        public void handleJoin(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            BeanDefinitionBuilder joinConfigBuilder = createBeanBuilder(JoinConfig.class);
            AbstractBeanDefinition beanDefinition = joinConfigBuilder.getBeanDefinition();
            for (Node child : childElements(node)) {
                String name = cleanNodeName(child);
                if ("multicast".equals(name)) {
                    handleMulticast(child, joinConfigBuilder);
                } else if ("tcp-ip".equals(name)) {
                    handleTcpIp(child, joinConfigBuilder);
                } else if (AliasedDiscoveryConfigUtils.supports(name)) {
                    handleAliasedDiscoveryStrategy(child, joinConfigBuilder, name);
                } else if ("discovery-strategies".equals(name)) {
                    handleDiscoveryStrategies(child, joinConfigBuilder);
                }
            }
            networkConfigBuilder.addPropertyValue("join", beanDefinition);
        }

        private void handleOutboundPorts(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            ManagedList<String> outboundPorts = new ManagedList<>();
            for (Node child : childElements(node)) {
                String name = cleanNodeName(child);
                if ("ports".equals(name)) {
                    String value = getTextContent(child);
                    outboundPorts.add(value);
                }
            }
            networkConfigBuilder.addPropertyValue("outboundPortDefinitions", outboundPorts);
        }

        private void handleReuseAddress(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            String value = node.getTextContent();
            networkConfigBuilder.addPropertyValue("reuseAddress", value);
        }

        private void handleMemberAddressProvider(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            BeanDefinitionBuilder memberAddressProviderConfigBuilder = createBeanBuilder(MemberAddressProviderConfig.class);
            for (Node child : childElements(node)) {
                if ("properties".equals(cleanNodeName(child))) {
                    handleProperties(child, memberAddressProviderConfigBuilder);
                    break;
                }
            }
            String implementationAttr = "implementation";
            String classNameAttr = "class-name";
            String implementationValue = getAttribute(node, implementationAttr);
            String classNameValue = getAttribute(node, classNameAttr);

            memberAddressProviderConfigBuilder.addPropertyValue("enabled", getBooleanValue(getAttribute(node, "enabled")));

            if (!isNullOrEmpty(implementationValue)) {
                memberAddressProviderConfigBuilder.addPropertyReference(implementationAttr, implementationValue);
            } else {
                if (isNullOrEmpty(classNameValue)) {
                    throw new InvalidConfigurationException("One of the \"class-name\" or \"implementation\" configuration"
                            + " is needed for member address provider configuration");
                }
                memberAddressProviderConfigBuilder.addPropertyValue("className", classNameValue);
            }
            networkConfigBuilder.addPropertyValue("memberAddressProviderConfig",
                    memberAddressProviderConfigBuilder.getBeanDefinition());
        }

        private void handleFailureDetector(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            if (!node.hasChildNodes()) {
                return;
            }

            for (Node child : childElements(node)) {
                // icmp only
                if (!cleanNodeName(child).equals("icmp")) {
                    throw new IllegalStateException("Unsupported child under Failure-Detector");
                }

                Node enabledNode = child.getAttributes().getNamedItem("enabled");
                boolean enabled = enabledNode != null && getBooleanValue(getTextContent(enabledNode));
                BeanDefinitionBuilder icmpFailureDetectorConfigBuilder = createBeanBuilder(IcmpFailureDetectorConfig.class);

                icmpFailureDetectorConfigBuilder.addPropertyValue("enabled", enabled);
                for (Node n : childElements(child)) {
                    String nodeName = cleanNodeName(n);

                    if (nodeName.equals("ttl")) {
                        int ttl = parseInt(getTextContent(n));
                        icmpFailureDetectorConfigBuilder.addPropertyValue("ttl", ttl);
                    } else if (nodeName.equals("timeout-milliseconds")) {
                        int timeout = parseInt(getTextContent(n));
                        icmpFailureDetectorConfigBuilder.addPropertyValue("timeoutMilliseconds", timeout);
                    } else if (nodeName.equals("parallel-mode")) {
                        boolean mode = parseBoolean(getTextContent(n));
                        icmpFailureDetectorConfigBuilder.addPropertyValue("parallelMode", mode);
                    } else if (nodeName.equals("fail-fast-on-startup")) {
                        boolean failOnStartup = parseBoolean(getTextContent(n));
                        icmpFailureDetectorConfigBuilder.addPropertyValue("failFastOnStartup", failOnStartup);
                    } else if (nodeName.equals("max-attempts")) {
                        int attempts = parseInt(getTextContent(n));
                        icmpFailureDetectorConfigBuilder.addPropertyValue("maxAttempts", attempts);
                    } else if (nodeName.equals("interval-milliseconds")) {
                        int interval = parseInt(getTextContent(n));
                        icmpFailureDetectorConfigBuilder.addPropertyValue("intervalMilliseconds", interval);
                    }
                }

                networkConfigBuilder.addPropertyValue("icmpFailureDetectorConfig",
                        icmpFailureDetectorConfigBuilder.getBeanDefinition());
            }
        }

        private void handleSSLConfig(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            BeanDefinitionBuilder sslConfigBuilder = createBeanBuilder(SSLConfig.class);
            String implAttribute = "factory-implementation";
            fillAttributeValues(node, sslConfigBuilder, implAttribute);
            Node implNode = node.getAttributes().getNamedItem(implAttribute);
            String implementation = implNode != null ? getTextContent(implNode) : null;
            if (implementation != null) {
                sslConfigBuilder.addPropertyReference(xmlToJavaName(implAttribute), implementation);
            }
            for (Node child : childElements(node)) {
                String name = cleanNodeName(child);
                if ("properties".equals(name)) {
                    handleProperties(child, sslConfigBuilder);
                }
            }
            networkConfigBuilder.addPropertyValue("SSLConfig", sslConfigBuilder.getBeanDefinition());
        }

        public void handleSymmetricEncryption(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            createAndFillBeanBuilder(node, SymmetricEncryptionConfig.class, "symmetricEncryptionConfig", networkConfigBuilder);
        }

        public void handleExecutor(Node node) {
            createAndFillListedBean(node, ExecutorConfig.class, "name", executorManagedMap);
        }

        public void handleDurableExecutor(Node node) {
            createAndFillListedBean(node, DurableExecutorConfig.class, "name", durableExecutorManagedMap);
        }

        public void handleScheduledExecutor(Node node) {
            BeanDefinitionBuilder builder = createAndFillListedBean(node, ScheduledExecutorConfig.class,
                    "name", scheduledExecutorManagedMap, "mergePolicy");

            for (Node n : childElements(node)) {
                String name = cleanNodeName(n);
                if ("merge-policy".equals(name)) {
                    handleMergePolicyConfig(n, builder);
                }
            }
        }

        public void handleCardinalityEstimator(Node node) {
            BeanDefinitionBuilder builder = createAndFillListedBean(node, CardinalityEstimatorConfig.class, "name",
                    cardinalityEstimatorManagedMap, "mergePolicy");

            for (Node n : childElements(node)) {
                String name = cleanNodeName(n);
                if ("merge-policy".equals(name)) {
                    handleMergePolicyConfig(n, builder);
                }
            }
        }

        public void handlePNCounter(Node node) {
            createAndFillListedBean(node, PNCounterConfig.class, "name", pnCounterManagedMap);
        }

        public void handleMulticast(Node node, BeanDefinitionBuilder joinConfigBuilder) {
            BeanDefinitionBuilder builder = createAndFillBeanBuilder(node, MulticastConfig.class, "multicastConfig",
                    joinConfigBuilder, "trusted-interfaces", "interface");
            ManagedList<String> interfaces = new ManagedList<>();
            for (Node n : childElements(node)) {
                String name = cleanNodeName(n);
                if ("trusted-interfaces".equals(name)) {
                    for (Node i : childElements(n)) {
                        name = cleanNodeName(i);
                        if ("interface".equals(name)) {
                            String value = getTextContent(i);
                            interfaces.add(value);
                        }
                    }
                }
            }
            builder.addPropertyValue("trustedInterfaces", interfaces);
        }

        public void handleTcpIp(Node node, BeanDefinitionBuilder joinConfigBuilder) {
            BeanDefinitionBuilder builder = createAndFillBeanBuilder(node, TcpIpConfig.class,
                    "tcpIpConfig", joinConfigBuilder, "interface", "member", "members");
            ManagedList<String> members = new ManagedList<>();
            for (Node n : childElements(node)) {
                String name = cleanNodeName(n);
                if ("member".equals(name) || "members".equals(name) || "interface".equals(name)) {
                    String value = getTextContent(n);
                    members.add(value);
                }
            }
            builder.addPropertyValue("members", members);
        }

        private void handleAliasedDiscoveryStrategy(Node node, BeanDefinitionBuilder builder, String name) {
            AliasedDiscoveryConfig config = AliasedDiscoveryConfigUtils.newConfigFor(name);
            fillAttributesForAliasedDiscoveryStrategy(config, node, builder, name);
        }

        public void handleReliableTopic(Node node) {
            BeanDefinitionBuilder builder = createBeanBuilder(ReliableTopicConfig.class);
            fillAttributeValues(node, builder);
            for (Node childNode : childElements(node)) {
                if ("message-listeners".equals(cleanNodeName(childNode))) {
                    ManagedList listeners = parseListeners(childNode, ListenerConfig.class);
                    builder.addPropertyValue("messageListenerConfigs", listeners);
                }
            }
            reliableTopicManagedMap.put(getAttribute(node, "name"), builder.getBeanDefinition());
        }

        public void handleRingbuffer(Node node) {
            BeanDefinitionBuilder ringbufferConfigBuilder = createBeanBuilder(RingbufferConfig.class);
            fillAttributeValues(node, ringbufferConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("ringbuffer-store".equals(nodeName)) {
                    handleRingbufferStoreConfig(childNode, ringbufferConfigBuilder);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    ringbufferConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, ringbufferConfigBuilder);
                }
            }
            ringbufferManagedMap.put(getAttribute(node, "name"), ringbufferConfigBuilder.getBeanDefinition());
        }

        public void handleRingbufferStoreConfig(Node node, BeanDefinitionBuilder ringbufferConfigBuilder) {
            BeanDefinitionBuilder builder = createBeanBuilder(RingbufferStoreConfig.class);
            for (Node child : childElements(node)) {
                if ("properties".equals(cleanNodeName(child))) {
                    handleProperties(child, builder);
                    break;
                }
            }
            extractBasicStoreConfig(node, builder);
            ringbufferConfigBuilder.addPropertyValue("ringbufferStoreConfig", builder.getBeanDefinition());
        }

        public void handleQueue(Node node) {
            BeanDefinitionBuilder queueConfigBuilder = createBeanBuilder(QueueConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, queueConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("item-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, ItemListenerConfig.class);
                    queueConfigBuilder.addPropertyValue("itemListenerConfigs", listeners);
                } else if ("queue-store".equals(nodeName)) {
                    handleQueueStoreConfig(childNode, queueConfigBuilder);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    queueConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, queueConfigBuilder);
                }
            }
            queueManagedMap.put(name, queueConfigBuilder.getBeanDefinition());
        }

        public void handleQueueStoreConfig(Node node, BeanDefinitionBuilder queueConfigBuilder) {
            BeanDefinitionBuilder queueStoreConfigBuilder = createBeanBuilder(QueueStoreConfig.class);
            AbstractBeanDefinition beanDefinition = queueStoreConfigBuilder.getBeanDefinition();
            for (Node child : childElements(node)) {
                if ("properties".equals(cleanNodeName(child))) {
                    handleProperties(child, queueStoreConfigBuilder);
                    break;
                }
            }
            String storeImplAttrName = "store-implementation";
            String factoryImplAttrName = "factory-implementation";
            fillAttributeValues(node, queueStoreConfigBuilder, storeImplAttrName, factoryImplAttrName);
            NamedNodeMap attributes = node.getAttributes();
            Node implRef = attributes.getNamedItem(storeImplAttrName);
            Node factoryImplRef = attributes.getNamedItem(factoryImplAttrName);
            if (factoryImplRef != null) {
                queueStoreConfigBuilder.addPropertyReference(xmlToJavaName(factoryImplAttrName), getTextContent(factoryImplRef));
            }
            if (implRef != null) {
                queueStoreConfigBuilder.addPropertyReference(xmlToJavaName(storeImplAttrName), getTextContent(implRef));
            }
            queueConfigBuilder.addPropertyValue("queueStoreConfig", beanDefinition);
        }

        private void extractBasicStoreConfig(Node node, BeanDefinitionBuilder builder) {
            String storeImplAttrName = "implementation";
            String factoryImplAttrName = "factory-implementation";
            fillAttributeValues(node, builder, storeImplAttrName, factoryImplAttrName);
            String implRef = getAttribute(node, storeImplAttrName);
            String factoryImplRef = getAttribute(node, factoryImplAttrName);
            if (factoryImplRef != null) {
                builder.addPropertyReference(xmlToJavaName(factoryImplAttrName), factoryImplRef);
            }
            if (implRef != null) {
                builder.addPropertyReference(xmlToJavaName("store-implementation"), implRef);
            }
        }

        public void handleList(Node node) {
            BeanDefinitionBuilder listConfigBuilder = createBeanBuilder(ListConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, listConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("item-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, ItemListenerConfig.class);
                    listConfigBuilder.addPropertyValue("itemListenerConfigs", listeners);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    listConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, listConfigBuilder);
                }
            }
            listManagedMap.put(name, listConfigBuilder.getBeanDefinition());
        }

        public void handleSet(Node node) {
            BeanDefinitionBuilder setConfigBuilder = createBeanBuilder(SetConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, setConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("item-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, ItemListenerConfig.class);
                    setConfigBuilder.addPropertyValue("itemListenerConfigs", listeners);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    setConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, setConfigBuilder);
                }
            }
            setManagedMap.put(name, setConfigBuilder.getBeanDefinition());
        }

        @SuppressWarnings({"checkstyle:methodlength", "checkstyle:npathcomplexity"})
        public void handleMap(Node node) {
            BeanDefinitionBuilder mapConfigBuilder = createBeanBuilder(MapConfig.class);
            AbstractBeanDefinition beanDefinition = mapConfigBuilder.getBeanDefinition();
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            mapConfigBuilder.addPropertyValue("name", name);
            fillAttributeValues(node, mapConfigBuilder, "maxSize", "maxSizePolicy");
            BeanDefinitionBuilder maxSizeConfigBuilder = createBeanBuilder(MaxSizeConfig.class);
            AbstractBeanDefinition maxSizeConfigBeanDefinition = maxSizeConfigBuilder.getBeanDefinition();
            mapConfigBuilder.addPropertyValue("maxSizeConfig", maxSizeConfigBeanDefinition);
            Node maxSizeNode = node.getAttributes().getNamedItem("max-size");
            if (maxSizeNode != null) {
                maxSizeConfigBuilder.addPropertyValue("size", getTextContent(maxSizeNode));
            }
            Node maxSizePolicyNode = node.getAttributes().getNamedItem("max-size-policy");
            if (maxSizePolicyNode != null) {
                maxSizeConfigBuilder.addPropertyValue(xmlToJavaName(cleanNodeName(maxSizePolicyNode)),
                        MaxSizePolicy.valueOf(getTextContent(maxSizePolicyNode)));
            }
            Node cacheDeserializedValueNode = node.getAttributes().getNamedItem("cache-deserialized-values");
            if (cacheDeserializedValueNode != null) {
                mapConfigBuilder.addPropertyValue("cacheDeserializedValues", getTextContent(cacheDeserializedValueNode));
            }

            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("map-store".equals(nodeName)) {
                    handleMapStoreConfig(childNode, mapConfigBuilder);
                } else if ("near-cache".equals(nodeName)) {
                    handleNearCacheConfig(childNode, mapConfigBuilder);
                } else if ("wan-replication-ref".equals(nodeName)) {
                    handleWanReplicationRef(mapConfigBuilder, childNode);
                } else if ("indexes".equals(nodeName)) {
                    ManagedList<BeanDefinition> indexes = new ManagedList<>();
                    for (Node indexNode : childElements(childNode)) {
                        handleIndex(indexes, indexNode);
                    }
                    mapConfigBuilder.addPropertyValue("indexConfigs", indexes);
                } else if ("attributes".equals(nodeName)) {
                    ManagedList<BeanDefinition> attributes = new ManagedList<>();
                    for (Node attributeNode : childElements(childNode)) {
                        BeanDefinitionBuilder attributeConfBuilder = createBeanBuilder(AttributeConfig.class);
                        fillAttributeValues(attributeNode, attributeConfBuilder);
                        attributes.add(attributeConfBuilder.getBeanDefinition());
                    }
                    mapConfigBuilder.addPropertyValue("attributeConfigs", attributes);
                } else if ("entry-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, EntryListenerConfig.class);
                    mapConfigBuilder.addPropertyValue("entryListenerConfigs", listeners);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    mapConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, mapConfigBuilder);
                } else if ("query-caches".equals(nodeName)) {
                    ManagedList queryCaches = getQueryCaches(childNode);
                    mapConfigBuilder.addPropertyValue("queryCacheConfigs", queryCaches);
                } else if ("partition-lost-listeners".endsWith(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, MapPartitionLostListenerConfig.class);
                    mapConfigBuilder.addPropertyValue("partitionLostListenerConfigs", listeners);
                } else if ("merkle-tree".equals(nodeName)) {
                    handleMerkleTreeConfig(mapConfigBuilder, childNode);
                } else if ("hot-restart".equals(nodeName)) {
                    handleHotRestartConfig(mapConfigBuilder, childNode);
                } else if ("event-journal".equals(nodeName)) {
                    handleEventJournalConfig(mapConfigBuilder, childNode);
                } else if ("map-eviction-policy".equals(nodeName)) {
                    handleMapEvictionPolicyConfig(mapConfigBuilder, childNode);
                } else if ("partition-strategy".equals(nodeName)) {
                    PartitioningStrategyConfig psConfig = new PartitioningStrategyConfig(getTextContent(childNode));
                    mapConfigBuilder.addPropertyValue("partitioningStrategyConfig", psConfig);
                }
            }
            mapConfigManagedMap.put(name, beanDefinition);
        }

        private void handleMapEvictionPolicyConfig(BeanDefinitionBuilder mapConfigBuilder, Node childNode) {
            NamedNodeMap attributes = childNode.getAttributes();
            Node implementationNode = attributes.getNamedItem("implementation");
            Node classNameNode = attributes.getNamedItem("class-name");

            String implementation = implementationNode != null ? getTextContent(implementationNode) : null;
            String className = classNameNode != null ? getTextContent(classNameNode) : null;

            if (implementation != null) {
                mapConfigBuilder.addPropertyReference("mapEvictionPolicy", implementation);
            } else if (className != null) {
                className = checkHasText(className, "map-eviction-policy `className` cannot be null or empty");
                try {
                    MapEvictionPolicy mapEvictionPolicy = ClassLoaderUtil.newInstance(getClass().getClassLoader(), className);
                    mapConfigBuilder.addPropertyValue("mapEvictionPolicy", mapEvictionPolicy);

                } catch (Exception e) {
                    throw rethrow(e);
                }
            } else {
                throw new IllegalArgumentException("One of `className` or `implementation`"
                        + " attributes is required to create map-eviction-policy");
            }
        }

        private void handleMerkleTreeConfig(BeanDefinitionBuilder configBuilder, Node node) {
            BeanDefinitionBuilder merkleTreeBuilder = createBeanBuilder(MerkleTreeConfig.class);
            fillAttributeValues(node, merkleTreeBuilder);
            configBuilder.addPropertyValue("merkleTreeConfig", merkleTreeBuilder.getBeanDefinition());
        }

        private void handleHotRestartConfig(BeanDefinitionBuilder configBuilder, Node node) {
            BeanDefinitionBuilder hotRestartConfigBuilder = createBeanBuilder(HotRestartConfig.class);
            fillAttributeValues(node, hotRestartConfigBuilder);
            configBuilder.addPropertyValue("hotRestartConfig", hotRestartConfigBuilder.getBeanDefinition());
        }

        private void handleEventJournalConfig(BeanDefinitionBuilder configBuilder, Node node) {
            BeanDefinitionBuilder eventJournalBuilder = createBeanBuilder(EventJournalConfig.class);
            fillAttributeValues(node, eventJournalBuilder);
            configBuilder.addPropertyValue("eventJournalConfig", eventJournalBuilder.getBeanDefinition());
        }

        private ManagedList getQueryCaches(Node childNode) {
            ManagedList<BeanDefinition> queryCaches = new ManagedList<>();
            for (Node queryCacheNode : childElements(childNode)) {
                BeanDefinitionBuilder beanDefinitionBuilder = parseQueryCaches(queryCacheNode);
                queryCaches.add(beanDefinitionBuilder.getBeanDefinition());
            }
            return queryCaches;
        }

        @SuppressWarnings("checkstyle:methodlength")
        private BeanDefinitionBuilder parseQueryCaches(Node queryCacheNode) {
            BeanDefinitionBuilder builder = createBeanBuilder(QueryCacheConfig.class);

            for (Node node : childElements(queryCacheNode)) {
                String nodeName = cleanNodeName(node);
                String textContent = getTextContent(node);
                NamedNodeMap attributes = queryCacheNode.getAttributes();
                String cacheName = getTextContent(attributes.getNamedItem("name"));
                builder.addPropertyValue("name", cacheName);

                if ("predicate".equals(nodeName)) {
                    BeanDefinitionBuilder predicateBuilder = createBeanBuilder(PredicateConfig.class);
                    String predicateType = getTextContent(node.getAttributes().getNamedItem("type"));
                    if ("sql".equals(predicateType)) {
                        predicateBuilder.addPropertyValue("sql", textContent);
                    } else if ("class-name".equals(predicateType)) {
                        predicateBuilder.addPropertyValue("className", textContent);
                    }
                    builder.addPropertyValue("predicateConfig", predicateBuilder.getBeanDefinition());
                } else if ("entry-listeners".equals(nodeName)) {
                    ManagedList<BeanDefinition> listeners = new ManagedList<>();
                    String implementationAttr = "implementation";
                    for (Node listenerNode : childElements(node)) {
                        BeanDefinitionBuilder listenerConfBuilder = createBeanBuilder(EntryListenerConfig.class);
                        fillAttributeValues(listenerNode, listenerConfBuilder, implementationAttr);
                        Node implementationNode = listenerNode.getAttributes().getNamedItem(implementationAttr);
                        if (implementationNode != null) {
                            listenerConfBuilder.addPropertyReference(implementationAttr, getTextContent(implementationNode));
                        }
                        listeners.add(listenerConfBuilder.getBeanDefinition());
                    }
                    builder.addPropertyValue("entryListenerConfigs", listeners);
                } else if ("include-value".equals(nodeName)) {
                    boolean includeValue = getBooleanValue(textContent);
                    builder.addPropertyValue("includeValue", includeValue);
                } else if ("batch-size".equals(nodeName)) {
                    int batchSize = getIntegerValue("batch-size", textContent.trim()
                    );
                    builder.addPropertyValue("batchSize", batchSize);
                } else if ("buffer-size".equals(nodeName)) {
                    int bufferSize = getIntegerValue("buffer-size", textContent.trim()
                    );
                    builder.addPropertyValue("bufferSize", bufferSize);
                } else if ("delay-seconds".equals(nodeName)) {
                    int delaySeconds = getIntegerValue("delay-seconds", textContent.trim()
                    );
                    builder.addPropertyValue("delaySeconds", delaySeconds);
                } else if ("in-memory-format".equals(nodeName)) {
                    String value = textContent.trim();
                    builder.addPropertyValue("inMemoryFormat", InMemoryFormat.valueOf(upperCaseInternal(value)));
                } else if ("coalesce".equals(nodeName)) {
                    boolean coalesce = getBooleanValue(textContent);
                    builder.addPropertyValue("coalesce", coalesce);
                } else if ("populate".equals(nodeName)) {
                    boolean populate = getBooleanValue(textContent);
                    builder.addPropertyValue("populate", populate);
                } else if ("indexes".equals(nodeName)) {
                    ManagedList<BeanDefinition> indexes = new ManagedList<>();
                    for (Node indexNode : childElements(node)) {
                        handleIndex(indexes, indexNode);
                    }
                    builder.addPropertyValue("indexConfigs", indexes);
                } else if ("eviction".equals(nodeName)) {
                    builder.addPropertyValue("evictionConfig", getEvictionConfig(node, false));
                }
            }
            return builder;
        }

        public void handleCache(Node node) {
            BeanDefinitionBuilder cacheConfigBuilder = createBeanBuilder(CacheSimpleConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, cacheConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("eviction".equals(nodeName)) {
                    cacheConfigBuilder.addPropertyValue("evictionConfig", getEvictionConfig(childNode, false));
                } else if ("expiry-policy-factory".equals(cleanNodeName(childNode))) {
                    cacheConfigBuilder.addPropertyValue("expiryPolicyFactoryConfig", getExpiryPolicyFactoryConfig(childNode));
                } else if ("cache-entry-listeners".equals(nodeName)) {
                    ManagedList<BeanDefinition> listeners = new ManagedList<>();
                    for (Node listenerNode : childElements(childNode)) {
                        BeanDefinitionBuilder listenerConfBuilder = createBeanBuilder(CacheSimpleEntryListenerConfig.class);
                        fillAttributeValues(listenerNode, listenerConfBuilder);
                        listeners.add(listenerConfBuilder.getBeanDefinition());
                    }
                    cacheConfigBuilder.addPropertyValue("cacheEntryListeners", listeners);
                } else if ("wan-replication-ref".equals(nodeName)) {
                    handleWanReplicationRef(cacheConfigBuilder, childNode);
                } else if ("partition-lost-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, CachePartitionLostListenerConfig.class);
                    cacheConfigBuilder.addPropertyValue("partitionLostListenerConfigs", listeners);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    cacheConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, cacheConfigBuilder);
                } else if ("hot-restart".equals(nodeName)) {
                    handleHotRestartConfig(cacheConfigBuilder, childNode);
                } else if ("event-journal".equals(nodeName)) {
                    handleEventJournalConfig(cacheConfigBuilder, childNode);
                }
            }
            cacheConfigManagedMap.put(name, cacheConfigBuilder.getBeanDefinition());
        }

        public void handleWanReplication(Node node) {
            BeanDefinitionBuilder replicationConfigBuilder = createBeanBuilder(WanReplicationConfig.class);
            String name = getAttribute(node, "name");
            replicationConfigBuilder.addPropertyValue("name", name);

            ManagedList<AbstractBeanDefinition> batchPublishers = new ManagedList<>();
            ManagedList<AbstractBeanDefinition> customPublishers = new ManagedList<>();

            for (Node n : childElements(node)) {
                String nName = cleanNodeName(n);
                if ("batch-publisher".equals(nName)) {
                    batchPublishers.add(handleBatchPublisher(n));
                }
                if ("custom-publisher".equals(nName)) {
                    customPublishers.add(handleCustomPublisher(n));
                } else if ("consumer".equals(nName)) {
                    replicationConfigBuilder.addPropertyValue("wanConsumerConfig", handleWanConsumer(n));
                }
            }
            replicationConfigBuilder.addPropertyValue("batchPublisherConfigs", batchPublishers);
            replicationConfigBuilder.addPropertyValue("customPublisherConfigs", customPublishers);
            wanReplicationManagedMap.put(name, replicationConfigBuilder.getBeanDefinition());
        }

        private AbstractBeanDefinition handleBatchPublisher(Node n) {
            BeanDefinitionBuilder builder = createBeanBuilder(WanBatchReplicationPublisherConfig.class);
            AbstractBeanDefinition definition = builder.getBeanDefinition();

            ArrayList<String> excluded = new ArrayList<>(AliasedDiscoveryConfigUtils.getTags());
            excluded.add("properties");
            excluded.add("discoveryStrategies");
            excluded.add("wanSync");

            fillValues(n, builder, excluded.toArray(new String[0]));

            for (Node child : childElements(n)) {

                String nodeName = cleanNodeName(child);
                if ("properties".equals(nodeName)) {
                    handleProperties(child, builder);
                } else if (AliasedDiscoveryConfigUtils.supports(nodeName)) {
                    handleAliasedDiscoveryStrategy(child, builder, nodeName);
                } else if ("discovery-strategies".equals(nodeName)) {
                    handleDiscoveryStrategies(child, builder);
                } else if ("wan-sync".equals(nodeName)) {
                    createAndFillBeanBuilder(child, WanSyncConfig.class, "wanSyncConfig", builder);
                }
            }
            return definition;
        }

        private AbstractBeanDefinition handleCustomPublisher(Node n) {
            BeanDefinitionBuilder builder = createBeanBuilder(CustomWanPublisherConfig.class);
            AbstractBeanDefinition definition = builder.getBeanDefinition();
            fillValues(n, builder, "properties");

            String className = getAttribute(n, "class-name");
            String implementation = getAttribute(n, "implementation");

            builder.addPropertyValue("className", className);
            if (implementation != null) {
                builder.addPropertyReference("implementation", implementation);
            }
            isTrue(className != null || implementation != null, "One of 'class-name' or 'implementation'"
                    + " attributes is required to create CustomWanPublisherConfig!");
            for (Node child : childElements(n)) {

                String nodeName = cleanNodeName(child);
                if ("properties".equals(nodeName)) {
                    handleProperties(child, builder);
                }
            }
            return definition;
        }

        private AbstractBeanDefinition handleWanConsumer(Node n) {
            BeanDefinitionBuilder consumerConfigBuilder = createBeanBuilder(WanConsumerConfig.class);

            String className = getAttribute(n, "class-name");
            String implementation = getAttribute(n, "implementation");
            boolean persistWanReplicatedData = getBooleanValue(getAttribute(n, "persist-wan-replicated-data"));

            consumerConfigBuilder.addPropertyValue("className", className);
            if (implementation != null) {
                consumerConfigBuilder.addPropertyReference("implementation", implementation);
            }
            consumerConfigBuilder.addPropertyValue("persistWanReplicatedData", persistWanReplicatedData);

            for (Node child : childElements(n)) {
                String nodeName = cleanNodeName(child);
                if ("properties".equals(nodeName)) {
                    handleProperties(child, consumerConfigBuilder);
                }
            }
            return consumerConfigBuilder.getBeanDefinition();
        }

        private void handlePartitionGroup(Node node) {
            BeanDefinitionBuilder partitionConfigBuilder = createBeanBuilder(PartitionGroupConfig.class);
            fillAttributeValues(node, partitionConfigBuilder);

            ManagedList<BeanDefinition> memberGroups = new ManagedList<>();
            for (Node child : childElements(node)) {
                String name = cleanNodeName(child);
                if ("member-group".equals(name)) {
                    BeanDefinitionBuilder memberGroupBuilder = createBeanBuilder(MemberGroupConfig.class);
                    ManagedList<String> interfaces = new ManagedList<>();
                    for (Node n : childElements(child)) {
                        if ("interface".equals(cleanNodeName(n))) {
                            interfaces.add(getTextContent(n));
                        }
                    }
                    memberGroupBuilder.addPropertyValue("interfaces", interfaces);
                    memberGroups.add(memberGroupBuilder.getBeanDefinition());
                }
            }
            partitionConfigBuilder.addPropertyValue("memberGroupConfigs", memberGroups);
            configBuilder.addPropertyValue("partitionGroupConfig", partitionConfigBuilder.getBeanDefinition());
        }

        private void handleManagementCenter(Node node) {
            BeanDefinitionBuilder managementCenterConfigBuilder = createBeanBuilder(ManagementCenterConfig.class);
            fillAttributeValues(node, managementCenterConfigBuilder);
            // < 3.9 - Backwards compatibility
            boolean isComplexType = false;
            List<String> complexTypeElements = Arrays.asList("url", "mutual-auth");
            for (Node c : childElements(node)) {
                if (complexTypeElements.contains(cleanNodeName(c))) {
                    isComplexType = true;
                    break;
                }
            }
            if (isComplexType) {
                for (Node child : childElements(node)) {
                    if ("url".equals(cleanNodeName(child))) {
                        String url = getTextContent(child);
                        managementCenterConfigBuilder.addPropertyValue("url", url);
                    } else if ("mutual-auth".equals(cleanNodeName(child))) {
                        managementCenterConfigBuilder.addPropertyValue("mutualAuthConfig",
                                handleMcMutualAuthConfig(child).getBeanDefinition());
                    }
                }
            }
            configBuilder.addPropertyValue("managementCenterConfig", managementCenterConfigBuilder.getBeanDefinition());
        }

        private BeanDefinitionBuilder handleMcMutualAuthConfig(Node node) {
            BeanDefinitionBuilder mcMutualAuthConfigBuilder = createBeanBuilder(MCMutualAuthConfig.class);
            fillAttributeValues(node, mcMutualAuthConfigBuilder);
            for (Node n : childElements(node)) {
                String nodeName = cleanNodeName(n);
                if ("factory-class-name".equals(nodeName)) {
                    mcMutualAuthConfigBuilder.addPropertyValue("factoryClassName", getTextContent(n).trim());
                } else if ("properties".equals(nodeName)) {
                    handleProperties(n, mcMutualAuthConfigBuilder);
                }
            }
            return mcMutualAuthConfigBuilder;
        }

        public void handleNearCacheConfig(Node node, BeanDefinitionBuilder configBuilder) {
            BeanDefinitionBuilder nearCacheConfigBuilder = createBeanBuilder(NearCacheConfig.class);
            fillAttributeValues(node, nearCacheConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("eviction".equals(nodeName)) {
                    handleEvictionConfig(childNode, nearCacheConfigBuilder, true);
                }
            }
            configBuilder.addPropertyValue("nearCacheConfig", nearCacheConfigBuilder.getBeanDefinition());
        }

        private void handleEvictionConfig(Node node, BeanDefinitionBuilder configBuilder, boolean isNearCache) {
            configBuilder.addPropertyValue("evictionConfig", getEvictionConfig(node, isNearCache));
        }

        private ExpiryPolicyFactoryConfig getExpiryPolicyFactoryConfig(Node node) {
            String className = getAttribute(node, "class-name");
            if (!isNullOrEmpty(className)) {
                return new ExpiryPolicyFactoryConfig(className);
            } else {
                TimedExpiryPolicyFactoryConfig timedExpiryPolicyFactoryConfig = null;
                for (Node n : childElements(node)) {
                    String nodeName = cleanNodeName(n);
                    if ("timed-expiry-policy-factory".equals(nodeName)) {
                        String expiryPolicyTypeStr = getAttribute(n, "expiry-policy-type");
                        String durationAmountStr = getAttribute(n, "duration-amount");
                        String timeUnitStr = getAttribute(n, "time-unit");
                        ExpiryPolicyType expiryPolicyType = ExpiryPolicyType.valueOf(upperCaseInternal(expiryPolicyTypeStr));
                        if (expiryPolicyType != ExpiryPolicyType.ETERNAL
                                && (isNullOrEmpty(durationAmountStr)
                                || isNullOrEmpty(timeUnitStr))) {
                            throw new InvalidConfigurationException("Both of the \"duration-amount\" or \"time-unit\" attributes"
                                    + " are required for expiry policy factory configuration"
                                    + " (except \"ETERNAL\" expiry policy type)");
                        }
                        DurationConfig durationConfig = null;
                        if (expiryPolicyType != ExpiryPolicyType.ETERNAL) {
                            long durationAmount = Long.parseLong(durationAmountStr);
                            TimeUnit timeUnit = TimeUnit.valueOf(upperCaseInternal(timeUnitStr));
                            durationConfig = new DurationConfig(durationAmount, timeUnit);
                        }
                        timedExpiryPolicyFactoryConfig = new TimedExpiryPolicyFactoryConfig(expiryPolicyType, durationConfig);
                    }
                }
                if (timedExpiryPolicyFactoryConfig == null) {
                    throw new InvalidConfigurationException(
                            "One of the \"class-name\" or \"timed-expire-policy-factory\" configuration"
                                    + " is needed for expiry policy factory configuration");
                } else {
                    return new ExpiryPolicyFactoryConfig(timedExpiryPolicyFactoryConfig);
                }
            }
        }

        public void handleMapStoreConfig(Node node, BeanDefinitionBuilder mapConfigBuilder) {
            BeanDefinitionBuilder mapStoreConfigBuilder = createBeanBuilder(MapStoreConfig.class);
            AbstractBeanDefinition beanDefinition = mapStoreConfigBuilder.getBeanDefinition();
            for (Node child : childElements(node)) {
                if ("properties".equals(cleanNodeName(child))) {
                    handleProperties(child, mapStoreConfigBuilder);
                    break;
                }
            }
            String implAttrName = "implementation";
            String factoryImplAttrName = "factory-implementation";
            String initialModeAttrName = "initial-mode";
            fillAttributeValues(node, mapStoreConfigBuilder, implAttrName, factoryImplAttrName, "initialMode");
            NamedNodeMap attributes = node.getAttributes();
            Node implRef = attributes.getNamedItem(implAttrName);
            Node factoryImplRef = attributes.getNamedItem(factoryImplAttrName);
            Node initialMode = attributes.getNamedItem(initialModeAttrName);
            if (factoryImplRef != null) {
                mapStoreConfigBuilder
                        .addPropertyReference(xmlToJavaName(factoryImplAttrName), getTextContent(factoryImplRef));
            }
            if (implRef != null) {
                mapStoreConfigBuilder.addPropertyReference(xmlToJavaName(implAttrName), getTextContent(implRef));
            }
            if (initialMode != null) {
                InitialLoadMode mode = InitialLoadMode.valueOf(upperCaseInternal(getTextContent(initialMode)));
                mapStoreConfigBuilder.addPropertyValue("initialLoadMode", mode);
            }
            mapConfigBuilder.addPropertyValue("mapStoreConfig", beanDefinition);
        }

        public void handleMultiMap(Node node) {
            BeanDefinitionBuilder multiMapConfigBuilder = createBeanBuilder(MultiMapConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, multiMapConfigBuilder);
            for (Node childNode : childElements(node)) {
                String nodeName = cleanNodeName(childNode);
                if ("entry-listeners".equals(nodeName)) {
                    ManagedList listeners = parseListeners(childNode, EntryListenerConfig.class);
                    multiMapConfigBuilder.addPropertyValue("entryListenerConfigs", listeners);
                } else if ("split-brain-protection-ref".equals(nodeName)) {
                    multiMapConfigBuilder.addPropertyValue("splitBrainProtectionName", getTextContent(childNode));
                } else if ("merge-policy".equals(nodeName)) {
                    handleMergePolicyConfig(childNode, multiMapConfigBuilder);
                }
            }
            multiMapManagedMap.put(name, multiMapConfigBuilder.getBeanDefinition());
        }

        public void handleTopic(Node node) {
            BeanDefinitionBuilder topicConfigBuilder = createBeanBuilder(TopicConfig.class);
            Node attName = node.getAttributes().getNamedItem("name");
            String name = getTextContent(attName);
            fillAttributeValues(node, topicConfigBuilder);
            for (Node childNode : childElements(node)) {
                if ("message-listeners".equals(cleanNodeName(childNode))) {
                    ManagedList listeners = parseListeners(childNode, ListenerConfig.class);
                    topicConfigBuilder.addPropertyValue("messageListenerConfigs", listeners);
                } else if ("statistics-enabled".equals(cleanNodeName(childNode))) {
                    String statisticsEnabled = getTextContent(childNode);
                    topicConfigBuilder.addPropertyValue("statisticsEnabled", statisticsEnabled);
                } else if ("global-ordering-enabled".equals(cleanNodeName(childNode))) {
                    String globalOrderingEnabled = getTextContent(childNode);
                    topicConfigBuilder.addPropertyValue("globalOrderingEnabled", globalOrderingEnabled);
                } else if ("multi-threading-enabled".equals(cleanNodeName(childNode))) {
                    String multiThreadingEnabled = getTextContent(childNode);
                    topicConfigBuilder.addPropertyValue("multiThreadingEnabled", multiThreadingEnabled);
                }
            }
            topicManagedMap.put(name, topicConfigBuilder.getBeanDefinition());
        }

        private void handleSecurity(Node node) {
            BeanDefinitionBuilder securityConfigBuilder = createBeanBuilder(SecurityConfig.class);
            AbstractBeanDefinition beanDefinition = securityConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, securityConfigBuilder);
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("realms".equals(nodeName)) {
                    handleRealms(child, securityConfigBuilder);
                } else if ("member-authentication".equals(nodeName)) {
                    securityConfigBuilder.addPropertyValue("memberRealm", getAttribute(child, "realm"));
                } else if ("client-authentication".equals(nodeName)) {
                    securityConfigBuilder.addPropertyValue("clientRealm", getAttribute(child, "realm"));
                } else if ("client-permission-policy".equals(nodeName)) {
                    handlePermissionPolicy(child, securityConfigBuilder);
                } else if ("client-permissions".equals(nodeName)) {
                    handleSecurityPermissions(child, securityConfigBuilder);
                } else if ("security-interceptors".equals(nodeName)) {
                    handleSecurityInterceptors(child, securityConfigBuilder);
                } else if ("client-block-unmapped-actions".equals(nodeName)) {
                    securityConfigBuilder.addPropertyValue("clientBlockUnmappedActions", getBooleanValue(getTextContent(child)));
                }
            }
            configBuilder.addPropertyValue("securityConfig", beanDefinition);
        }

        private void handleRealms(Node node, BeanDefinitionBuilder securityConfigBuilder) {
            ManagedMap<String, BeanDefinition> realms = new ManagedMap<>();
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("realm".equals(nodeName)) {
                    realms.put(getAttribute(child, "name"), handleRealm(child, securityConfigBuilder));
                }
            }
            securityConfigBuilder.addPropertyValue("realmConfigs", realms);
        }

        private AbstractBeanDefinition handleRealm(Node node, BeanDefinitionBuilder securityConfigBuilder) {
            BeanDefinitionBuilder realmConfigBuilder = createBeanBuilder(RealmConfig.class);
            AbstractBeanDefinition beanDefinition = realmConfigBuilder.getBeanDefinition();
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("identity".equals(nodeName)) {
                    handleIdentity(child, realmConfigBuilder);
                } else if ("authentication".equals(nodeName)) {
                    handleAuthentication(child, realmConfigBuilder);
                }
            }
            return beanDefinition;
        }

        private void handleAuthentication(Node node, BeanDefinitionBuilder realmConfigBuilder) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("jaas".equals(nodeName)) {
                    handleLoginModules(child, realmConfigBuilder);
                } else if ("tls".equals(nodeName)) {
                    createAndFillBeanBuilder(child, TlsAuthenticationConfig.class, "TlsAuthenticationConfig",
                            realmConfigBuilder);
                } else if ("ldap".equals(nodeName)) {
                    handleLdapAuthenticationConfig(realmConfigBuilder, child);
                }
            }
        }

        private BeanDefinitionBuilder handleLdapAuthenticationConfig(BeanDefinitionBuilder realmConfigBuilder, Node node) {
            BeanDefinitionBuilder builder = createAndFillBeanBuilder(node, LdapAuthenticationConfig.class,
                    "LdapAuthenticationConfig", realmConfigBuilder, "roleMappingMode", "userSearchScope", "roleSearchScope");
            for (Node n : childElements(node)) {
                String name = xmlToJavaName(cleanNodeName(n));
                if ("roleMappingMode".equals(name)) {
                    builder.addPropertyValue(name, LdapRoleMappingMode.getRoleMappingMode(getTextContent(n)));
                } else if ("userSearchScope".equals(name) || "roleSearchScope".equals(name)) {
                    builder.addPropertyValue(name, LdapSearchScope.getSearchScope(getTextContent(n)));
                }
            }
            return builder;
        }

        private void handleIdentity(Node node, BeanDefinitionBuilder realmConfigBuilder) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("credentials-factory".equals(nodeName)) {
                    handleCredentialsFactory(child, realmConfigBuilder);
                } else if ("username-password".equals(nodeName)) {
                    BeanDefinitionBuilder configBuilder = createBeanBuilder(UsernamePasswordIdentityConfig.class)
                            .addConstructorArgValue(getAttribute(child, "username"))
                            .addConstructorArgValue(getAttribute(child, "password"));
                    realmConfigBuilder.addPropertyValue("UsernamePasswordIdentityConfig", configBuilder.getBeanDefinition());
                } else if ("token".equals(nodeName)) {
                    BeanDefinitionBuilder configBuilder = createBeanBuilder(TokenIdentityConfig.class)
                            .addConstructorArgValue(TokenEncoding.getTokenEncoding(getAttribute(child, "encoding")))
                            .addConstructorArgValue(getTextContent(child));
                    realmConfigBuilder.addPropertyValue("TokenIdentityConfig", configBuilder.getBeanDefinition());
                } else if ("credentials-ref".equals(nodeName)) {
                    realmConfigBuilder.addPropertyReference("credentials", getTextContent(child));
                }
            }
        }

        private void handleMemberAttributes(Node node) {
            BeanDefinitionBuilder memberAttributeConfigBuilder = createBeanBuilder(MemberAttributeConfig.class);
            AbstractBeanDefinition beanDefinition = memberAttributeConfigBuilder.getBeanDefinition();
            ManagedMap<String, Object> attributes = new ManagedMap<>();
            for (Node n : childElements(node)) {
                String name = cleanNodeName(n);
                if (!"attribute".equals(name)) {
                    continue;
                }
                String attributeName = getTextContent(n.getAttributes().getNamedItem("name")).trim();
                String attributeType = getTextContent(n.getAttributes().getNamedItem("type")).trim();
                String value = getTextContent(n);
                Object oValue;
                if ("string".equals(attributeType)) {
                    oValue = value;
                } else if ("boolean".equals(attributeType)) {
                    oValue = Boolean.parseBoolean(value);
                } else if ("byte".equals(attributeType)) {
                    oValue = Byte.parseByte(value);
                } else if ("double".equals(attributeType)) {
                    oValue = Double.parseDouble(value);
                } else if ("float".equals(attributeType)) {
                    oValue = Float.parseFloat(value);
                } else if ("int".equals(attributeType)) {
                    oValue = Integer.parseInt(value);
                } else if ("long".equals(attributeType)) {
                    oValue = Long.parseLong(value);
                } else if ("short".equals(attributeType)) {
                    oValue = Short.parseShort(value);
                } else {
                    oValue = value;
                }
                attributes.put(attributeName, oValue);
            }
            memberAttributeConfigBuilder.addPropertyValue("attributes", attributes);
            configBuilder.addPropertyValue("memberAttributeConfig", beanDefinition);
        }

        private void handleNativeMemory(Node node) {
            BeanDefinitionBuilder nativeMemoryConfigBuilder = createBeanBuilder(NativeMemoryConfig.class);
            AbstractBeanDefinition beanDefinition = nativeMemoryConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, nativeMemoryConfigBuilder);
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("size".equals(nodeName)) {
                    handleMemorySizeConfig(child, nativeMemoryConfigBuilder);
                }
            }
            configBuilder.addPropertyValue("nativeMemoryConfig", beanDefinition);
        }

        private void handleMemorySizeConfig(Node node, BeanDefinitionBuilder nativeMemoryConfigBuilder) {
            BeanDefinitionBuilder memorySizeConfigBuilder = createBeanBuilder(MemorySize.class);
            NamedNodeMap attributes = node.getAttributes();
            Node value = attributes.getNamedItem("value");
            Node unit = attributes.getNamedItem("unit");
            memorySizeConfigBuilder.addConstructorArgValue(getTextContent(value));
            memorySizeConfigBuilder.addConstructorArgValue(MemoryUnit.valueOf(getTextContent(unit)));
            nativeMemoryConfigBuilder.addPropertyValue("size", memorySizeConfigBuilder.getBeanDefinition());
        }

        private void handleSecurityInterceptors(Node node, BeanDefinitionBuilder securityConfigBuilder) {
            List<BeanDefinition> lms = new ManagedList<>();
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("interceptor".equals(nodeName)) {
                    BeanDefinitionBuilder siConfigBuilder = createBeanBuilder(SecurityInterceptorConfig.class);
                    AbstractBeanDefinition beanDefinition = siConfigBuilder.getBeanDefinition();
                    NamedNodeMap attributes = child.getAttributes();
                    Node classNameNode = attributes.getNamedItem("class-name");
                    String className = classNameNode != null ? getTextContent(classNameNode) : null;
                    Node implNode = attributes.getNamedItem("implementation");
                    String implementation = implNode != null ? getTextContent(implNode) : null;
                    isTrue(className != null || implementation != null,
                            "One of 'class-name' or 'implementation' attributes is required"
                                    + " to create SecurityInterceptorConfig!");
                    siConfigBuilder.addPropertyValue("className", className);
                    if (implementation != null) {
                        siConfigBuilder.addPropertyReference("implementation", implementation);
                    }
                    lms.add(beanDefinition);
                }
            }
            securityConfigBuilder.addPropertyValue("securityInterceptorConfigs", lms);
        }

        private void handleCredentialsFactory(Node node, BeanDefinitionBuilder securityConfigBuilder) {
            BeanDefinitionBuilder credentialsConfigBuilder = createBeanBuilder(CredentialsFactoryConfig.class);
            AbstractBeanDefinition beanDefinition = credentialsConfigBuilder.getBeanDefinition();
            NamedNodeMap attributes = node.getAttributes();
            Node classNameNode = attributes.getNamedItem("class-name");
            String className = classNameNode != null ? getTextContent(classNameNode) : null;
            Node implNode = attributes.getNamedItem("implementation");
            String implementation = implNode != null ? getTextContent(implNode) : null;
            credentialsConfigBuilder.addPropertyValue("className", className);
            if (implementation != null) {
                credentialsConfigBuilder.addPropertyReference("implementation", implementation);
            }
            isTrue(className != null || implementation != null, "One of 'class-name' or 'implementation'"
                    + " attributes is required to create CredentialsFactory!");
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("properties".equals(nodeName)) {
                    handleProperties(child, credentialsConfigBuilder);
                    break;
                }
            }
            securityConfigBuilder.addPropertyValue("credentialsFactoryConfig", beanDefinition);
        }

        private void handleLoginModules(Node node, BeanDefinitionBuilder realmConfigBuilder) {
            BeanDefinitionBuilder jaasConfigBuilder = createBeanBuilder(JaasAuthenticationConfig.class);
            AbstractBeanDefinition beanDefinition = jaasConfigBuilder.getBeanDefinition();
            List<BeanDefinition> lms = new ManagedList<BeanDefinition>();
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("login-module".equals(nodeName)) {
                    handleLoginModule(child, lms);
                }
            }
            jaasConfigBuilder.addPropertyValue("loginModuleConfigs", lms);
            realmConfigBuilder.addPropertyValue("jaasAuthenticationConfig", beanDefinition);
        }

        private void handleLoginModule(Node node, List<BeanDefinition> list) {
            BeanDefinitionBuilder lmConfigBuilder = createBeanBuilder(LoginModuleConfig.class);
            AbstractBeanDefinition beanDefinition = lmConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, lmConfigBuilder, "class-name", "implementation");
            NamedNodeMap attributes = node.getAttributes();
            Node classNameNode = attributes.getNamedItem("class-name");
            String className = classNameNode != null ? getTextContent(classNameNode) : null;
            Node implNode = attributes.getNamedItem("implementation");
            String implementation = implNode != null ? getTextContent(implNode) : null;
            lmConfigBuilder.addPropertyValue("className", className);
            if (implementation != null) {
                lmConfigBuilder.addPropertyReference("implementation", implementation);
            }
            isTrue(className != null || implementation != null, "One of 'class-name' or 'implementation'"
                    + " attributes is required to create LoginModule!");
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("properties".equals(nodeName)) {
                    handleProperties(child, lmConfigBuilder);
                    break;
                }
            }
            list.add(beanDefinition);
        }

        private void handlePermissionPolicy(Node node, BeanDefinitionBuilder securityConfigBuilder) {
            BeanDefinitionBuilder permPolicyConfigBuilder = createBeanBuilder(PermissionPolicyConfig.class);
            AbstractBeanDefinition beanDefinition = permPolicyConfigBuilder.getBeanDefinition();
            NamedNodeMap attributes = node.getAttributes();
            Node classNameNode = attributes.getNamedItem("class-name");
            String className = classNameNode != null ? getTextContent(classNameNode) : null;
            Node implNode = attributes.getNamedItem("implementation");
            String implementation = implNode != null ? getTextContent(implNode) : null;
            permPolicyConfigBuilder.addPropertyValue("className", className);
            if (implementation != null) {
                permPolicyConfigBuilder.addPropertyReference("implementation", implementation);
            }
            isTrue(className != null || implementation != null, "One of 'class-name' or 'implementation'"
                    + " attributes is required to create PermissionPolicy!");
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("properties".equals(nodeName)) {
                    handleProperties(child, permPolicyConfigBuilder);
                    break;
                }
            }
            securityConfigBuilder.addPropertyValue("clientPolicyConfig", beanDefinition);
        }

        private void handleSecurityPermissions(Node node, BeanDefinitionBuilder securityConfigBuilder) {
            Set<BeanDefinition> permissions = new ManagedSet<>();
            NamedNodeMap attributes = node.getAttributes();
            Node onJoinOpAttribute = attributes.getNamedItem("on-join-operation");
            if (onJoinOpAttribute != null) {
                String onJoinOp = getTextContent(onJoinOpAttribute);
                OnJoinPermissionOperationName onJoinPermissionOperation = OnJoinPermissionOperationName
                        .valueOf(upperCaseInternal(onJoinOp));
                securityConfigBuilder.addPropertyValue("onJoinPermissionOperation", onJoinPermissionOperation);
            }
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                PermissionType type = PermissionType.getType(nodeName);
                if (type == null) {
                    continue;
                }

                handleSecurityPermission(child, permissions, type);
            }
            securityConfigBuilder.addPropertyValue("clientPermissionConfigs", permissions);
        }

        private void handleSecurityPermission(Node node, Set<BeanDefinition> permissions, PermissionType type) {
            BeanDefinitionBuilder permissionConfigBuilder = createBeanBuilder(PermissionConfig.class);
            AbstractBeanDefinition beanDefinition = permissionConfigBuilder.getBeanDefinition();
            permissionConfigBuilder.addPropertyValue("type", type);
            NamedNodeMap attributes = node.getAttributes();
            Node nameNode = attributes.getNamedItem("name");
            String name = nameNode != null ? getTextContent(nameNode) : "*";
            permissionConfigBuilder.addPropertyValue("name", name);
            Node principalNode = attributes.getNamedItem("principal");
            String principal = principalNode != null ? getTextContent(principalNode) : "*";
            permissionConfigBuilder.addPropertyValue("principal", principal);
            List<String> endpoints = new ManagedList<>();
            List<String> actions = new ManagedList<>();
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("endpoints".equals(nodeName)) {
                    handleSecurityPermissionEndpoints(child, endpoints);
                } else if ("actions".equals(nodeName)) {
                    handleSecurityPermissionActions(child, actions);
                }
            }
            permissionConfigBuilder.addPropertyValue("endpoints", endpoints);
            permissionConfigBuilder.addPropertyValue("actions", actions);
            permissions.add(beanDefinition);
        }

        private void handleSecurityPermissionEndpoints(Node node, List<String> endpoints) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("endpoint".equals(nodeName)) {
                    endpoints.add(getTextContent(child));
                }
            }
        }

        private void handleSecurityPermissionActions(Node node, List<String> actions) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("action".equals(nodeName)) {
                    actions.add(getTextContent(child));
                }
            }
        }

        private void handleWanReplicationRef(BeanDefinitionBuilder beanDefinitionBuilder, Node childNode) {
            BeanDefinitionBuilder wanReplicationRefBuilder = createBeanBuilder(WanReplicationRef.class);
            AbstractBeanDefinition wanReplicationRefBeanDefinition = wanReplicationRefBuilder.getBeanDefinition();
            fillValues(childNode, wanReplicationRefBuilder);
            for (Node node : childElements(childNode)) {
                String nodeName = cleanNodeName(node);
                if (nodeName.equals("filters")) {
                    List<String> filters = new ManagedList<>();
                    handleFilters(node, filters);
                    wanReplicationRefBuilder.addPropertyValue("filters", filters);
                }
            }
            beanDefinitionBuilder.addPropertyValue("wanReplicationRef", wanReplicationRefBeanDefinition);
        }

        private void handleFilters(Node node, List<String> filters) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("filter-impl".equals(nodeName)) {
                    filters.add(getTextContent(child));
                }
            }
        }

        private void handleRestApi(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            BeanDefinitionBuilder restApiConfigBuilder = createBeanBuilder(RestApiConfig.class);
            AbstractBeanDefinition beanDefinition = restApiConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, restApiConfigBuilder);
            ManagedSet<RestEndpointGroup> groupSet = new ManagedSet<>();
            for (RestEndpointGroup group : RestEndpointGroup.values()) {
                if (group.isEnabledByDefault()) {
                    groupSet.add(group);
                }
            }
            handleRestEndpointGroup(node, restApiConfigBuilder, groupSet);
            networkConfigBuilder.addPropertyValue("restApiConfig", beanDefinition);
        }

        private void handleRestEndpointGroup(Node node, BeanDefinitionBuilder builder,
                                             ManagedSet<RestEndpointGroup> groupSet) {
            for (Node child : childElements(node)) {
                String nodeName = cleanNodeName(child);
                if ("endpoint-group".equals(nodeName)) {
                    NamedNodeMap attributes = child.getAttributes();
                    Node attrEnabled = attributes.getNamedItem("enabled");
                    boolean enabled = attrEnabled != null && getBooleanValue(getTextContent(attrEnabled));
                    String name = getTextContent(attributes.getNamedItem("name"));
                    RestEndpointGroup group;
                    try {
                        group = RestEndpointGroup.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidConfigurationException(
                                "Wrong name attribute value was provided in endpoint-group element: " + name
                                        + "\nAllowed values: " + Arrays.toString(RestEndpointGroup.values()));
                    }
                    if (enabled) {
                        groupSet.add(group);
                    } else {
                        groupSet.remove(group);
                    }
                }
            }
            builder.addPropertyValue("enabledGroups", groupSet);
        }

        private void handleMemcacheProtocol(Node node, BeanDefinitionBuilder networkConfigBuilder) {
            BeanDefinitionBuilder memcacheProtocolConfigBuilder = createBeanBuilder(MemcacheProtocolConfig.class);
            AbstractBeanDefinition beanDefinition = memcacheProtocolConfigBuilder.getBeanDefinition();
            fillAttributeValues(node, memcacheProtocolConfigBuilder);
            networkConfigBuilder.addPropertyValue("memcacheProtocolConfig", beanDefinition);
        }

        // construct the endpoint qualifier corresponding to an
        // endpoint-config or server-socket-endpoint-config node
        private EndpointQualifier createEndpointQualifier(ProtocolType type, Node node) {
            return EndpointQualifier.resolve(type, getAttribute(node, "name"));
        }
    }
}
