package com.agarron.simpleast_core.node;

import java.util.List;

public interface Parent<T extends Node> extends Node {
    List<T> getChildren();
    void addChild(T child);
}
