package com.agarron.simpleast_core.simple;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.agarron.simpleast_core.builder.ASTBuilder;
import com.agarron.simpleast_core.node.Node;
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

    public static ASTBuilder.Rule RULE_BOLD = createSimpleStyleRule(PATTERN_BOLD, new StyleFactory() {
        @Override
        public CharacterStyle get() {
            return new StyleSpan(Typeface.BOLD);
        }
    });

    public static ASTBuilder.Rule RULE_UNDERLINE = createSimpleStyleRule(PATTERN_UNDERLINE, new StyleFactory() {
        @Override
        public CharacterStyle get() {
            return new UnderlineSpan();
        }
    });

    public static ASTBuilder.Rule RULE_STRIKETHRU = createSimpleStyleRule(PATTERN_STRIKETHRU, new StyleFactory() {
        @Override
        public CharacterStyle get() {
            return new StrikethroughSpan();
        }
    });

    public static ASTBuilder.Rule RULE_TEXT = new ASTBuilder.Rule() {
        @Override
        public Pattern getPattern() {
            return PATTERN_TEXT;
        }

        @Override
        public Node parse(final Matcher matcher, final ASTBuilder astBuilder) {
            return new TextNode(matcher.group());
        }
    };

    public static ASTBuilder.Rule RULE_ITALICS = new ASTBuilder.Rule() {
        @Override
        public Pattern getPattern() {
            return PATTERN_ITALICS;
        }

        @Override
        public Node parse(final Matcher matcher, final ASTBuilder astBuilder) {
            final String match;
            final String asteriskMatch = matcher.group(2);
            if (asteriskMatch != null && asteriskMatch.length() > 0) {
                match = asteriskMatch;
            } else {
                match = matcher.group(1);
            }

            final Collection<CharacterStyle> styles = new ArrayList<>(1);
            styles.add(new StyleSpan(Typeface.ITALIC));
            return new StyleNode(styles, astBuilder.parse(match));
        }
    };

    public static List<ASTBuilder.Rule> getSimpleMarkdownRules() {
        final List<ASTBuilder.Rule> rules = new ArrayList<>();
        rules.add(RULE_BOLD);
        rules.add(RULE_UNDERLINE);
        rules.add(RULE_ITALICS);
        rules.add(RULE_STRIKETHRU);
        rules.add(RULE_TEXT);
        return rules;
    }

    private static ASTBuilder.Rule createSimpleStyleRule(final Pattern pattern, final StyleFactory styleFactory) {
        return new ASTBuilder.Rule() {
            @Override
            public Pattern getPattern() {
                return pattern;
            }

            @Override
            public Node parse(Matcher matcher, ASTBuilder astBuilder) {
                return new StyleNode(Collections.singleton(styleFactory.get()), astBuilder.parse(matcher.group(1)));
            }
        };
    }

    private interface StyleFactory {
        CharacterStyle get();
    }
}
