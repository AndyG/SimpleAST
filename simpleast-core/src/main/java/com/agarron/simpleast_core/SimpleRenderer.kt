package com.agarron.simpleastcore

import android.text.Spannable
import android.text.SpannableStringBuilder

fun render(ast: Collection<Node>): CharSequence {
    val builder = SpannableStringBuilder()
    ast.forEach { renderNode(it, builder) }
    return builder
}

fun renderNode(node: Node, builder: SpannableStringBuilder) {
    if (node is StyleNode){
        val startIndex = builder.length
        node.getChildren().forEach { renderNode(it, builder) }
        node.styles.forEach { builder.setSpan(it, startIndex, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
    } else if (node is TextNode) {
        builder.append(node.content)
    } else {
        throw IllegalArgumentException("invalid node of type: " + node.type)
    }
}
