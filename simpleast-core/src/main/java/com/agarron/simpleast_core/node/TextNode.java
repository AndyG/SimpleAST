package com.agarron.simpleast_core.node;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.agarron.simpleast_core.simple.SpannableRenderable;

public class TextNode implements Node, SpannableRenderable {

    public static final String TYPE = "text";

    private final String content;
    private final Parent parent;

    public TextNode(final String content, final Parent parent) {
        this.content = content;
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Parent getParent() {
        return parent;
    }

    @Override
    public void render(final SpannableStringBuilder builder, final Context context) {
        builder.append(content);
    }
}
