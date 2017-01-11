package com.agarron.simpleastcore

import android.text.style.CharacterStyle
import java.util.*

open class Node(val type: String)

open class TextNode(type: String, val content: String) : Node(type) {
    constructor(content: String): this("text", content)
}

open class StyleNode : Node, Parent {

    val styles: Collection<CharacterStyle>
    private val children: Collection<Node>

    constructor(styles: Collection<CharacterStyle>, children: Collection<Node>): this("style", styles, children)

    constructor(type: String, styles: Collection<CharacterStyle>, children: Collection<Node>) : super(type) {
        this.styles = styles
        this.children = children
    }

    override fun getChildren(): Collection<Node> {
        return children
    }

    companion object {

        @JvmStatic
        fun createWithText(content: String, styles: Collection<CharacterStyle>): StyleNode {
            val textNode = TextNode(content)
            return StyleNode("style", styles, Collections.singleton(textNode))
        }
    }
}

interface Parent {
    fun getChildren(): Collection<Node>
}
