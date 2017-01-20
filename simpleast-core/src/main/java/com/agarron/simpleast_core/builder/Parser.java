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

    private final Root root = new Root();

    private Stack<NodeBuilder> stack = new Stack<>();

    private final List<Rule> rules = new ArrayList<>();

    private final CharSequence source;

    public Parser(final CharSequence source) {
        this.source = source;
    }

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

//    public List<Node> safeParse(CharSequence source) {
//        try {
//            return parse(source, 0);
//        } catch (StackOverflowError e) {
//            Log.d("findme", "caught StackOverflowError", e);
//        }
//
//        return null;
//    }

    public List<Node> iterativeParse() {

        stack.add(new NodeBuilder(root, 0, source.length()));

        while (!stack.isEmpty()) {

            Log.d("findme", "iterating with stack size: " + stack.size());

            final NodeBuilder builder = stack.pop();

            Log.d("findme", "builder: " + builder.startIndex + " -- " + builder.endIndex);

            if (builder.startIndex >= builder.endIndex) {
                break;
            }

            final CharSequence inspectionSource = source.subSequence(builder.startIndex, builder.endIndex);
            Log.d("findme", "iterativeParse: inspecting " + inspectionSource);

            boolean foundRule = false;
            for (final Rule rule : rules) {

                final Matcher matcher = rule.pattern.matcher(inspectionSource);

                if (matcher.find()) {
                    foundRule = true;

                    if (builder.node instanceof Parent) {

                        final NodeBuilder newBuilder = rule.parse(matcher);

                        if (newBuilder.node instanceof Parent) {
                            stack.push(newBuilder);
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

//    public List<Node> parse(CharSequence source, final int depth) {
//        Log.d("findme", "called parse with depth: " + depth);
//
//        final List<Node> result = new ArrayList<>();
//
//        while (source.length() > 0) {
//            boolean foundRule = false;
//
//            for (final Rule rule : rules) {
//                final Matcher matcher = rule.pattern.matcher(source);
//                if (matcher.find()) {
//                    final String match = matcher.group();
//                    source = source.subSequence(match.length(), source.length());
//                    foundRule = true;
//
//                    final Node node = rule.parse(matcher, this, depth);
//                    result.add(node);
//                    break;
//                }
//            }
//
//            if (!foundRule) {
//                throw new RuntimeException("failed to find rule to match source: " + source);
//            }
//        }
//
//        return result;
//    }

    public static class NodeBuilder {
        private final Node node;
        private final int startIndex;
        private final int endIndex;

        public NodeBuilder(Node node, int startIndex, int endIndex) {
            this.node = node;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
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
