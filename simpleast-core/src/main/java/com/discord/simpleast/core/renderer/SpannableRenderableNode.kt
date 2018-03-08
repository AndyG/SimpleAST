package com.discord.simpleast.core.renderer

import android.content.Context
import android.text.SpannableStringBuilder
import com.discord.simpleast.core.node.Node

abstract class SpannableRenderableNode : Node() {
  abstract fun render(builder: SpannableStringBuilder, context: Context? = null)
}