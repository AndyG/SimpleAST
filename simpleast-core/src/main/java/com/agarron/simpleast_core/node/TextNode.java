package com.agarron.simpleast_core.node;


public class TextNode extends Node {

    private static final String TYPE = "text";

    private final String content;

    public TextNode(final String content) {
        super(TYPE);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
//
//    @Override
//    public void render(final SpannableStringBuilder builder, final Context context) {
//        builder.append(content);
//    }
}
