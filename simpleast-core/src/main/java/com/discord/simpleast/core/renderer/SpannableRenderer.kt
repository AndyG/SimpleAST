package com.discord.simpleast.core.renderer

import android.text.SpannableStringBuilder

/**
 * A renderer that knows how to render the [SpannableRenderableNode] instances.
 */
object SpannableRenderer {

  @JvmStatic
  fun <T: SpannableStringBuilder, R> render(builder: T, ast: Collection<SpannableRenderableNode<R>>, renderContext: R): T {
    for (node in ast) {
      node.render(builder, renderContext)
    }

    return builder
  }
}

