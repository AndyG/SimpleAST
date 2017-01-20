package com.agarron.simpleast_core.builder;


import android.util.Log;

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

            Log.d("findme", "iterating with stack size: " + stack.size());

            final NodeBuilder builder = stack.pop();

            Log.d("findme", "builder: " + builder.startIndex + " -- " + builder.endIndex);

            if (builder.startIndex >= builder.endIndex) {
                break;
            }

            final CharSequence inspectionSource = source.subSequence(builder.startIndex, builder.endIndex);
            final int offset = builder.startIndex;

            Log.d("findme", "iterativeParse: inspecting " + inspectionSource);

            boolean foundRule = false;
            for (final Rule rule : rules) {

                final Matcher matcher = rule.pattern.matcher(inspectionSource);

                if (matcher.find()) {

                    final int matchStart = matcher.start();
                    final int matchEnd = matcher.end();

                    final int matcherSourceStart = matchStart + offset;
                    final int matcherSourceEnd = matchEnd + offset;

                    foundRule = true;

                    if (builder.node instanceof Parent) {

                        final NodeBuilder newBuilder = rule.parse(matcher, (Parent) builder.node);
                        newBuilder.applyOffset(offset);

                        if (newBuilder.node instanceof Parent) {
                            stack.push(newBuilder);
                        }

                        if (matcherSourceEnd != builder.endIndex) {
                            stack.push(new NodeBuilder(builder.node, matcherSourceEnd, builder.endIndex));
                        }

                        final int leftoverStart = newBuilder.endIndex;
                        final int leftoverEnd = builder.endIndex;

                        Log.d("findme", "leftover start: " + leftoverStart);
                        Log.d("findme", "leftover end: " + leftoverEnd);

                        ((Parent) builder.node).addChild(newBuilder.node);
                        Log.d("findme", "added node of type: " + newBuilder.node.getClass().getSimpleName());
                    }

                    break;
                }
            }

            if (!foundRule) {
                throw new RuntimeException("failed to find rule to match source: \"" + inspectionSource + "\"");
            }

            Log.d("findme", "--------------------------");
        }

        return root.getChildren();
    }

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

        public abstract NodeBuilder parse(Matcher matcher, Parent parent);
    }

    private static class Root implements Parent {

        private final List<Node> children = new ArrayList<>();

        @Override
        public String getType() {
            return "root";
        }

        @Override
        public Parent getParent() {
            return null;
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
