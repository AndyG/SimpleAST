package com.agarron.simpleast_core.node;

import java.util.List;

public interface Parent extends Node {
    public List<Node> getChildren();
    public void addChild(Node child);
}
