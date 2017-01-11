package com.agarron.simpleast_core

import com.agarron.simpleast_core.Node
import com.agarron.simpleast_core.Parent

class ASTUtils {
    companion object {
        @JvmStatic
        fun processTree(ast: Collection<Node>, nodeProcessor: (Node) -> Unit) {
            ast.forEach {
                processSubtree(it, nodeProcessor)
            }
        }

        private fun processSubtree(node: Node, nodeProcessor: (Node) -> Unit) {
            if (node is Parent) {
                node.getChildren().forEach { processSubtree(it, nodeProcessor) }
            }

            nodeProcessor.invoke(node)
        }
    }
}
