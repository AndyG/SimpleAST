package com.discord.simpleast.core.simple

import android.support.annotation.StringRes
import android.text.SpannableStringBuilder
import android.widget.TextView
import com.discord.simpleast.core.parser.Parser
import com.discord.simpleast.core.parser.Rule
import com.discord.simpleast.core.renderer.SpannableRenderableNode
import com.discord.simpleast.core.renderer.SpannableRenderer

object SimpleRenderer {

  fun renderBasicMarkdown(@StringRes sourceResId: Int, textView: TextView) {
    val source = textView.context.getString(sourceResId)
    renderBasicMarkdown(source, textView)
  }

  fun renderBasicMarkdown(source: CharSequence, textView: TextView) {
    textView.text = renderBasicMarkdown(source)
  }

  fun renderBasicMarkdown(source: CharSequence): SpannableStringBuilder {
    return render(source, SimpleMarkdownRules.createSimpleMarkdownRules<Any?>(), null)
  }

  fun <R> render(source: CharSequence, rules: Collection<Rule<SpannableRenderableNode<R>>>, renderContext: R): SpannableStringBuilder {
    val parser = Parser<SpannableRenderableNode<R>>()
    for (rule in rules) {
      parser.addRule(rule)
    }

    return SpannableRenderer.render(SpannableStringBuilder(), parser.parse(source, false), renderContext)
  }
}
