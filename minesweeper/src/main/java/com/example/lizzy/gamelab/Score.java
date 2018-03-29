package com.example.lizzy.gamelab;

import java.io.Serializable;

/**
 * A simple class to store the names and scores of game winners.
 * Created by lizzy on 3/24/18.
 */
public class Score implements Serializable {

    private String winnerName;
    private int score;
    private String difficulty;

    /**
     * A constructor for a Score object. Saves the high score as a self-contained record, to be added
     * to the corresponding ArrayList.
     * @param name The winner's name
     * @param score The winner's computed score
     * @param difficulty The difficulty of the game being played
     */
    public Score(String name, int score, String difficulty) {
        winnerName = name;
        this.score = score;
        this.difficulty = difficulty;
    }

    /**
     * An accessor method for the high score's player name.
     * @return The name of the individual who created the high score
     */
    public String getWinnerName() {
        return winnerName;
    }

    /**
     * An accessor method for the high score.
     * @return The recorded score in the game
     */
    public int getScore() {
        return score;
    }

    /**
     * An accessor method for the difficulty level for this high score.
     * @return The difficulty of the game, expressed as a string
     */
    public String getDifficulty() {
        return difficulty;
    }
}
