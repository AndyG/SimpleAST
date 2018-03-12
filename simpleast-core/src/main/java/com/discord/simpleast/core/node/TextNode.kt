package com.discord.simpleast.core.node


import android.text.SpannableStringBuilder
import com.discord.simpleast.core.renderer.SpannableRenderableNode

/**
 * Node representing simple text.
 */
open class TextNode<in R> (val content: String) : SpannableRenderableNode<R>() {
  override fun render(builder: SpannableStringBuilder, renderContext: R) {
    builder.append(content)
  }
}
