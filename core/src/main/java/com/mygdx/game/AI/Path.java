package com.mygdx.game.AI;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * The path that exists between 2 nodes not bidirectional
 */
public class Path implements Connection<Node> {
    Node from;
    Node to;

    /**
     * Creates a path from F to T
     * @param f the start Node
     * @param t the end Node
     */
    public Path(Node f, Node t) {
        from = f;
        to = t;
    }

    /**
     * @return the cost of traveling the path
     */
    @Override
    public float getCost() {
        return to.cost;
    }

    /**
     * @return the start node of the path
     */
    @Override
    public Node getFromNode() {
        return from;
    }

    /**
     * @return the end node of the path
     */
    @Override
    public Node getToNode() {
        return to;
    }
}
