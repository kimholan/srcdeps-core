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
package org.srcdeps.core.impl.builder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.srcdeps.core.BuildException;
import org.srcdeps.core.BuildRequest;
import org.srcdeps.core.BuildRequest.Verbosity;
import org.srcdeps.core.config.Configuration;
import org.srcdeps.core.shell.Shell;
import org.srcdeps.core.shell.ShellCommand;

/**
 * A base for {@link MvnBuilder} and {@link MvnwBuilder}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public abstract class AbstractMvnBuilder extends ShellBuilder {
    public static final List<String> mvnDefaultArgs = Collections.unmodifiableList(Arrays.asList("clean", "install"));
    public static final List<String> mvnwFileNames = Collections.unmodifiableList(Arrays.asList("mvnw", "mvnw.cmd"));

    public static final List<String> pomFileNames = Collections.unmodifiableList(
            Arrays.asList("pom.xml", "pom.atom", "pom.clj", "pom.groovy", "pom.rb", "pom.scala", "pom.yml"));
    public static final List<String> skipTestsArgs = Collections.singletonList("-DskipTests");

    public static boolean hasMvnwFile(Path directory) {
        for (String fileName : mvnwFileNames) {
            if (directory.resolve(fileName).toFile().exists()) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPomFile(Path directory) {
        for (String fileName : pomFileNames) {
            if (directory.resolve(fileName).toFile().exists()) {
                return true;
            }
        }
        return false;
    }

    public AbstractMvnBuilder(String executable) {
        super(executable);
    }

    @Override
    protected List<String> getDefaultBuildArguments() {
        String settingsPath = System.getProperty(Configuration.SRCDEPS_MVN_SETTINGS_PROP);
        if (settingsPath != null) {
            List<String> result = new ArrayList<>(mvnDefaultArgs.size() + 2);
            result.addAll(mvnDefaultArgs);
            result.add("-s");
            result.add(settingsPath);
            return Collections.unmodifiableList(result);
        } else {
            return mvnDefaultArgs;
        }
    }

    @Override
    protected List<String> getSkipTestsArguments(boolean skipTests) {
        return skipTests ? skipTestsArgs : Collections.<String> emptyList();
    }

    @Override
    protected List<String> getVerbosityArguments(Verbosity verbosity) {
        switch (verbosity) {
        case trace:
        case debug:
            return Collections.singletonList("--debug");
        case info:
            return Collections.emptyList();
        case warn:
        case error:
            return Collections.singletonList("--quiet");
        default:
            throw new IllegalStateException("Unexpected " + Verbosity.class.getName() + " value [" + verbosity + "]");
        }
    }

    @Override
    public void setVersions(BuildRequest request) throws BuildException {
        final List<String> args = new ArrayList<>();
        args.add("org.codehaus.mojo:versions-maven-plugin:"+ request.getVersionsMavenPluginVersion() +":set");
        args.add("-DnewVersion=" + request.getSrcVersion().toString());
        args.add("-DartifactId=*");
        args.add("-DgroupId=*");
        args.add("-DoldVersion=*");
        args.add("-DgenerateBackupPoms=false");
        args.addAll(getVerbosityArguments(request.getVerbosity()));

        ShellCommand cliRequest = ShellCommand.builder() //
                .executable(locateExecutable(request)).arguments(args) //
                .workingDirectory(request.getProjectRootDirectory()) //
                .environment(request.getBuildEnvironment()) //
                .ioRedirects(request.getIoRedirects()) //
                .timeoutMs(request.getTimeoutMs()) //
                .build();
        Shell.execute(cliRequest).assertSuccess();
    }

}
