@file:JvmName("SimpleMarkdownRules")
package com.agarron.simpleast_core

import android.graphics.Typeface
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@JvmField
val PATTERN_BOLD: Pattern = Pattern.compile("^\\*\\*([\\s\\S]+?)\\*\\*(?!\\*)")

@JvmField
val PATTERN_UNDERLINE: Pattern = Pattern.compile("^__([\\s\\S]+?)__(?!_)")

@JvmField
val PATTERN_STRIKETHRU: Pattern = Pattern.compile("^~~(?=\\S)([\\s\\S]*?\\S)~~")

@JvmField
val PATTERN_ITALICS: Pattern = Pattern.compile(
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
)

@JvmField
val PATTERN_TEXT: Pattern = Pattern.compile("^[\\s\\S]+?(?=[^0-9A-Za-z\\s\\u00c0-\\uffff]|\\n\\n| {2,}\\n|\\w+:\\S|$)")

@JvmField
val RULE_BOLD = object: ASTBuilder.Rule {
    override fun getPattern(): Pattern {
        return PATTERN_BOLD
    }

    override fun parse(matcher: Matcher, astBuilder: ASTBuilder): Node {
        val styles = Collections.singletonList(StyleSpan(Typeface.BOLD))
        return StyleNode(styles, astBuilder.parse(matcher.group(1)))
    }
}

@JvmField
val RULE_UNDERLINE = object: ASTBuilder.Rule {
    override fun getPattern(): Pattern {
        return PATTERN_UNDERLINE
    }

    override fun parse(matcher: Matcher, astBuilder: ASTBuilder): Node {
        val styles = Collections.singletonList(UnderlineSpan())
        return StyleNode(styles, astBuilder.parse(matcher.group(1)))
    }
}

@JvmField
val RULE_ITALICS = object: ASTBuilder.Rule {
    override fun getPattern(): Pattern {
        return PATTERN_ITALICS
    }

    override fun parse(matcher: Matcher, astBuilder: ASTBuilder): Node {
        val match: String
        val asteriskMatch = matcher.group(2)
        if (!asteriskMatch.isNullOrEmpty()) {
            match = asteriskMatch
        } else {
            match = matcher.group(1)
        }

        val styles = Collections.singletonList(StyleSpan(Typeface.ITALIC))
        return StyleNode(styles, astBuilder.parse(match))
    }
}

@JvmField
val RULE_STRIKETHRU = object: ASTBuilder.Rule {
    override fun getPattern(): Pattern {
        return PATTERN_STRIKETHRU
    }

    override fun parse(matcher: Matcher, astBuilder: ASTBuilder): Node {
        val styles = Collections.singletonList(StrikethroughSpan())
        return StyleNode(styles, astBuilder.parse(matcher.group(1)))
    }
}

@JvmField
val RULE_TEXT = object : ASTBuilder.Rule {
    override fun getPattern(): Pattern {
        return PATTERN_TEXT
    }

    override fun parse(matcher: Matcher, astBuilder: ASTBuilder): Node {
        return TextNode(matcher.group())
    }
}
