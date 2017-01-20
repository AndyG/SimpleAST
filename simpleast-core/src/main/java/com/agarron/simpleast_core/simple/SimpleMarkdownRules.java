package com.agarron.simpleast_core.simple;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.node.StyleNode;
import com.agarron.simpleast_core.node.TextNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMarkdownRules {
    public static Pattern PATTERN_BOLD = Pattern.compile("^\\*\\*([\\s\\S]+?)\\*\\*(?!\\*)");
    public static Pattern PATTERN_UNDERLINE = Pattern.compile("^__([\\s\\S]+?)__(?!_)");
    public static Pattern PATTERN_STRIKETHRU = Pattern.compile("^~~(?=\\S)([\\s\\S]*?\\S)~~");
    public static Pattern PATTERN_TEXT = Pattern.compile("^[\\s\\S]+?(?=[^0-9A-Za-z\\s\\u00c0-\\uffff]|\\n\\n| {2,}\\n|\\w+:\\S|$)");
    public static final Pattern PATTERN_ESCAPE = Pattern.compile("^\\\\([^0-9A-Za-z\\s])");

    public static Pattern PATTERN_ITALICS = Pattern.compile(
        // only match _s surrounding words.
        "^\\b_" + "((?:__|\\\\[\\s\\S]|[^\\\\_])+?)_" + "\\b" +
        "|" +
        // Or match *s that are followed by a non-space:
        "^\\*(?=\\S)(" +
        // Match any of:
        //  - `**`: so that bolds inside italics don't close the
        // italics
        //  - whitespace
        //  - non-whitespace, non-* characters
        "(?:\\*\\*|\\s+|[^\\s\\*])*?" +
        // followed by a non-space, non-* then *
        "[^\\s\\*])\\*(?!\\*)"
    );

    public static Parser.Rule RULE_BOLD = createSimpleStyleRule(PATTERN_BOLD, new StyleFactory() {
        @Override
        public CharacterStyle get() {
            return new StyleSpan(Typeface.BOLD);
        }
    });

    public static Parser.Rule RULE_UNDERLINE = createSimpleStyleRule(PATTERN_UNDERLINE, new StyleFactory() {
        @Override
        public CharacterStyle get() {
            return new UnderlineSpan();
        }
    });

    public static Parser.Rule RULE_STRIKETHRU = createSimpleStyleRule(PATTERN_STRIKETHRU, new StyleFactory() {
        @Override
        public CharacterStyle get() {
            return new StrikethroughSpan();
        }
    });

    public static Parser.Rule RULE_TEXT = new Parser.Rule(PATTERN_TEXT) {
        @Override
        public Parser.NodeBuilder parse(Matcher matcher) {
            return new Parser.NodeBuilder(new TextNode(matcher.group()), 0, 0);
        }
    };

    public static Parser.Rule RULE_ESCAPE = new Parser.Rule(PATTERN_ESCAPE) {
        @Override
        public Parser.NodeBuilder parse(Matcher matcher) {
            return new Parser.NodeBuilder(new TextNode(matcher.group(1)), 0, 0);
        }
    };

    public static Parser.Rule RULE_ITALICS = new Parser.Rule(PATTERN_ITALICS) {
        @Override
        public Parser.NodeBuilder parse(final Matcher matcher) {
            final int startIndex, endIndex;
            final String asteriskMatch = matcher.group(2);
            if (asteriskMatch != null && asteriskMatch.length() > 0) {
                startIndex = matcher.start(2);
                endIndex = matcher.end(2);
            } else {
                startIndex = matcher.start(1);
                endIndex = matcher.end(1);
            }

            final Collection<CharacterStyle> styles = new ArrayList<>(1);
            styles.add(new StyleSpan(Typeface.ITALIC));
            final StyleNode node = new StyleNode(styles);

            return new Parser.NodeBuilder(node, startIndex, endIndex);
        }
    };

    public static List<Parser.Rule> getSimpleMarkdownRules() {
        final List<Parser.Rule> rules = new ArrayList<>();
        rules.add(RULE_ESCAPE);
        rules.add(RULE_BOLD);
        rules.add(RULE_UNDERLINE);
        rules.add(RULE_ITALICS);
        rules.add(RULE_STRIKETHRU);
        rules.add(RULE_TEXT);
        return rules;
    }

    private static Parser.Rule createSimpleStyleRule(final Pattern pattern, final StyleFactory styleFactory) {
        return new Parser.Rule(pattern) {
            @Override
            public Parser.NodeBuilder parse(Matcher matcher) {
                StyleNode node = new StyleNode(Collections.singleton(styleFactory.get()));
                return new Parser.NodeBuilder(node, matcher.start(1), matcher.end(1));
            }
        };
    }

    private interface StyleFactory {
        CharacterStyle get();
    }
}
