package com.agarron.simpleast_core.renderer

import android.text.SpannableStringBuilder
import android.text.Spanned
import com.agarron.simpleast_core.node.Node
import com.agarron.simpleast_core.node.StyleNode
import com.agarron.simpleast_core.node.TextNode

class Renderer {

  fun render(ast: Collection<Node>): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
    for (node in ast) {
      renderNode(node, builder)
    }

    return builder
  }

  private fun renderNode(node: Node, builder: SpannableStringBuilder) {
    renderMethods[node.type]
        ?.invoke(node, builder)
        ?: throw IllegalArgumentException("invalid node of type: " + node.type)
  }

  private val renderMethods: Map<String, (Node, SpannableStringBuilder) -> Unit> = mapOf(
      StyleNode.TYPE to {node, builder ->
        node as StyleNode
        val startIndex = builder.length

        // First render all child nodes, as these are the nodes we want to apply the styles to.
        for (child in node.children) {
          renderNode(node, builder)
        }

        for (style in node.styles) {
          builder.setSpan(style, startIndex, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
      },
      TextNode.TYPE to {node, builder -> builder.append((node as TextNode).content) }
  )
}
