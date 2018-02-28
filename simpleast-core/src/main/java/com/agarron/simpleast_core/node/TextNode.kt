package com.agarron.simpleast_core.node


import android.content.Context
import android.text.SpannableStringBuilder

import com.agarron.simpleast_core.renderer.SpannableRenderableNode

/**
 * Node representing simple text.
 */
class TextNode(val content: String) : SpannableRenderableNode() {

  override fun render(builder: SpannableStringBuilder, context: Context?) {
    builder.append(content)
  }
}
