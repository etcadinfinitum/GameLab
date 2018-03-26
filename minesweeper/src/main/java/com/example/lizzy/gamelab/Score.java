package com.example.lizzy.gamelab;

import java.io.Serializable;

/**
 * A simple class to store the names and scores of game winners.
 * Created by lizzy on 3/24/18.
 */
public class Score implements Serializable {

    private String winnerName;
    private int score;

    public Score(String name, int score) {
        winnerName = name;
        this.score = score;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public int getScore() {
        return score;
    }

}
