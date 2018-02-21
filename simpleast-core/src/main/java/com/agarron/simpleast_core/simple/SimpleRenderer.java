package com.agarron.simpleast_core.simple;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.renderer.SpannableRenderableNode;
import com.agarron.simpleast_core.renderer.SpannableRenderer;

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

  public static SpannableStringBuilder render(final CharSequence source, final Collection<Parser.Rule<SpannableRenderableNode>> rules, final Context context) {
    final Parser<SpannableRenderableNode> parser = new Parser<>();
    for (final Parser.Rule<SpannableRenderableNode> rule : rules) {
      parser.addRule(rule);
    }

    return SpannableRenderer.render(new SpannableStringBuilder(), parser.parse(source, false), context);
  }
}
