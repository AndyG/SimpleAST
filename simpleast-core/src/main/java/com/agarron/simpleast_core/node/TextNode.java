package com.agarron.simpleast_core.node;


import android.content.Context;
import android.text.SpannableStringBuilder;

import com.agarron.simpleast_core.renderer.SpannableRenderableNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextNode extends SpannableRenderableNode {

    public final String content;

    public TextNode(final String content) {
        this.content = content;
    }

  @Override
  public void render(@NotNull SpannableStringBuilder builder, @Nullable Context context) {
    builder.append(content);
  }
}
