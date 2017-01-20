package com.agarron.simpleast_core.builder;

import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.Parent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final List<Rule> rules = new ArrayList<>();

    public Parser addRule(final Rule rule) {
        rules.add(rule);
        return this;
    }

    public Parser addRules(final Collection<Rule> rules) {
        for (final Rule rule : rules) {
            addRule(rule);
        }
        return this;
    }

    public Parser addRules(final Rule... rules) {
        for (final Rule rule : rules) {
            addRule(rule);
        }
        return this;
    }

    public List<Node> parse(final CharSequence source) {
        final Stack<NodeBuilder> stack = new Stack<>();
        final Root root = new Root();
        stack.add(new NodeBuilder(root, 0, source.length()));

        while (!stack.isEmpty()) {

            final NodeBuilder builder = stack.pop();

            if (builder.startIndex >= builder.endIndex) {
                break;
            }

            final CharSequence inspectionSource = source.subSequence(builder.startIndex, builder.endIndex);
            final int offset = builder.startIndex;

            boolean foundRule = false;
            for (final Rule rule : rules) {

                final Matcher matcher = rule.pattern.matcher(inspectionSource);

                if (matcher.find()) {
                    foundRule = true;

                    if (builder.node instanceof Parent) {

                        final NodeBuilder newBuilder = rule.parse(matcher);
                        newBuilder.applyOffset(offset);

                        if (newBuilder.node instanceof Parent) {
                            stack.push(newBuilder);
                        }

                        // We want to speak in terms of indices within the source string,
                        // but the Rules only see the matchers in the context of the substring
                        // being examined. Adding this offset address that issue.
                        final int matcherSourceEnd = matcher.end() + offset;

                        if (matcherSourceEnd != builder.endIndex) {
                            stack.push(new NodeBuilder(builder.node, matcherSourceEnd, builder.endIndex));
                        }

                        ((Parent) builder.node).addChild(newBuilder.node);
                    }

                    break;
                }
            }

            if (!foundRule) {
                throw new RuntimeException("failed to find rule to match source: \"" + inspectionSource + "\"");
            }
        }

        return root.getChildren();
    }

    /**
     * Facilitates fast parsing of the source text.
     *
     * The provided Node will be added to the tree, and text between startIndex (inclusive)
     * and endIndex (exclusive) will continue to be parsed into Nodes and added as children under
     * this Node.
     */
    public static class NodeBuilder {
        private final Node node;
        private int startIndex;
        private int endIndex;

        public NodeBuilder(Node node, int startIndex, int endIndex) {
            this.node = node;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        private void applyOffset(final int offset) {
            startIndex += offset;
            endIndex += offset;
        }
    }

    public static abstract class Rule {

        private final Pattern pattern;

        public Rule(final Pattern pattern) {
            this.pattern = pattern;
        }

        public abstract NodeBuilder parse(Matcher matcher);
    }

    private static class Root implements Parent {

        private final List<Node> children = new ArrayList<>();

        @Override
        public String getType() {
            return "root";
        }

        @Override
        public List<Node> getChildren() {
            return children;
        }

        @Override
        public void addChild(Node child) {
            children.add(child);
        }
    }
}
