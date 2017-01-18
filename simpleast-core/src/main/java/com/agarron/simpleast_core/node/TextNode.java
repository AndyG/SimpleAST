package com.agarron.simpleast_core.node;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.agarron.simpleast_core.simple.SpannableRenderable;

public class TextNode implements Node, SpannableRenderable {

    public static final String TYPE = "text";

    private final String content;

    public TextNode(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void render(final SpannableStringBuilder builder, final Context context) {
        builder.append(content);
    }
}
