package com.agarron.simpleast_core.simple;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import com.agarron.simpleast_core.builder.ASTBuilder;
import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.StyleNode;
import com.agarron.simpleast_core.node.TextNode;

import java.util.Collection;

public class SimpleRenderer {

    public static SpannableStringBuilder renderBasicMarkdown(final CharSequence source) {
        return render(source, SimpleMarkdownRules.getSimpleMarkdownRules());
    }

    public static SpannableStringBuilder render(final CharSequence source, final Collection<ASTBuilder.Rule> rules) {
        final ASTBuilder parser = new ASTBuilder();
        for (final ASTBuilder.Rule rule : rules) {
            parser.addRule(rule);
        }

        return render(parser.parse(source));
    }

    public static SpannableStringBuilder render(final Collection<Node> ast) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        for (final Node node : ast) {
            renderNode(node, builder);
        }

        return builder;
    }

    private static void renderNode(final Node node, final SpannableStringBuilder builder) {
        if (node instanceof StyleNode) {
            final int startIndex = builder.length();
            final StyleNode styleNode = (StyleNode) node;

            for (final Node child : styleNode.getChildren()) {
                renderNode(child, builder);
            }

            for (final CharacterStyle style : styleNode.getStyles()) {
                builder.setSpan(style, startIndex, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (node instanceof TextNode) {
            builder.append(((TextNode)node).getContent());
        } else {
            throw new IllegalArgumentException("invalid node of type: " + node.getType());
        }
    }
}
