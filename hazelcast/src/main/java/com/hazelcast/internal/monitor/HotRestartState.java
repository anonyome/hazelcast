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

package com.hazelcast.internal.monitor;

import com.hazelcast.hotrestart.BackupTaskStatus;
import com.hazelcast.json.JsonSerializable;

/**
 * Hot Restart statistics to be used by {@link MemberState} implementations.
 */
public interface HotRestartState extends JsonSerializable {

    /**
     * Returns the local hot restart backup task status (not the cluster backup status).
     */
    BackupTaskStatus getBackupTaskStatus();

    boolean isHotBackupEnabled();

    String getBackupDirectory();
}
