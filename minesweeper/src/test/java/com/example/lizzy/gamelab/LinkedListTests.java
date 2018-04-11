package com.example.lizzy.gamelab;

import android.widget.Button;

import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListTests {

    @Test
    public void makeList() {
        LinkedList newList = new LinkedList();
        assertTrue(newList.getLastNode() == null);
    }

    @Test
    public void makeListWithNodes() {
        LinkedList newList = new LinkedList();
        newList.add("1", new Button(null));
        newList.add("2", new Button(null));
        newList.add("3", new Button(null));
        assertTrue(newList.size() == 3);
    }

    @Test
    public void checkForExistingNode() {
        LinkedList newList = new LinkedList();
        Button duplicateButton = new Button(null);
        newList.add("doesn't matter", duplicateButton);
        assertTrue(newList.contains(duplicateButton) != null);
    }


    @Test
    public void tryToAddSameButtonTwice() {
        LinkedList newList = new LinkedList();
        Button duplicateButton = new Button(null);
        newList.add("doesn't matter", duplicateButton);
        assertFalse(newList.add("the other thing", duplicateButton));
    }

    @Test
    public void checkStringConcatWorks() {
        LinkedList newList = new LinkedList();
        Button b1 = new Button(null);
        Button b2 = new Button(null);
        String s1 = "doesn't matter";
        String s2 = " i don't care";
        newList.add(s1, b1);
        newList.add(s2, b2);
        String bothStrings = s1 + s2;
        assertTrue(bothStrings.equals(newList.getFullString()));
    }

    @Test
    public void breakList() {
        LinkedList newList = new LinkedList();
        Button b1 = new Button(null);
        Button b2 = new Button(null);
        newList.add("1", b1);
        newList.add("2", b2);
        assertFalse(newList.add("3", b1));
    }

    @Test
    public void getLastNodeAfterListIsBroken() {
        LinkedList newList = new LinkedList();
        Button b1 = new Button(null);
        Button b2 = new Button(null);
        Button b3 = new Button(null);
        Button b4 = new Button(null);
        newList.add("4", b4);
        newList.add("1", b1);
        newList.add("2", b2);
        newList.add("3", b3);
        newList.add("1", b1);
        assertEquals(b1, newList.getLastNode().getButton());
        assertTrue(newList.size() == 2);
    }

}
