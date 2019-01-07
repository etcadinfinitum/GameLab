package experiment.gamelab;

import android.view.View;

/**
 * A basic implementation of a queue class to easily manage view movements.
 */
public class Queue {

    private QueueNode first;
    private QueueNode last;

    /**
     * A simple constructor for the class.
     */
    public Queue() {
        first = null;
        last = null;
    }

    /**
     * A utility to add new a new view to the existing queue.
     * @param view The view being added
     * @return A boolean value indicating the new view was added to the queue (true --> added)
     */
    public boolean push(View view) {
        if (first == null) {
            first = new QueueNode(view);
            last = first;
            return true;
        } else {
            QueueNode newNode = new QueueNode(view);
            last.setNext(newNode);
            last = newNode;
            return true;
        }
    }

    /**
     * A utility method to determine if the queue already contains the specified view.
     * @param view The view being investigated
     * @return A boolean value indicating whether the view is already in the queue (true --> exists)
     */
    public boolean contains(View view) {
        if (first == null) {
            return false;
        } else {
            QueueNode currNode = first;
            boolean found = false;
            while (currNode.getNext() != null) {
                if (currNode.compareView(view)) {
                    found = true;
                    break;
                }
                currNode = currNode.getNext();
            }
            if (currNode.compareView(view)) {
                found = true;
            }
            return found;
        }

    }

    /**
     * A method to remove the last queue item.
     * @return The view that was just popped off the end of the list.
     */
    public View pop() {
        if (first == last) {
            first = null;
            last = null;
            return null;
        } else {
            QueueNode newNext = first.getNext();
            View oldView = first.getView();
            first = newNext;
            return oldView;
        }
    }

}
