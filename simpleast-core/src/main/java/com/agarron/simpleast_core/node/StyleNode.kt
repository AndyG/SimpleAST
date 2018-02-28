package com.agarron.simpleast_core.node

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.CharacterStyle

import com.agarron.simpleast_core.renderer.SpannableRenderableNode

class StyleNode(val styles: List<CharacterStyle>) : SpannableRenderableNode() {

  override fun render(builder: SpannableStringBuilder, context: Context?) {
    val startIndex = builder.length

    // First render all child nodes, as these are the nodes we want to apply the styles to.
    getChildren()?.forEach { (it as SpannableRenderableNode).render(builder, context) }

    styles.forEach { builder.setSpan(it, startIndex, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
  }

  companion object {

    /**
     * Convenience method for creating a [StyleNode] when we already know what
     * the text content will be.
     */
    @JvmStatic
    fun createWithText(content: String, styles: List<CharacterStyle>): StyleNode {
      val styleNode = StyleNode(styles)
      styleNode.addChild(TextNode(content))
      return styleNode
    }
  }
}
