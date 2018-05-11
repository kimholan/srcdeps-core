/**
 * Copyright 2015-2018 Maven Source Dependencies
 * Plugin contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.srcdeps.core;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class BuildMetadata {

    public static class Builder {

        private BuildRequestId buildRequestId;

        private String commitId;
        private Map<Gavtc, String> gavtcMetadatas = new TreeMap<Gavtc, String>();

        public BuildMetadata build() {
            final Map<Gavtc, String> useGavtcMetadatas = Collections.unmodifiableMap(gavtcMetadatas);
            this.gavtcMetadatas = null;
            return new BuildMetadata(buildRequestId, commitId, useGavtcMetadatas);
        }

        public Builder buildRequestId(BuildRequestId buildRequestId) {
            this.buildRequestId = buildRequestId;
            return this;
        }

        public Builder commitId(String commitId) {
            this.commitId = commitId;
            return this;
        }

        public Builder gavtcMetadata(Gavtc gavtc, String sha1) {
            this.gavtcMetadatas.put(gavtc, sha1);
            return this;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private final BuildRequestId buildRequestId;
    private final String commitId;
    private final Map<Gavtc, String> gavtcMetadatas;

    BuildMetadata(BuildRequestId buildRequestId, String commitId, Map<Gavtc, String> gavtcMetadatas) {
        super();
        this.buildRequestId = buildRequestId;
        this.commitId = commitId;
        this.gavtcMetadatas = gavtcMetadatas;
    }

    public BuildRequestId getBuildRequestId() {
        return buildRequestId;
    }

    @Override
    public String toString() {
        return "BuildMetadata [buildRequestId=" + buildRequestId + ", commitId=" + commitId + ", gavtcMetadatas="
                + gavtcMetadatas + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((buildRequestId == null) ? 0 : buildRequestId.hashCode());
        result = prime * result + ((commitId == null) ? 0 : commitId.hashCode());
        result = prime * result + ((gavtcMetadatas == null) ? 0 : gavtcMetadatas.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuildMetadata other = (BuildMetadata) obj;
        if (buildRequestId == null) {
            if (other.buildRequestId != null)
                return false;
        } else if (!buildRequestId.equals(other.buildRequestId))
            return false;
        if (commitId == null) {
            if (other.commitId != null)
                return false;
        } else if (!commitId.equals(other.commitId))
            return false;
        if (gavtcMetadatas == null) {
            if (other.gavtcMetadatas != null)
                return false;
        } else if (!gavtcMetadatas.equals(other.gavtcMetadatas))
            return false;
        return true;
    }

    public String getCommitId() {
        return commitId;
    }

    public Map<Gavtc, String> getGavtcMetadatas() {
        return gavtcMetadatas;
    }

}
