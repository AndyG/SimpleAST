package com.agarron.simpleast_core.builder;


import com.agarron.simpleast_core.node.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        final List<Node> result = new ArrayList<>();
        CharSequence mutableSource = source;

        while (mutableSource.length() > 0) {
            boolean foundRule = false;

            for (final Rule rule : rules) {
                final Matcher matcher = rule.pattern.matcher(mutableSource);
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

    public static abstract class Rule {

        private final Pattern pattern;

        public Rule(final Pattern pattern) {
            this.pattern = pattern;
        }

        public abstract Node parse(Matcher matcher, Parser parser);
    }
}
