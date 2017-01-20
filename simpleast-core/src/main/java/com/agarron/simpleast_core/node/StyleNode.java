package com.agarron.simpleast_core.node;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import com.agarron.simpleast_core.simple.SpannableRenderable;

import java.util.ArrayList;
import java.util.List;

public class StyleNode implements Node, Parent, SpannableRenderable {

    public static final String TYPE = "style";

    public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
        final StyleNode styleNode = new StyleNode(styles);
        styleNode.addChild(new TextNode(content));
        return styleNode;
    }

    private final List<Node> children;
    private final List<CharacterStyle> styles;

    public StyleNode(final List<CharacterStyle> styles) {
        this.styles = styles;
        this.children = new ArrayList<>();
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public void addChild(Node child) {
        children.add(child);
    }

    public List<CharacterStyle> getStyles() {
        return styles;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void render(final SpannableStringBuilder builder, final Context context) {
        final int startIndex = builder.length();

        // First render all child nodes, as these are the nodes we want to apply the styles to.
        for (final Node child : children) {
            if (child instanceof SpannableRenderable) {
                ((SpannableRenderable) child).render(builder, context);
            }
        }

        for (final CharacterStyle style : styles) {
            builder.setSpan(style, startIndex, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
