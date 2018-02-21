package com.agarron.simpleast_core.renderer

import android.content.Context
import android.text.SpannableStringBuilder
import com.agarron.simpleast_core.node.Node

/**
 * A renderer that knows how to render the [Node] types provided by default in the SimpleAST.
 *
 * Consumers may override [customRenderMethods] to provide additional methods for custom nodes.
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

