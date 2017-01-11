package com.agarron.simpleast_core.node;

public class TextNode extends Node {

    private final String content;

    public TextNode(final String content) {
        this("text", content);
    }

    public TextNode(final String type, final String content) {
        super(type);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
