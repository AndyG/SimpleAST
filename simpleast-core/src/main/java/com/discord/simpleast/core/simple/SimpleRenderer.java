package com.discord.simpleast.core.simple;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.discord.simpleast.core.parser.Parser;
import com.discord.simpleast.core.parser.Rule;
import com.discord.simpleast.core.renderer.SpannableRenderableNode;
import com.discord.simpleast.core.renderer.SpannableRenderer;

import java.util.Collection;

public class SimpleRenderer {

  public static void renderBasicMarkdown(final TextView textView, @StringRes final int sourceResId) {
    final CharSequence source = textView.getContext().getString(sourceResId);
    renderBasicMarkdown(textView, source);
  }

  public static void renderBasicMarkdown(final TextView textView, final CharSequence source) {
    textView.setText(renderBasicMarkdown(source, textView.getContext()));
  }

  public static SpannableStringBuilder renderBasicMarkdown(final CharSequence source, final Context context) {
    return render(source, SimpleMarkdownRules.createSimpleMarkdownRules(), context);
  }

  public static SpannableStringBuilder renderBasicMarkdown(final CharSequence source) {
    return render(source, SimpleMarkdownRules.createSimpleMarkdownRules(), null);
  }

  public static SpannableStringBuilder render(final CharSequence source, final Collection<Rule<SpannableRenderableNode>> rules, final Context context) {
    final Parser<SpannableRenderableNode> parser = new Parser<>();
    for (final Rule<SpannableRenderableNode> rule : rules) {
      parser.addRule(rule);
    }

    return SpannableRenderer.render(new SpannableStringBuilder(), parser.parse(source, false), context);
  }
}
