package com.agarron.simpleast_core.node

/**
 * Represents a single node in an Abstract Syntax Tree. It can (but does not need to) have children.
 */
open class Node {

  private var children: MutableList<Node>? = null

  fun getChildren(): List<Node>? = children

  fun hasChildren(): Boolean = children?.isNotEmpty() == true

  fun addChild(child: Node) {
    children = (children ?: ArrayList()).apply {
      add(child)
    }
  }
}
