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
package org.srcdeps.config.yaml.writer;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.srcdeps.core.config.Configuration;
import org.srcdeps.core.config.tree.ContainerNode;
import org.srcdeps.core.config.tree.ListNode;
import org.srcdeps.core.config.tree.Node;
import org.srcdeps.core.config.tree.ScalarNode;
import org.srcdeps.core.config.tree.Visitor;
import org.srcdeps.core.config.tree.walk.AbstractVisitor;

/**
 * A {@link Visitor} that serializes the given tree to a {@link Writer}.
 *
 * @author <a href="https://github.com/ppalaga">Peter Palaga</a>
 */
public class YamlWriterVisitor extends AbstractVisitor implements Closeable {

    private final YamlWriterConfiguration configuration;

    private int depth = 0;

    private final char[] indentString;
    private final Writer out;

    public YamlWriterVisitor(Writer out, YamlWriterConfiguration configuration) {
        super();
        this.out = out;
        this.configuration = configuration;

        final int indentLength = configuration.getIndentLength();
        final char indentChar = configuration.getIndentChar();
        this.indentString = new char[indentLength];
        for (int i = 0; i < indentLength; i++) {
            indentString[i] = indentChar;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        out.close();
    }

    /** {@inheritDoc} */
    @Override
    public boolean containerBegin(ContainerNode<? extends Node> node) {
        boolean hasListParent = hasListAncestor(0);
        super.containerBegin(node);
        try {
            if (!node.isInDefaultState(stack)) {
                writeComment(node.getCommentBefore());
                if (!(node instanceof Configuration.Builder)) {
                    indent();
                    if (hasListParent) {
                        throw new IllegalStateException("Contributions welcome");
                    } else {
                        out.write(node.getName());
                        out.write(':');
                        out.write(configuration.getNewLine());
                    }
                    depth++;
                }
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeComment(List<String> commentLines) throws IOException {
        if (!commentLines.isEmpty() && depth != 0) {
            out.write(configuration.getNewLine());
        }
        for (String line : commentLines) {
            indent();
            out.write('#');
            if (line != null && !line.isEmpty()) {
                out.write(' ');
                out.write(line);
            }
            out.write(configuration.getNewLine());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void containerEnd() {
        Node node = stack.peek();
        if (!(node instanceof Configuration.Builder) && !node.isInDefaultState(stack)) {
            depth--;
        }
        super.containerEnd();
    }

    private void indent() throws IOException {
        for (int i = 0; i < depth; i++) {
            out.write(indentString, 0, indentString.length);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean listBegin(ListNode<? extends Node> node) {
        super.listBegin(node);
        if (!node.isInDefaultState(stack)) {
            try {
                writeComment(node.getCommentBefore());
                indent();
                out.write(node.getName());
                out.write(':');
                out.write(configuration.getNewLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void scalar(ScalarNode<Object> node) {
        try {
            if ("configModelVersion"
                    .equals(node.getName()) /* We want configModelVersion always to be present in the output */
                    || !node.isInDefaultState(stack)) {
                writeComment(node.getCommentBefore());
                if (hasListAncestor(0)) {
                    indent();
                    out.write("- ");
                } else {
                    indent();
                    out.write(node.getName());
                    out.write(':');
                    out.write(' ');
                }
                out.write(String.valueOf(node.getValue()));
                out.write(configuration.getNewLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
