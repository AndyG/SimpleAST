package com.agarron.simpleast_core.node;

import java.util.ArrayList;
import java.util.List;

public class Node {

  public final String type;

  private List<Node> children;

  public Node(final String type) {
    this.type = type;
  }

  public List<Node> getChildren() {
    return children;
  }

  public boolean hasChildren() {
    return children != null && !children.isEmpty();
  }

  public void addChild(final Node child) {
    if (children == null) {
      children = new ArrayList<>();
    }

    children.add(child);
  }
}
