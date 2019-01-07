package experiment.gamelab;

import android.widget.Button;

/**
 * A custom linkedList implementation (singly linked), which handles searching for previously added nodes
 * which refer to the same button.
 */
public class LinkedList {

    private ListNode start;

    /**
     * Constructor for the new LinkedList. Creates an empty list.
     */
    public LinkedList() {
        start = null;
    }

    /**
     * A public method to add the next selected node in the list to the list.
     * @param letter The letter to be added next
     * @param button The button to be associated with the node
     * @return A boolean value indicating whether the node was added (true --> added, false --> already existed, broke link to subsequent nodes)
     */
    public boolean add(String letter, Button button) {
        boolean isAdded = false;
        ListNode existingNode = contains(button);
        if (existingNode == null) {
            ListNode newNode = new ListNode(letter, button);
            if (start != null) {
                ListNode currNode = start;
                while (currNode.getNext() != null) {
                    currNode = currNode.getNext();
                }
                currNode.setLink(newNode);
                isAdded = true;
            } else {
                start = newNode;
                isAdded = true;
            }
        } else {
            breakList(existingNode);
            isAdded = false;
        }
        return isAdded;
    }

    /**
     * A public method to set the argument node to be the last node.
     * @param node The node which should mark the end of the modified list
     * @return A boolean value indicating whether the list was successfully broken (true --> broken)
     */
    public boolean breakList(ListNode node) {
        return node.breakLink();
    }

    /**
     *
     * @param node
     * @return
     */
    public boolean breakListAtPreviousLink(ListNode node) {
        ListNode current = start;
        while (current != null) {
            if (current.getNext() == node) {
                current.breakLink();
                return true;
            } else {
                current = current.getNext();
            }
        }
        return false;
    }

    /**
     * A public method which determines if the argument button is already associated with an existing node
     * in the list. Returns null if not already associated (new selection, new node)
     * @param button The button clicked by the user
     * @return The ListNode associated with the provided button; returns null if not already associated
     */
    public ListNode contains(Button button) {
        ListNode existingNode = null;
        ListNode currNode = start;
        while (existingNode == null && currNode != null) {
            if (currNode.getButton().equals(button)) {
                existingNode = currNode;
            }
            currNode = currNode.getNext();
        }
        return existingNode;
    }

    /**
     * An accessor method to get the entire ordered string described by this linkedList.
     * @return The string represented by the linkedlist
     */
    public String getFullString() {
        String result = "";
        ListNode currNode = start;
        while (currNode != null) {
            result += currNode.getLetter();
            currNode = currNode.getNext();
        }
        return result;
    }

    /**
     * A helper method to get the last node in the list.
     * @return The last node in this list
     */
    public ListNode getLastNode() {
        ListNode currNode = start;
        if (currNode == null) {
            return null;
        } else {
            while (currNode.getNext() != null) {
                currNode = currNode.getNext();
            }
            return currNode;
        }
    }

    /**
     * A helper method to get the size of the list (ie the current number of linked nodes).
     * @return The number of nodes in the current list
     */
    public int size() {
        int size = 0;
        ListNode currNode = start;
        if (currNode == null) {
            return size;
        } else {
            size++;
            while (currNode.getNext() != null) {
                size++;
                currNode = currNode.getNext();
            }
            return size;
        }
    }

}
