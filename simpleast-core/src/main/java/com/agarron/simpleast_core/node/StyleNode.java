package com.agarron.simpleast_core.node;

import android.text.style.CharacterStyle;

import java.util.List;

public class StyleNode extends Node {

  public final List<CharacterStyle> styles;

  public StyleNode(final List<CharacterStyle> styles) {
    this.styles = styles;
  }

  public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
    final StyleNode styleNode = new StyleNode(styles);
    styleNode.addChild(new TextNode(content));
    return styleNode;
  }
}
