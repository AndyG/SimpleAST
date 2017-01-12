package com.agarron.simpleast_core.builder;


import com.agarron.simpleast_core.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ASTBuilder {
    private final List<Rule> rules = new ArrayList<>();

    public void addRule(final Rule rule) {
        rules.add(rule);
    }

    public List<Node> parse(final CharSequence source) {
        final List<Node> result = new ArrayList<>();
        CharSequence mutableSource = source;

        while (mutableSource.length() > 0) {
            boolean foundRule = false;

            for (final Rule rule : rules) {
                final Matcher matcher = rule.getPattern().matcher(mutableSource);
                if (matcher.find()) {
                    final String match = matcher.group();
                    mutableSource = mutableSource.subSequence(match.length(), mutableSource.length());
                    foundRule = true;

                    final Node node = rule.parse(matcher, this);
                    result.add(node);
                    break;
                }
            }

            if (!foundRule) {
                throw new RuntimeException("failed to find rule to match source: " + mutableSource);
            }
        }

        return result;
    }

    public interface Rule {
        Pattern getPattern();
        Node parse(Matcher matcher, ASTBuilder astBuilder);
    }
}
