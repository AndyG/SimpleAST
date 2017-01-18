package com.agarron.simpleast_core.simple;

import android.content.Context;
import android.text.SpannableStringBuilder;

public interface SpannableRenderable {
    void render(final SpannableStringBuilder builder, final Context context);
}
