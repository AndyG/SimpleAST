package com.agarron.simpleast_core.parser

import com.agarron.simpleast_core.node.Node
import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class Rule<T : Node> @JvmOverloads constructor(pattern: Pattern, val applyOnNestedParse: Boolean = false) {

  val matcher: Matcher = pattern.matcher("")

  abstract fun parse(matcher: Matcher, parser: Parser<T>, isNested: Boolean): SubtreeSpec<T>
}

