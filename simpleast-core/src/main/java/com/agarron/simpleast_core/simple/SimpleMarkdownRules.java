package com.agarron.simpleast_core.simple;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.agarron.simpleast_core.node.StyleNode;
import com.agarron.simpleast_core.node.TextNode;
import com.agarron.simpleast_core.parser.Parser;
import com.agarron.simpleast_core.parser.Rule;
import com.agarron.simpleast_core.renderer.SpannableRenderableNode;

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

  public static Rule<SpannableRenderableNode> createBoldRule() {
    return createSimpleStyleRule(PATTERN_BOLD, new StyleFactory() {
      @Override
      public CharacterStyle get() {
        return new StyleSpan(Typeface.BOLD);
      }
    });
  }

  public static Rule<SpannableRenderableNode> createUnderlineRule() {
    return createSimpleStyleRule(PATTERN_UNDERLINE, new StyleFactory() {
      @Override
      public CharacterStyle get() {
        return new UnderlineSpan();
      }
    });
  }

  public static Rule<SpannableRenderableNode> createStrikethruRule() {
    return createSimpleStyleRule(PATTERN_STRIKETHRU, new StyleFactory() {
      @Override
      public CharacterStyle get() {
        return new StrikethroughSpan();
      }
    });
  }

  public static Rule<SpannableRenderableNode> createTextRule() {
    return new Rule<SpannableRenderableNode>(PATTERN_TEXT, true) {
      @Override
      public Parser.SubtreeSpec<SpannableRenderableNode> parse(Matcher matcher, Parser parser, boolean isNested) {
        final SpannableRenderableNode node = new TextNode(matcher.group());
        return Parser.SubtreeSpec.createTerminal(node);
      }
    };
  }

  public static Rule<SpannableRenderableNode> createEscapeRule() {
    return new Rule<SpannableRenderableNode>(PATTERN_ESCAPE, false) {
      @Override
      public Parser.SubtreeSpec<SpannableRenderableNode> parse(Matcher matcher, Parser parser, boolean isNested) {
        return Parser.SubtreeSpec.createTerminal((SpannableRenderableNode) new TextNode(matcher.group(1)));
      }
    };
  }

  public static Rule<SpannableRenderableNode> createItalicsRule() {
    return new Rule<SpannableRenderableNode>(PATTERN_ITALICS, false) {
      @Override
      public Parser.SubtreeSpec<SpannableRenderableNode> parse(final Matcher matcher, Parser parser, boolean isNested) {
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

        final SpannableRenderableNode node = new StyleNode(styles);
        return Parser.SubtreeSpec.createNonterminal(node, startIndex, endIndex);
      }
    };
  }

  public static List<Rule<SpannableRenderableNode>> createSimpleMarkdownRules() {
    return createSimpleMarkdownRules(true);
  }

  public static List<Rule<SpannableRenderableNode>> createSimpleMarkdownRules(final boolean includeTextRule) {
    final List<Rule<SpannableRenderableNode>> rules = new ArrayList<>();
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

  private static Rule<SpannableRenderableNode> createSimpleStyleRule(final Pattern pattern, final StyleFactory styleFactory) {
    return new Rule<SpannableRenderableNode>(pattern, false) {
      @Override
      public Parser.SubtreeSpec<SpannableRenderableNode> parse(Matcher matcher, Parser parser, boolean isNested) {
        final SpannableRenderableNode node = new StyleNode(Collections.singletonList(styleFactory.get()));
        return Parser.SubtreeSpec.createNonterminal(node, matcher.start(1), matcher.end(1));
      }
    };
  }

  private interface StyleFactory {
    CharacterStyle get();
  }
}

