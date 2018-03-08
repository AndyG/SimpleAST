package com.discord.simpleast.core.renderer

import android.content.Context
import android.text.SpannableStringBuilder

/**
 * A renderer that knows how to render the [SpannableRenderableNode] instances.
 */
object SpannableRenderer {

  @JvmStatic
  fun <T: SpannableStringBuilder> render(builder: T, ast: Collection<SpannableRenderableNode>, context: Context? = null): T {
    for (node in ast) {
      node.render(builder, context)
    }

    return builder
  }
}

