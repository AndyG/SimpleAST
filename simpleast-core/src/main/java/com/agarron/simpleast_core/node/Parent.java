package com.agarron.simpleast_core.node;

import java.util.List;

public interface Parent extends Node {
    List<Node> getChildren();
    void addChild(Node child);
}
