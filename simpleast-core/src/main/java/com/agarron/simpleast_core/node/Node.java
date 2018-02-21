package com.agarron.simpleast_core.node;

import java.util.ArrayList;
import java.util.List;

public class Node {

  private List<Node> children;

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
