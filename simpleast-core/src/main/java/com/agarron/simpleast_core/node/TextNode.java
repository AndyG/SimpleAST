package com.agarron.simpleast_core.node;

public class TextNode implements Node {

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
}
