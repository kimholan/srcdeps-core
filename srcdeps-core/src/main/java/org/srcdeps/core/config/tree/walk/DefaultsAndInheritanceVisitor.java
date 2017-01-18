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
package org.srcdeps.core.config.tree.walk;

import java.util.List;

import org.srcdeps.core.config.tree.ContainerNode;
import org.srcdeps.core.config.tree.ListNode;
import org.srcdeps.core.config.tree.Node;
import org.srcdeps.core.config.tree.ScalarNode;
import org.srcdeps.core.config.tree.Visitor;

/**
 * A {@link Visitor} that calls {@link DefaultsAndInheritanceReceiver#applyDefaultsAndInheritance(List)} for each node
 * of a tree.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class DefaultsAndInheritanceVisitor extends AbstractVisitor {

    @Override
    public boolean containerBegin(ContainerNode<? extends Node> node) {
        super.containerBegin(node);
        node.applyDefaultsAndInheritance(stack);
        return true;
    }

    @Override
    public boolean listBegin(ListNode<? extends Node> node) {
        super.listBegin(node);
        node.applyDefaultsAndInheritance(stack);
        return true;
    }

    @Override
    public void scalar(ScalarNode<Object> node) {
        node.applyDefaultsAndInheritance(stack);
    }

}
