package com.agarron.simpleast_core.node;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import com.agarron.simpleast_core.renderer.SpannableRenderableNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StyleNode extends SpannableRenderableNode {

  public final List<CharacterStyle> styles;

  public StyleNode(final List<CharacterStyle> styles) {
    this.styles = styles;
  }

  public static StyleNode createWithText(final String content, final List<CharacterStyle> styles) {
    final StyleNode styleNode = new StyleNode(styles);
    styleNode.addChild(new TextNode(content));
    return styleNode;
  }

  @Override
  public void render(@NotNull SpannableStringBuilder builder, @Nullable Context context) {
    final int startIndex = builder.length();

    // First render all child nodes, as these are the nodes we want to apply the styles to.
    for (final Node child : getChildren()) {
      ((SpannableRenderableNode) child).render(builder, context);
    }

    for (final CharacterStyle style : styles) {
      builder.setSpan(style, startIndex, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }
}
