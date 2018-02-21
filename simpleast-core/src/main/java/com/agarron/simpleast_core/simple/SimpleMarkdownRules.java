package com.agarron.simpleast_core.simple;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.StyleNode;
import com.agarron.simpleast_core.node.TextNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
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
        "(?:\\*\\*|\\s+(?:[^*\\s]|\\*\\*)|[^\\s*])+?" +
        // followed by a non-space, non-* then *
        ")\\*(?!\\*)"
    );

    public static Parser.Rule createBoldRule() {
        return createSimpleStyleRule(PATTERN_BOLD, new StyleFactory() {
            @Override
            public CharacterStyle get() {
                return new StyleSpan(Typeface.BOLD);
            }
        });
    }

    public static Parser.Rule createUnderlineRule() {
        return createSimpleStyleRule(PATTERN_UNDERLINE, new StyleFactory() {
            @Override
            public CharacterStyle get() {
                return new UnderlineSpan();
            }
        });
    }

    public static Parser.Rule createStrikethruRule() {
        return createSimpleStyleRule(PATTERN_STRIKETHRU, new StyleFactory() {
            @Override
            public CharacterStyle get() {
                return new StrikethroughSpan();
            }
        });
    }

    public static Parser.Rule createTextRule() {
        return new Parser.Rule(PATTERN_TEXT, true) {
            @Override
            public Parser.SubtreeSpec parse(Matcher matcher, Parser parser, boolean isNested) {
                final Node node = new TextNode(matcher.group());
                return Parser.SubtreeSpec.createTerminal(node);
            }
        };
    }

    public static Parser.Rule createEscapeRule() {
       return new Parser.Rule(PATTERN_ESCAPE, false) {
            @Override
            public Parser.SubtreeSpec parse(Matcher matcher, Parser parser, boolean isNested) {
                return Parser.SubtreeSpec.createTerminal((Node) new TextNode(matcher.group(1)));
            }
        };
    }

    public static Parser.Rule createItalicsRule() {
        return new Parser.Rule(PATTERN_ITALICS, false) {
            @Override
            public Parser.SubtreeSpec parse(final Matcher matcher, Parser parser, boolean isNested) {
                final int startIndex, endIndex;
                final String asteriskMatch = matcher.group(2);
                if (asteriskMatch != null && asteriskMatch.length() > 0) {
                    startIndex = matcher.start(2);
                    endIndex = matcher.end(2);
                } else {
                    startIndex = matcher.start(1);
                    endIndex = matcher.end(1);
                }

                final List<CharacterStyle> styles = new ArrayList<>(1);
                styles.add(new StyleSpan(Typeface.ITALIC));
                final Node node = new StyleNode(styles);

                return Parser.SubtreeSpec.createNonterminal(node, startIndex, endIndex);
            }
        };
    }

    public static List<Parser.Rule> createSimpleMarkdownRules() {
        return createSimpleMarkdownRules(true);
    }

    public static List<Parser.Rule> createSimpleMarkdownRules(final boolean includeTextRule) {
        final List<Parser.Rule> rules = new ArrayList<>();
        rules.add(createEscapeRule());
        rules.add(createBoldRule());
        rules.add(createUnderlineRule());
        rules.add(createItalicsRule());
        rules.add(createStrikethruRule());
        if (includeTextRule) {
            rules.add(createTextRule());
        }
        return rules;
    }

    private static Parser.Rule createSimpleStyleRule(final Pattern pattern, final StyleFactory styleFactory) {
        return new Parser.Rule(pattern, false) {
            @Override
            public Parser.SubtreeSpec parse(Matcher matcher, Parser parser, boolean isNested) {
                final Node node = new StyleNode(Collections.singletonList(styleFactory.get()));
                return Parser.SubtreeSpec.createNonterminal(node, matcher.start(1), matcher.end(1));
            }
        };
    }

    private interface StyleFactory {
        CharacterStyle get();
    }
}
