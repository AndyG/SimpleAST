package com.agarron.simpleast_core.simple;

import android.content.Context;
import android.text.SpannableStringBuilder;

import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.Parent;

import java.util.ArrayList;
import java.util.List;

public class SpannableRenderableParent implements SpannableRenderable, Parent {

    protected final List<SpannableRenderable> children = new ArrayList<>();

    public static final String TYPE = "parent";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public List getChildren() {
        return children;
    }

    @Override
    public void addChild(Node child) {
        children.add((SpannableRenderable) child);
    }

    @Override
    public void render(SpannableStringBuilder builder, Context context) {
        // First render all child nodes, as these are the nodes we want to apply the styles to.
        for (final SpannableRenderable child : children) {
            child.render(builder, context);
        }
    }
}
