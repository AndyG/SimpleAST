package com.discord.simpleast.core.renderer

import android.text.SpannableStringBuilder
import com.discord.simpleast.core.node.Node

abstract class SpannableRenderableNode<in R> : Node() {
  abstract fun render(builder: SpannableStringBuilder, renderContext: R)
}