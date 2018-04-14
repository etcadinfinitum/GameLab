package com.example.lizzy.gamelab;

import android.view.View;

import org.junit.Test;

import static org.junit.Assert.*;


public class QueueNodeTests {

    @Test
    public void testConstructor() {
        QueueNode newNode = new QueueNode(null);
        assertFalse(newNode == null);
    }


    @Test
    public void testViewRetriever() {
        View leView = new View(null);
        QueueNode newNode = new QueueNode(leView);
        // compare view in newNode to view created above
        assertTrue(newNode.compareView(leView));
    }

    @Test
    public void singleNodeGetNext() {
        QueueNode newNode = new QueueNode(null);
        // check to make sure a null value is returned for end of the queue
        assertNull(newNode.getNext());
    }

    @Test
    public void multNodesSetNext() {
        View leView = new View(null);
        View leView2 = new View(null);
        QueueNode newNode = new QueueNode(leView);
        QueueNode nextNode = new QueueNode(leView2);
        newNode.setNext(nextNode);
        assertTrue(newNode.getNext().equals(nextNode));
    }

}
