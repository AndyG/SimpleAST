package com.agarron.simpleast_core.utils;

import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.Parent;

import java.util.Collection;
import java.util.List;

public class ASTUtils {
    public static <T extends Node> void traversePostOrder(final Collection<T> ast, final NodeProcessor nodeProcessor) {
        for (final T node : ast) {
            traversePostOrderSubtree(node, nodeProcessor);
        }
    }

    private static <T extends Node> void traversePostOrderSubtree(final T node, final NodeProcessor nodeProcessor) {
        if (node instanceof Parent) {
            final Parent<T> parent = (Parent<T>) node;
            final List<T> children = parent.getChildren();
            for (final T child : children) {
                traversePostOrderSubtree(child, nodeProcessor);
            }
        }

        nodeProcessor.processNode(node);
    }
}
