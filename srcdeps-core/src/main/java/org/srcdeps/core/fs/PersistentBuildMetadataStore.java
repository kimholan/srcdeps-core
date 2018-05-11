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
package org.srcdeps.core.fs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srcdeps.core.BuildMetadataStore;
import org.srcdeps.core.BuildRequestId;
import org.srcdeps.core.Gavtc;

/**
 * A {@link BuildMetadataStore} that stores its entries to the filesystem.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 * @since 3.2.1
 */
@Named
@Singleton
public class PersistentBuildMetadataStore implements BuildMetadataStore {
    private static final String COMMIT_ID = "commitId";

    private static final Logger log = LoggerFactory.getLogger(PersistentBuildMetadataStore.class);

    private static void store(final Path p, String content) {
        try {
            Files.createDirectories(p.getParent());
            Files.write(p, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not write to path [%s]", p), e);
        }
    }

    private final Path rootDirectory;

    public PersistentBuildMetadataStore(Path rootDirectory) {
        super();
        this.rootDirectory = rootDirectory;
        try {
            Files.createDirectories(rootDirectory);
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Could not create %s.rootDirectory [%s]", this.getClass().getName(), rootDirectory));
        }
    }

    Path createBuildRequestIdPath(String hash) {
        int i = 0;
        final Path p = rootDirectory.resolve(hash.substring(i++, i)).resolve(hash.substring(i++, i))
                .resolve(hash.substring(i++, i)).resolve(hash.substring(i++, i)).resolve(hash.substring(i));
        return p;
    }

    /** {@inheritDoc} */
    @Override
    public String retrieveCommitId(BuildRequestId buildRequestId) {
        final Path p = createBuildRequestIdPath(buildRequestId.getHash()).resolve(COMMIT_ID);
        if (Files.exists(p)) {
            try {
                String result = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                log.debug("{} path {} points at commitId {}", BuildRequestId.class.getSimpleName(), p, result);
                return result;
            } catch (IOException e) {
                throw new RuntimeException(String.format("Could not read %s", p), e);
            }
        }
        log.debug("{} path {} does not exist", BuildRequestId.class.getSimpleName(), p);
        return null;
    }

    @Override
    public String retrieveSha1(BuildRequestId buildRequestId, Gavtc gavtc) {
        final String gavtcString = gavtc.toString();
        final Path p = createBuildRequestIdPath(buildRequestId.getHash()).resolve(gavtcString);
        if (Files.exists(p)) {
            try {
                final String result = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                log.debug("{} path {} gavtc {} points at sha1 {}", BuildRequestId.class.getSimpleName(), p, gavtcString,
                        result);
                return result;
            } catch (IOException e) {
                throw new RuntimeException(String.format("Could not read %s", p), e);
            }
        }
        log.debug("{} path {} does not exist", BuildRequestId.class.getSimpleName(), p);
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void storeCommitId(BuildRequestId buildRequestId, String commitId) {
        final Path p = createBuildRequestIdPath(buildRequestId.getHash()).resolve(COMMIT_ID);
        log.debug("{} path {} will point at commitId {}; {}", BuildRequestId.class.getSimpleName(), p, commitId,
                buildRequestId);
        store(p, commitId);
    }

    @Override
    public void storeSha1(BuildRequestId buildRequestId, Gavtc gavtc, String sha1) {
        final String gavtcString = gavtc.toString();
        final Path p = createBuildRequestIdPath(buildRequestId.getHash()).resolve(gavtcString);
        store(p, sha1);
    }

}
