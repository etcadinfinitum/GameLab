package com.example.lizzy.gamelab;

import android.widget.Button;

/**
 * A custom ListNode class.
 */
public class ListNode {

    private ListNode next;
    private String letter;
    private Button button;

    /**
     * The constructor for the node object.
     * @param letter
     */
    public ListNode(String letter, Button button) {
        this.letter = letter;
        this.button = button;
        next = null;
    }

    /**
     * Gets the next node subsequent to the current object.
     * @return The next node in the list
     */
    public ListNode getNext() {
        return next;
    }

    /**
     * An accessor method to get the string associated with this particular node
     * @return The string associated with this node
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Breaks subsequent links to node objects, effectively rendering the existing node as the last node in the list
     * @return A boolean value indicating whether subsequent nodes were disassociated (true --> link was broken)
     */
    public boolean breakLink() {
        if (next == null) {
            return false;
        } else {
            next = null;
            return true;
        }
    }

    /**
     * Associates the current object's next pointer to the argument node.
     * @param node The new node being added to the list, subsequent to the current object
     * @return A boolean value indicating whether the link was established (true --> linked)
     */
    public boolean setLink(ListNode node) {
        next = node;
        return true;
    }

    /**
     * Returns the view button associated with this list node.
     * @return The Button object
     */
    public Button getButton() {
        return button;
    }

}
