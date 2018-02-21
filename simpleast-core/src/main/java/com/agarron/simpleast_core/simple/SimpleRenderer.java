package com.agarron.simpleast_core.simple;

import android.support.annotation.StringRes;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.renderer.Renderer;

import java.util.Collection;

public class SimpleRenderer {

    public static void renderBasicMarkdown(final TextView textView, @StringRes final int sourceResId) {
        final CharSequence source = textView.getContext().getString(sourceResId);
        renderBasicMarkdown(textView, source);
    }

    public static void renderBasicMarkdown(final TextView textView, final CharSequence source) {
        textView.setText(renderBasicMarkdown(source));
    }

    public static SpannableStringBuilder renderBasicMarkdown(final CharSequence source) {
        return render(source, SimpleMarkdownRules.createSimpleMarkdownRules());
    }

    public static  SpannableStringBuilder render(final CharSequence source, final Collection<Parser.Rule> rules) {
        final Parser parser = new Parser();
        for (final Parser.Rule rule : rules) {
            parser.addRule(rule);
        }

        return new Renderer().render(parser.parse(source, false));
    }
}
