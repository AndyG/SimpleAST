package com.agarron.simpleast_core.utils;

import com.agarron.simpleast_core.node.Node;
import com.agarron.simpleast_core.node.Parent;

import java.util.Collection;

public class ASTUtils {
    public static void traversePostOrder(final Collection<Node> ast, final NodeProcessor nodeProcessor) {
        for (final Node node : ast) {
            traversePostOrderSubtree(node, nodeProcessor);
        }
    }

    private static void traversePostOrderSubtree(final Node node, final NodeProcessor nodeProcessor) {
        if (node instanceof Parent) {
            for (final Node child : ((Parent)node).getChildren()) {
                traversePostOrderSubtree(child, nodeProcessor);
            }
        }

        nodeProcessor.processNode(node);
    }

    interface NodeProcessor {
        void processNode(Node node);
    }
}
