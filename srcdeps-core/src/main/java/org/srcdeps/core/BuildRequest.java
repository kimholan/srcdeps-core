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
package org.srcdeps.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.srcdeps.core.shell.IoRedirects;
import org.srcdeps.core.util.SrcdepsCoreUtils;

/**
 * A description of what and how should be built.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class BuildRequest {

    /**
     *
     * A builder for {@link BuildRequest}s.
     *
     */
    /**
     * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
     *
     */
    public static class BuildRequestBuilder {

        private boolean addDefaultBuildArguments = true;
        private List<String> buildArguments = new ArrayList<>();
        private Map<String, String> buildEnvironment = new LinkedHashMap<>();
        private Set<String> forwardProperties = new LinkedHashSet<>();
        private IoRedirects ioRedirects = IoRedirects.inheritAll();
        private Path projectRootDirectory;
        private List<String> scmUrls = new ArrayList<>();
        private boolean skipTests = true;
        private SrcVersion srcVersion;
        private long timeoutMs = DEFAULT_TIMEOUT_MS;
        private Verbosity verbosity = Verbosity.info;
        private String versionsMavenPluginVersion;

        /**
         * @param addDefaultBuildArguments
         *            see {@link BuildRequest#isAddDefaultBuildArguments()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder addDefaultBuildArguments(boolean addDefaultBuildArguments) {
            this.addDefaultBuildArguments = addDefaultBuildArguments;
            return this;
        }

        /**
         * @return a new {@link BuildRequest} based on the values stored in fields of this {@link BuildRequestBuilder}
         */
        public BuildRequest build() {
            return new BuildRequest(projectRootDirectory, srcVersion, Collections.unmodifiableList(scmUrls),
                    Collections.unmodifiableList(buildArguments), skipTests, addDefaultBuildArguments,
                    Collections.unmodifiableSet(forwardProperties), Collections.unmodifiableMap(buildEnvironment),
                    verbosity, ioRedirects, timeoutMs, versionsMavenPluginVersion);
        }

        /**
         * @param argument
         *            the single build argument to add to {@link #buildArguments}; see
         *            {@link BuildRequest#getBuildArguments()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder buildArgument(String argument) {
            this.buildArguments.add(argument);
            return this;
        }

        /**
         * @param arguments
         *            the arguments to add to {@link #buildArguments}; see {@link BuildRequest#getBuildArguments()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder buildArguments(List<String> buildArguments) {
            this.buildArguments.addAll(buildArguments);
            return this;
        }

        /**
         * @param arguments
         *            the arguments to add to {@link #buildArguments}; see {@link BuildRequest#getBuildArguments()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder buildArguments(String... arguments) {
            for (String argument : arguments) {
                this.buildArguments.add(argument);
            }
            return this;
        }

        /**
         * @param buildEnvironment
         *            see {@link BuildRequest#getBuildEnvironment()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder buildEnvironment(Map<String, String> buildEnvironment) {
            this.buildEnvironment.putAll(buildEnvironment);
            return this;
        }

        /**
         * Add an environment variable of the given {@code name} and {@code value} to {@link #buildEnvironment}.
         *
         * @see BuildRequest#getBuildEnvironment()
         * @param name
         *            the name of the environment variable to add to {@link #buildEnvironment}
         * @param value
         *            the value of the environment variable to add to {@link #buildEnvironment}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder buildEnvironmentVariable(String name, String value) {
            this.buildEnvironment.put(name, value);
            return this;
        }

        /**
         * @see BuildRequest#getForwardProperties()
         * @param values
         *            the property names or patterns to forward
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder forwardProperties(Collection<String> values) {
            forwardProperties.addAll(values);
            return this;
        }

        /**
         * @see BuildRequest#getForwardProperties()
         * @param value
         *            the property name or pattern to forward
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder forwardProperty(String value) {
            forwardProperties.add(value);
            return this;
        }

        /**
         * @param ioRedirects
         *            see {@link BuildRequest#getIoRedirects()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder ioRedirects(IoRedirects ioRedirects) {
            this.ioRedirects = ioRedirects;
            return this;
        }

        /**
         * @param projectRootDirectory
         *            see {@link BuildRequest#getProjectRootDirectory()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder projectRootDirectory(Path projectRootDirectory) {
            this.projectRootDirectory = projectRootDirectory;
            return this;
        }

        /**
         * @param scmUrl
         *            a SCM url to add to {@link #scmUrls}. See {@link BuildRequest#getScmUrls()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder scmUrl(String scmUrl) {
            this.scmUrls.add(scmUrl);
            return this;
        }

        /**
         * @param scmUrls
         *            SCM urls to add to {@link #scmUrls}. See {@link BuildRequest#getScmUrls()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder scmUrls(Collection<String> scmUrls) {
            this.scmUrls.addAll(scmUrls);
            return this;
        }

        /**
         * @param skipTests see {@link BuildRequest#isSkipTests()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder skipTests(boolean skipTests) {
            this.skipTests = skipTests;
            return this;
        }

        /**
         * @param srcVersion
         *            see {@link BuildRequest#getSrcVersion()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder srcVersion(SrcVersion srcVersion) {
            this.srcVersion = srcVersion;
            return this;
        }

        /**
         * @param timeoutMs
         *            see {@link BuildRequest#getTimeoutMs()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        /**
         * @param verbosity
         *            see {@link BuildRequest#getVerbosity()}
         * @return this {@link BuildRequestBuilder}
         */
        public BuildRequestBuilder verbosity(Verbosity verbosity) {
            this.verbosity = verbosity;
            return this;
        }

        public BuildRequestBuilder versionsMavenPluginVersion(String versionsMavenPluginVersion) {
            this.versionsMavenPluginVersion = versionsMavenPluginVersion;
            return this;
        }
    };

    /**
     * The verbosity level the appropriate {@link Builder} should use when executing a {@link BuildRequest}. The
     * interpretation of the individual levels is up to the given {@link Builder} implementation. Some {@link Builder}s
     * may map the levels listed here to a distinct set of levels they support internally.
     */
    public enum Verbosity {
        debug, error, info, trace, warn;

        public static Verbosity fastValueOf(String level) {
            SrcdepsCoreUtils.assertArgNotNull(level, "Verbosity name");
            switch (level.toLowerCase(Locale.ROOT)) {
            case "trace":
                return trace;
            case "debug":
                return debug;
            case "info":
                return info;
            case "warn":
                return warn;
            case "error":
                return error;
            default:
                throw new IllegalStateException("No such " + Verbosity.class.getName() + " with name [" + level + "]");
            }
        }

    }

    /** {@link Long#MAX_VALUE} */
    private static final long DEFAULT_TIMEOUT_MS = Long.MAX_VALUE;

    /**
     * @return a new {@link BuildRequestBuilder}
     */
    public static BuildRequestBuilder builder() {
        return new BuildRequestBuilder();
    }

    /**
     * @return the default timeout in milliseconds for source dependency builds. The value is {@link Long#MAX_VALUE}.
     */
    public static long getDefaultTimeoutMs() {
        return DEFAULT_TIMEOUT_MS;
    }

    private final boolean addDefaultBuildArguments;
    private final List<String> buildArguments;
    private final Map<String, String> buildEnvironment;
    private final Set<String> forwardProperties;
    private final IoRedirects ioRedirects;
    private final Path projectRootDirectory;
    private final List<String> scmUrls;
    private final boolean skipTests;
    private final SrcVersion srcVersion;
    private final long timeoutMs;
    private final Verbosity verbosity;
    private final String versionsMavenPluginVersion;

    private BuildRequest(Path projectRootDirectory, SrcVersion srcVersion, List<String> scmUrls,
            List<String> buildArguments, boolean skipTests, boolean addDefaultBuildArguments,
            Set<String> forwardProperties, Map<String, String> buildEnvironment, Verbosity verbosity,
            IoRedirects ioRedirects, long timeoutMs, String versionsMavenPluginVersion) {
        super();

        SrcdepsCoreUtils.assertArgNotNull(projectRootDirectory, "projectRootDirectory");
        SrcdepsCoreUtils.assertArgNotNull(srcVersion, "srcVersion");
        SrcdepsCoreUtils.assertArgNotNull(scmUrls, "scmUrls");
        SrcdepsCoreUtils.assertCollectionNotEmpty(scmUrls, "scmUrls");
        SrcdepsCoreUtils.assertArgNotNull(buildArguments, "buildArguments");
        SrcdepsCoreUtils.assertArgNotNull(forwardProperties, "forwardProperties");
        SrcdepsCoreUtils.assertArgNotNull(buildEnvironment, "buildEnvironment");
        SrcdepsCoreUtils.assertArgNotNull(ioRedirects, "ioRedirects");
        SrcdepsCoreUtils.assertArgNotNull(versionsMavenPluginVersion, "versionsMavenPluginVersion");

        this.projectRootDirectory = projectRootDirectory;
        this.srcVersion = srcVersion;
        this.scmUrls = scmUrls;
        this.buildArguments = buildArguments;
        this.skipTests = skipTests;
        this.buildEnvironment = buildEnvironment;
        this.verbosity = verbosity;
        this.timeoutMs = timeoutMs;
        this.addDefaultBuildArguments = addDefaultBuildArguments;
        this.forwardProperties = forwardProperties;
        this.ioRedirects = ioRedirects;
        this.versionsMavenPluginVersion = versionsMavenPluginVersion;
    }

    /**
     * @return a {@link List} of arguments that should be used by {@link Builder#build(BuildRequest)}. Cannot be
     *         {@code null}. The {@link Builder} will combine this list with its default list of arguments if
     *         {@link #isAddDefaultBuildArguments()} returns {@code true}.
     */
    public List<String> getBuildArguments() {
        return buildArguments;
    }

    /**
     * @return a {@link Map} of environment variables that should be used by {@link Builder#build(BuildRequest)}. Cannot
     *         be {@code null}. Note that these are just overlay variables - if the {@link Builder} spawns a new
     *         process, the environment is copied from the present process and the variables provided by the present
     *         method are overwritten or added.
     */
    public Map<String, String> getBuildEnvironment() {
        return buildEnvironment;
    }

    /**
     * Used rarely, mostly for debugging. A list of property names that the top level builder A should pass as java
     * system properties to every dependency builder B using {@code -DmyProperty=myValue} style command line arguments.
     * Further, in case a child builder B spawns its own new child builder C, B must pass all these properties to C in
     * the very same manner as A did to B.
     *
     * A property name may end with asterisk {@code *} to denote that all properties starting with the part before the
     * asterisk should be forwared. E.g. {@code my.prop.*} would forward both {@code my.prop.foo} and
     * {@code my.prop.bar}.
     *
     * @return a {@link Set} of properties to forward
     */
    public Set<String> getForwardProperties() {
        return forwardProperties;
    }

    /**
     * @return the {@link IoRedirects} to use when the {@link Builder} spawns new {@link Process}es
     */
    public IoRedirects getIoRedirects() {
        return ioRedirects;
    }

    /**
     * @return the root directory of the project's source tree that should be built
     *
     */
    public Path getProjectRootDirectory() {
        return projectRootDirectory;
    }

    /**
     * @return a {@link List} of URLs that should be tried one after another to checkout the version determined by
     *         {@link #getSrcVersion()}. The URLs will be tried by the {@link BuildService} in the given order until the
     *         checkout is successful. See {@link Scm} for the information about the format of the URLs.
     */
    public List<String> getScmUrls() {
        return scmUrls;
    }

    /**
     * @return the {@link SrcVersion} to checkout and build
     */
    public SrcVersion getSrcVersion() {
        return srcVersion;
    }

    /**
     * @return the timeout in milliseconds for the {@link Builder#setVersions(BuildRequest)} and
     *         {@link Builder#build(BuildRequest)} operations.
     */
    public long getTimeoutMs() {
        return timeoutMs;
    }

    /**
     * @return the verbosity level to use when configuring the {@link Builder}
     */
    public Verbosity getVerbosity() {
        return verbosity;
    }

    public String getVersionsMavenPluginVersion() {
        return versionsMavenPluginVersion;
    }

    /**
     * @return {@code true} if the given {@link Builder}'s default arguments should be combined with the arguments
     *         returned by {@link #getBuildArguments()}; {@code false} otherwise
     */
    public boolean isAddDefaultBuildArguments() {
        return addDefaultBuildArguments;
    }

    /**
     * @return {@code true} if no tests should be run when building the dependency. For dependencies built with Maven,
     *         this accounts to adding {@code -DskipTests} to the {@code mvn} arguments.
     */
    public boolean isSkipTests() {
        return skipTests;
    }

    @Override
    public String toString() {
        return "BuildRequest [addDefaultBuildArguments=" + addDefaultBuildArguments + ", buildArguments="
                + buildArguments + ", buildEnvironment=" + buildEnvironment + ", forwardProperties=" + forwardProperties
                + ", ioRedirects=" + ioRedirects + ", projectRootDirectory=" + projectRootDirectory + ", scmUrls="
                + scmUrls + ", skipTests=" + skipTests + ", srcVersion=" + srcVersion + ", timeoutMs=" + timeoutMs
                + ", verbosity=" + verbosity + ", versionsMavenPluginVersion=" + versionsMavenPluginVersion + "]";
    }
}
