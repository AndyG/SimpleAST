package com.discord.simpleast.core.renderer

import android.text.SpannableStringBuilder
import com.discord.simpleast.core.node.Node

/**
 * A renderer that knows how to render the nodes.
 */
object SpannableRenderer {

  @JvmStatic
  fun <T: SpannableStringBuilder, R> render(builder: T, ast: Collection<Node<R>>, renderContext: R): T {
    for (node in ast) {
      node.render(builder, renderContext)
    }

    return builder
  }
}

