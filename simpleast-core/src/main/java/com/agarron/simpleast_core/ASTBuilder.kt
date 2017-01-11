package com.agarron.simpleast_core

import com.agarron.simpleast_core.Node
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class ASTBuilder {

    private val rules = ArrayList<Rule>()

    fun addRule(rule: Rule) {
        rules.add(rule)
    }

    private fun nestedParse(source: CharSequence, rules: Collection<Rule>): Collection<Node> {
        val result = ArrayList<Node>()
        var mutableSource = source

        while (!mutableSource.isEmpty()) {
            var foundRule = false
            for(index in 0..rules.size - 1) {
                val rule = rules.elementAt(index)
                val matcher = rule.getPattern().matcher(mutableSource)
                if (matcher.find()) {
                    val match: String = matcher.group()
                    mutableSource = mutableSource.subSequence(match.length..mutableSource.length - 1)

                    foundRule = true

                    val parsedNode = rule.parse(matcher, this)
                    result.add(parsedNode)
                    break
                }
            }

            if (!foundRule) {
                throw RuntimeException("failed to find rule to match source: $mutableSource")
            }
        }

        return result
    }

    interface Rule {
        fun getPattern(): Pattern
        fun parse(matcher: Matcher, astBuilder: ASTBuilder): Node
    }

    fun parse(charSequence: CharSequence): Collection<Node> {
        val tree = nestedParse(charSequence, rules)
        return tree
    }
}

