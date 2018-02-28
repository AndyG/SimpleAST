package com.agarron.simpleast_core.simple

import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import com.agarron.simpleast_core.node.StyleNode
import com.agarron.simpleast_core.node.TextNode
import com.agarron.simpleast_core.parser.ParseSpec
import com.agarron.simpleast_core.parser.Parser
import com.agarron.simpleast_core.parser.Rule
import com.agarron.simpleast_core.renderer.SpannableRenderableNode
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object SimpleMarkdownRules {
  var PATTERN_BOLD = Pattern.compile("^\\*\\*([\\s\\S]+?)\\*\\*(?!\\*)")
  var PATTERN_UNDERLINE = Pattern.compile("^__([\\s\\S]+?)__(?!_)")
  var PATTERN_STRIKETHRU = Pattern.compile("^~~(?=\\S)([\\s\\S]*?\\S)~~")
  var PATTERN_TEXT = Pattern.compile("^[\\s\\S]+?(?=[^0-9A-Za-z\\s\\u00c0-\\uffff]|\\n\\n| {2,}\\n|\\w+:\\S|$)")
  val PATTERN_ESCAPE = Pattern.compile("^\\\\([^0-9A-Za-z\\s])")

  var PATTERN_ITALICS = Pattern.compile(
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
  )

  fun createBoldRule(): Rule<SpannableRenderableNode> {
    return createSimpleStyleRule(PATTERN_BOLD, object : StyleFactory {
      override fun get(): CharacterStyle {
        return StyleSpan(Typeface.BOLD)
      }
    })
  }

  fun createUnderlineRule(): Rule<SpannableRenderableNode> {
    return createSimpleStyleRule(PATTERN_UNDERLINE, object : StyleFactory {
      override fun get(): CharacterStyle {
        return UnderlineSpan()
      }
    })
  }

  fun createStrikethruRule(): Rule<SpannableRenderableNode> {
    return createSimpleStyleRule(PATTERN_STRIKETHRU, object : StyleFactory {
      override fun get(): CharacterStyle {
        return StrikethroughSpan()
      }
    })
  }

  fun createTextRule(): Rule<SpannableRenderableNode> {
    return object : Rule<SpannableRenderableNode>(PATTERN_TEXT, true) {
      override fun parse(matcher: Matcher, parser: Parser<SpannableRenderableNode>, isNested: Boolean): ParseSpec<SpannableRenderableNode> {
        val node = TextNode(matcher.group())
        return ParseSpec.createTerminal(node)
      }
    }
  }

  fun createEscapeRule(): Rule<SpannableRenderableNode> {
    return object : Rule<SpannableRenderableNode>(PATTERN_ESCAPE, false) {
      override fun parse(matcher: Matcher, parser: Parser<SpannableRenderableNode>, isNested: Boolean): ParseSpec<SpannableRenderableNode> {
        return ParseSpec.createTerminal(TextNode(matcher.group(1)) as SpannableRenderableNode)
      }
    }
  }

  fun createItalicsRule(): Rule<SpannableRenderableNode> {
    return object : Rule<SpannableRenderableNode>(PATTERN_ITALICS, false) {
      override fun parse(matcher: Matcher, parser: Parser<SpannableRenderableNode>, isNested: Boolean): ParseSpec<SpannableRenderableNode> {
        val startIndex: Int
        val endIndex: Int
        val asteriskMatch = matcher.group(2)
        if (asteriskMatch != null && asteriskMatch.length > 0) {
          startIndex = matcher.start(2)
          endIndex = matcher.end(2)
        } else {
          startIndex = matcher.start(1)
          endIndex = matcher.end(1)
        }

        val styles = ArrayList<CharacterStyle>(1)
        styles.add(StyleSpan(Typeface.ITALIC))

        val node = StyleNode(styles)
        return ParseSpec.createNonterminal(node, startIndex, endIndex)
      }
    }
  }

  @JvmOverloads @JvmStatic
  fun createSimpleMarkdownRules(includeTextRule: Boolean = true): List<Rule<SpannableRenderableNode>> {
    val rules = ArrayList<Rule<SpannableRenderableNode>>()
    rules.add(createEscapeRule())
    rules.add(createBoldRule())
    rules.add(createUnderlineRule())
    rules.add(createItalicsRule())
    rules.add(createStrikethruRule())
    if (includeTextRule) {
      rules.add(createTextRule())
    }
    return rules
  }

  private fun createSimpleStyleRule(pattern: Pattern, styleFactory: StyleFactory): Rule<SpannableRenderableNode> {
    return object : Rule<SpannableRenderableNode>(pattern, false) {
      override fun parse(matcher: Matcher, parser: Parser<SpannableRenderableNode>, isNested: Boolean): ParseSpec<SpannableRenderableNode> {
        val node = StyleNode(listOf(styleFactory.get()))
        return ParseSpec.createNonterminal(node, matcher.start(1), matcher.end(1))
      }
    }
  }

  private interface StyleFactory {
    fun get(): CharacterStyle
  }
}

