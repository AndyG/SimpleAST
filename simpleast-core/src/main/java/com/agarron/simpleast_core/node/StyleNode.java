package com.agarron.simpleast_core.node;

import android.text.style.CharacterStyle;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StyleNode implements Node, Parent {

    public static final String TYPE = "style";

    public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
        final Node textNode = new TextNode(content);
        return new StyleNode(styles, Collections.singletonList(textNode));
    }

    private final List<Node> children;
    private final Collection<CharacterStyle> styles;

    public StyleNode(final Collection<CharacterStyle> styles, final List<Node> children) {
        this.styles = styles;
        this.children = children;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    public Collection<CharacterStyle> getStyles() {
        return styles;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
