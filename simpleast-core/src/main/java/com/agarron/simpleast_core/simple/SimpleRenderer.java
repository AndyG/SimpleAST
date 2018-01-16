package com.agarron.simpleast_core.simple;

import android.support.annotation.StringRes;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.agarron.simpleast_core.builder.Parser;
import com.agarron.simpleast_core.node.Node;

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

    public static <T extends Node> SpannableStringBuilder render(final CharSequence source, final Collection<Parser.Rule<T>> rules) {
        final Parser<T> parser = new Parser<>();
        for (final Parser.Rule<T> rule : rules) {
            parser.addRule(rule);
        }

        return render(parser.parse(source, false));
    }

    public static <T extends Node> SpannableStringBuilder render(final Collection<T> ast) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        for (final Node node : ast) {
            renderNode(node, builder);
        }

        return builder;
    }

    private static void renderNode(final Node node, final SpannableStringBuilder builder) {
        if (node instanceof SpannableRenderable) {
            ((SpannableRenderable) node).render(builder, null);
        } else {
            throw new IllegalArgumentException("invalid node of type: " + node.getType());
        }
    }
}
