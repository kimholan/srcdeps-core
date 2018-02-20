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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.srcdeps.core.BuildRequest.Verbosity;

public class BuildRequestIdTest {

    @Test
    public void hashCodeEquals() {

        final Map<String, String> env1 = new HashMap<>();
        env1.put("k1", "v1");
        env1.put("k2", "v2");

        final Set<String> props = new HashSet<>();
        props.add("prop1");
        props.add("prop2");

        BuildRequestId id1 = new BuildRequestId(true, true, Arrays.<String>asList("arg1", "arg2"), env1, props,
                GavSet.builder().include("org.mygroup").build(), Arrays.asList("url1", "url2"), true,
                SrcVersion.parse("1.2.3-SRC-revision-deadbeef"), 50000, Verbosity.error);
        BuildRequestId id2 = new BuildRequestId(true, true, new ArrayList<>(Arrays.<String>asList("arg1", "arg2")),
                new LinkedHashMap<String, String>(env1), new LinkedHashSet<String>(props),
                GavSet.builder().include("org.mygroup").build(), new ArrayList<>(Arrays.<String>asList("url1", "url2")),
                true, SrcVersion.parse("1.2.3-SRC-revision-deadbeef"), 50000, Verbosity.error);

        Assert.assertEquals(id1, id2);
        Assert.assertEquals(id1.hashCode(), id2.hashCode());

        Assert.assertEquals(id1.getHash(), id2.getHash());
        Assert.assertEquals("c16cc8b4b89b965ce7dd32becc6b58d6bc1db14c", id1.getHash());
    }

}
