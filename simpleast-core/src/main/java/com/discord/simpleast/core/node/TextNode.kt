package com.discord.simpleast.core.node


import android.content.Context
import android.text.SpannableStringBuilder

import com.discord.simpleast.core.renderer.SpannableRenderableNode

/**
 * Node representing simple text.
 */
class TextNode(val content: String) : SpannableRenderableNode() {

  override fun render(builder: SpannableStringBuilder, context: Context?) {
    builder.append(content)
  }
}
