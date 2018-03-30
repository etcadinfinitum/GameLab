package com.example.lizzy.gamelab;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * A game engine class for Boggle. Will generate the boggle dice configuration for the game (only selecting
 * sides, random placement of the dice will be handled by the game's view). Also validates the user's
 * word selection when they make a move.
 */
public class Boggle_Model extends Observable {

    String[] thisBoard = new String[16];

    /**
     * The constructor for the game model. Adds the game view class as an observer, determines the
     * @param game
     */
    public Boggle_Model(Observer game) {
        super();
        addObserver(game);
        generateDice();

        setChanged();
        notifyObservers();
    }

    /**
     * A helper method which generates an array representation of each legal die in the English-language
     * version of Boggle, then randomly chooses one element from each die array and
     */
    private void generateDice() {

        String[] die0 = new String[]{"R", "I", "F", "O", "B", "X"};
        String[] die1 = new String[]{"I", "F", "E", "H", "E", "Y"};
        String[] die2 = new String[]{"D", "E", "N", "O", "W", "S"};
        String[] die3 = new String[]{"U", "T", "O", "K", "N", "D"};
        String[] die4 = new String[]{"H", "M", "S", "R", "A", "O"};
        String[] die5 = new String[]{"L", "U", "P", "E", "T", "S"};
        String[] die6 = new String[]{"A", "C", "I", "T", "O", "A"};
        String[] die7 = new String[]{"Y", "L", "G", "K", "U", "E"};
        String[] die8 = new String[]{"Qu", "B", "M", "J", "O", "A"};
        String[] die9 = new String[]{"E", "H", "I", "S", "P", "N"};
        String[] die10 = new String[]{"V", "E", "T", "I", "G", "N"};
        String[] die11 = new String[]{"B", "A", "L", "I", "Y", "T"};
        String[] die12 = new String[]{"E", "Z", "A", "V", "N", "D"};
        String[] die13 = new String[]{"R", "A", "L", "E", "S", "C"};
        String[] die14 = new String[]{"U", "W", "I", "L", "R", "G"};
        String[] die15 = new String[]{"P", "A", "C", "E", "M", "D"};

        ArrayList<String[]> theDice = new ArrayList<>(16);
        theDice.add(die0);
        theDice.add(die1);
        theDice.add(die2);
        theDice.add(die3);
        theDice.add(die4);
        theDice.add(die5);
        theDice.add(die6);
        theDice.add(die7);
        theDice.add(die8);
        theDice.add(die9);
        theDice.add(die10);
        theDice.add(die11);
        theDice.add(die12);
        theDice.add(die13);
        theDice.add(die14);
        theDice.add(die15);
        theDice.trimToSize();

        for (int i = 0; i < 16; i++) {
            Random dieRoll = new Random();
            int idx = dieRoll.nextInt(6);
            thisBoard[i] = (theDice.get(i))[idx];
        }

    }

    /**
     * A method which is going to be invoked by the game view, which will provide a new word input by
     * the user and determine whether it belongs to the dictionary.
     * @param newWord
     * @return
     */
    public boolean makeMove(LinkedList<String> newWord) {
        long wordValue = convertWordToLong(newWord);
        return validateWord(wordValue);
    }

    /**
     * A helper method intended to convert the user's input into a more condensed format that can
     * be compared against the dictionary dataset for inclusion. Implementation is tentatively anticipated
     * as a string-to-long conversion as described here: https://stackoverflow.com/questions/2276641/way-to-store-a-large-dictionary-with-low-memory-footprint-fast-lookups-on-and
     * @param word The set of characters that the user selected on the board, expressed as a LinkedList (for now)
     * @return The representation of the user's word as a long type (for use in the implementation linked above
     */
    private long convertWordToLong(LinkedList<String> word) {
        long wordValue = 0;
        if (word.size() > 12) {
            // use 13+ character array to validate word
            String wordString = "";
            for (String element : word) {
                wordString += element;
            }
        } else {
            // use long value array
        }
        return wordValue;
    }

    private boolean validateWord(long wordValue) {

        return true;
    }

    /**
     * An accessor method to get the die roll configuration for each individual die. The dice are placed
     * in order; the view will have to randomly arrange the dice on the board for true randomness.
     * @return The string array containing the letters that will be displayed for this game
     */
    public String[] getThisBoard() {
        return thisBoard;
    }

}
