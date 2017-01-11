package com.agarron.simpleast_core.node;

import android.text.style.CharacterStyle;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StyleNode extends Node implements Parent {

    public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
        return createWithText("style", content, styles);
    }

    public static StyleNode createWithText(final String type, final String content, final List<CharacterStyle> styles) {
        final Node textNode = new TextNode(content);
        return new StyleNode(type, styles, Collections.singletonList(textNode));
    }

    private final List<Node> children;
    private final Collection<CharacterStyle> styles;

    public StyleNode(final Collection<CharacterStyle> styles, final List<Node> children) {
        this("style", styles, children);
    }

    public StyleNode(final String type, final Collection<CharacterStyle> styles, final List<Node> children) {
        super(type);
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
}
