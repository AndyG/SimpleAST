package com.agarron.simpleast_core.node;

public class TextNode extends Node {

    public static final String TYPE = "text";

    private final String content;

    public TextNode(final String content) {
        this(TYPE, content);
    }

    public TextNode(final String type, final String content) {
        super(type);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
