package com.agarron.simpleast_core.builder;


import android.util.Log;

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

    public List<Node> safeParse(CharSequence source) {
        try {
            return parse(source, 0);
        } catch (StackOverflowError e) {
            Log.d("findme", "caught StackOverflowError", e);
        }

        return null;
    }

    public List<Node> parse(CharSequence source, final int depth) {
        Log.d("findme", "called parse with depth: " + depth);

        final List<Node> result = new ArrayList<>();

        while (source.length() > 0) {
            boolean foundRule = false;

            for (final Rule rule : rules) {
                final Matcher matcher = rule.pattern.matcher(source);
                if (matcher.find()) {
                    final String match = matcher.group();
                    source = source.subSequence(match.length(), source.length());
                    foundRule = true;

                    final Node node = rule.parse(matcher, this, depth);
                    result.add(node);
                    break;
                }
            }

            if (!foundRule) {
                throw new RuntimeException("failed to find rule to match source: " + source);
            }
        }

        return result;
    }

    public static abstract class Rule {

        private final Pattern pattern;

        public Rule(final Pattern pattern) {
            this.pattern = pattern;
        }

        public abstract Node parse(Matcher matcher, Parser parser, final int depth);
    }
}
