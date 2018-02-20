package com.agarron.simpleast_core.node;

import android.text.style.CharacterStyle;

import java.util.List;

public class StyleNode extends Node {

  private static final String TYPE = "style";

  private final List<CharacterStyle> styles;

  public StyleNode(final List<CharacterStyle> styles) {
    super(TYPE);
    this.styles = styles;
  }

  public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
    final StyleNode styleNode = new StyleNode(styles);
    styleNode.addChild(new TextNode(content));
    return styleNode;
  }
//
//  @Override
//  public void render(final SpannableStringBuilder builder, final Context context) {
//    final int startIndex = builder.length();
//
//    // First render all child nodes, as these are the nodes we want to apply the styles to.
//    for (final Node child : getChildren()) {
//      child.render(builder, context);
//    }
//
//    for (final CharacterStyle style : styles) {
//      builder.setSpan(style, startIndex, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }
//  }

  public List<CharacterStyle> getStyles() {
    return styles;
  }
}
