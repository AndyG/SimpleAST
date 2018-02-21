package com.agarron.simpleast_core.node;


public class TextNode extends Node {

    public static final String TYPE = "text";

    public final String content;

    public TextNode(final String content) {
        super(TYPE);
        this.content = content;
    }
}
