package com.agarron.simpleast_core.builder;

import android.support.annotation.Nullable;

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

    public List<Node> parse(final @Nullable CharSequence source) {
        final Stack<SubtreeSpec> stack = new Stack<>();
        final Root root = new Root();

        if (!isTextEmpty(source)) {
            stack.add(new SubtreeSpec(root, 0, source.length()));
        }

        while (!stack.isEmpty()) {
            final SubtreeSpec builder = stack.pop();

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

                    final SubtreeSpec newBuilder = rule.parse(matcher);
                    ((Parent) builder.root).addChild(newBuilder.root);

                    if (!newBuilder.isTerminal) {
                        newBuilder.applyOffset(offset);
                        stack.push(newBuilder);
                    }

                    // We want to speak in terms of indices within the source string,
                    // but the Rules only see the matchers in the context of the substring
                    // being examined. Adding this offset address that issue.
                    final int matcherSourceEnd = matcher.end() + offset;

                    // In case the last match didn't consume the rest of the source for this subtree,
                    // make sure the rest of the source is consumed.
                    if (matcherSourceEnd != builder.endIndex) {
                        stack.push(SubtreeSpec.createNonterminal((Parent) builder.root, matcherSourceEnd, builder.endIndex));
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

    private static boolean isTextEmpty(final CharSequence text) {
        return text == null || text.length() == 0;
    }

    /**
     * Facilitates fast parsing of the source text.
     *
     * For nonterminal subtrees, the provided root will be added to the main, and text between
     * startIndex (inclusive) and endIndex (exclusive) will continue to be parsed into Nodes and
     * added as children under this root.
     *
     * For terminal subtrees, the root will simply be added to the tree and no additional parsing will
     * take place on the text.
     */
    public static class SubtreeSpec {
        private final Node root;
        private final boolean isTerminal;
        private int startIndex;
        private int endIndex;

        public static SubtreeSpec createNonterminal(Parent node, int startIndex, int endIndex) {
            return new SubtreeSpec(node, startIndex, endIndex);
        }

        public static SubtreeSpec createTerminal(Node node) {
            return new SubtreeSpec(node);
        }

        private SubtreeSpec(Parent root, int startIndex, int endIndex) {
            this.root = root;
            this.isTerminal = false;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        private SubtreeSpec(Node root) {
            this.root = root;
            this.isTerminal = true;
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

        public abstract SubtreeSpec parse(Matcher matcher);
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
