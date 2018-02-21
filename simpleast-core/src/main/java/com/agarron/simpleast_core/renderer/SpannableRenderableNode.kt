package com.agarron.simpleast_core.renderer

import android.content.Context
import android.text.SpannableStringBuilder
import com.agarron.simpleast_core.node.Node

abstract class SpannableRenderableNode : Node() {
  abstract fun render(builder: SpannableStringBuilder, context: Context)
}