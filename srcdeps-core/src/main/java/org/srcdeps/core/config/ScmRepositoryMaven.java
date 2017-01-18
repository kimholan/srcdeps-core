/**
 * Copyright 2015-2016 Maven Source Dependencies
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
package org.srcdeps.core.config;

import java.util.Map;
import java.util.Stack;

import org.srcdeps.core.config.tree.Node;
import org.srcdeps.core.config.tree.ScalarNode;
import org.srcdeps.core.config.tree.impl.DefaultContainerNode;
import org.srcdeps.core.config.tree.impl.DefaultScalarNode;

/**
 * Maven specific settings for a {@link ScmRepository} under which this hangs.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class ScmRepositoryMaven {
    public static class Builder extends DefaultContainerNode<Node> {
        ScalarNode<String> versionsMavenPluginVersion = new DefaultScalarNode<String>("versionsMavenPluginVersion",
                null, String.class) {

            @Override
            public void applyDefaultsAndInheritance(Stack<Node> configurationStack) {
                Configuration.Builder configuratioBuilder = (Configuration.Builder) configurationStack.get(0);
                inheritFrom(configuratioBuilder.maven.versionsMavenPluginVersion, configurationStack);
            }

            @Override
            public boolean isInDefaultState(Stack<Node> configurationStack) {
                Configuration.Builder configuratioBuilder = (Configuration.Builder) configurationStack.get(0);
                return isInDefaultState(configuratioBuilder.maven.versionsMavenPluginVersion, configurationStack);
            }

        };

        public Builder() {
            super("maven");
            addChildren(versionsMavenPluginVersion);
        }

        public ScmRepositoryMaven build() {
            return new ScmRepositoryMaven(versionsMavenPluginVersion.getValue());
        }

        public Builder commentBefore(String value) {
            commentBefore.add(value);
            return this;
        }

        @Override
        public Map<String, Node> getChildren() {
            return children;
        }

        public Builder versionsMavenPluginVersion(String versionsMavenPluginVersion) {
            this.versionsMavenPluginVersion.setValue(versionsMavenPluginVersion);
            return this;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    private final String versionsMavenPluginVersion;

    public ScmRepositoryMaven(String versionsMavenPluginVersion) {
        super();
        this.versionsMavenPluginVersion = versionsMavenPluginVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScmRepositoryMaven other = (ScmRepositoryMaven) obj;
        if (versionsMavenPluginVersion == null) {
            if (other.versionsMavenPluginVersion != null)
                return false;
        } else if (!versionsMavenPluginVersion.equals(other.versionsMavenPluginVersion))
            return false;
        return true;
    }

    /**
     * @return the version of {@code org.codehaus.mojo:versions-maven-plugin} to use when setting versions in the given
     *         source repository. When not set explicitly, the default is
     *         {@value ScmRepositoryMaven#DEFAULT_VERSIONS_MAVEN_PLUGIN_VERSION}.
     */
    public String getVersionsMavenPluginVersion() {
        return versionsMavenPluginVersion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((versionsMavenPluginVersion == null) ? 0 : versionsMavenPluginVersion.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ScmRepositoryMaven [versionsMavenPluginVersion=" + versionsMavenPluginVersion + "]";
    }
}
