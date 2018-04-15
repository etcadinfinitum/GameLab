package com.example.lizzy.gamelab;

import android.view.View;

/**
 * A simple class for a queue node.
 */
public class QueueNode {

    private QueueNode next;
    private View view;

    /**
     * Constructor for a new queue node.
     * @param view The view being attached to this queue
     */
    public QueueNode(View view) {
        next = null;
        this.view = view;
    }

    /**
     * A utility method to cycle through the queue.
     * @return The next node in the queue
     */
    public QueueNode getNext() {
        return next;
    }

    /**
     * A utility method to link a new queue node to the node currently being referenced. The node
     * will only be linked if there are no other nodes linked to the referenced node.
     * @param node The node being linked to the end of the queue
     * @return A boolean value indicating whether the node was linked (true --> linked)
     */
    public boolean setNext(QueueNode node) {
        if (next == null) {
            next = node;
            return true;
        } else {
            return false;
        }
    }

    /**
     * A utility method to determine whether the referenced view is in the queue.
     * @param viewToCheck The view being investigated
     * @return A boolean value indicating whether this node's view is the same view being queried (true --> same view)
     */
    public boolean compareView(View viewToCheck) {
        if (viewToCheck.equals(view)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A utility method to retrieve the view associated with this node. Called by the game engine
     * so that the view's background modifications is the engine's responsibility.
     * @return The view corresponding to this QueueNode
     */
    public View getView() {
        return view;
    }

}
