package com.agarron.simpleast_core.node;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import com.agarron.simpleast_core.simple.SpannableRenderableParent;

import java.util.List;

public class StyleNode extends SpannableRenderableParent {

    public static final String TYPE = "style";

    public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
        final StyleNode styleNode = new StyleNode(styles);
        styleNode.addChild(new TextNode(content));
        return styleNode;
    }

    private final List<CharacterStyle> styles;

    public StyleNode(final List<CharacterStyle> styles) {
        this.styles = styles;
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

        super.render(builder, context);

        for (final CharacterStyle style : styles) {
            builder.setSpan(style, startIndex, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
