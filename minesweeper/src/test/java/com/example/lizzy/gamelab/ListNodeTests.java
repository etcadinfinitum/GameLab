package com.example.lizzy.gamelab;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListNodeTests {


    @Test
    public void makeNewNode() {
        String someLetter = "dark was the night";
        android.widget.Button someButton = new android.widget.Button(null);
        ListNode theNode = new ListNode(someLetter, someButton);
        assertTrue(theNode != null);
        assertTrue(someLetter.equals(theNode.getLetter()));
        assertTrue(someButton.equals(theNode.getButton()));
    }

    @Test
    public void add2Nodes() {
        String someLetter = "dark was the night/ ";
        android.widget.Button someButton = new android.widget.Button(null);
        ListNode node1 = new ListNode(someLetter, someButton);

        String someLetter2 = "and his heart was frail";
        android.widget.Button someButton2 = new android.widget.Button(null);
        ListNode node2 = new ListNode(someLetter2, someButton2);

        node1.setLink(node2);

        assertTrue(node1.getNext() != null);
        assertTrue(node1.getNext() == node2);
    }

    @Test
    public void addSameNodes() {
        String someLetter = "dark was the night/ ";
        android.widget.Button someButton = new android.widget.Button(null);
        ListNode node1 = new ListNode(someLetter, someButton);
        /*
        String someLetter2 = "and his heart was frail";
        android.widget.Button someButton2 = new android.widget.Button(null);
        ListNode node2 = new ListNode(someLetter2, someButton2);
        */
        node1.setLink(node1);

        assertTrue(node1.getNext() != null);
    }

}
