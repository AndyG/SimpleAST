package com.agarron.simpleast_core.renderer

import android.text.SpannableStringBuilder
import android.text.Spanned
import com.agarron.simpleast_core.node.Node
import com.agarron.simpleast_core.node.StyleNode
import com.agarron.simpleast_core.node.TextNode

/**
 * A renderer that knows how to render the [Node] types provided by default in the SimpleAST.
 *
 * Consumers may override [customRenderMethods] to provide additional methods for custom nodes.
 */
open class DefaultRenderer {

  protected val customRenderMethods: Map<Class<out Node>, (Node, SpannableStringBuilder) -> Unit> = emptyMap()

  private val renderMethodsInternal: Map<Class<out Node>, (Node, SpannableStringBuilder) -> Unit> = customRenderMethods
      .plus(mapOf(
          StyleNode::class.java to { node, builder -> renderStyleNode(node as StyleNode, builder) },
          TextNode::class.java to { node, builder -> renderTextNode(node as TextNode, builder) }
      ))


  fun render(ast: Collection<Node>): SpannableStringBuilder {
    val builder = SpannableStringBuilder()

    for (node in ast) {
      renderNode(node, builder)
    }

    return builder
  }

  private fun renderNode(node: Node, builder: SpannableStringBuilder) {
    renderMethodsInternal[node.javaClass]
        ?.invoke(node, builder)
        ?: throw IllegalArgumentException("invalid node of type: " + node.type)
  }

  private fun renderStyleNode(styleNode: StyleNode, builder: SpannableStringBuilder) {
    val startIndex = builder.length

    // First render all child nodes, as these are the nodes we want to apply the styles to.
    styleNode.children.forEach { renderNode(it, builder) }

    for (style in styleNode.styles) {
      builder.setSpan(style, startIndex, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
  }

  private fun renderTextNode(textNode: TextNode, builder: SpannableStringBuilder) {
    builder.append(textNode.content)
  }
}
