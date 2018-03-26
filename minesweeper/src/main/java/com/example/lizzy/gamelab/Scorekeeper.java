package com.example.lizzy.gamelab;

import java.util.ArrayList;

/**
 * A game top score manager. Intended to automatically validate new high scores and add them to a sorted
 * ArrayList.
 * Created by lizzy on 3/24/18.
 */

public class Scorekeeper {

    private ArrayList<Score> minesweeperLocal = new ArrayList<Score>(5);

    /**
     * Parameterless constructor.
     */
    public Scorekeeper() {
        // deserialize score lists
    }

    /**
     * A method to validate whether the score of a successfully won game is a top score for the device.
     * @param game The name of the game, passed as a string.
     * @param score The score of the newly won game, to be tested against the top scores in the existing ArrayList.
     * @return A boolean flag indicating whether the score is a top score
     */
    public boolean checkNewTopScore(String game, int score) {
        boolean isTopScore = false;
        ArrayList<Score> gameType = getGameType(game);
        for (Score thisScore : gameType) {
            if (score >= thisScore.getScore()) {
                isTopScore = true;
            }
        }
        return isTopScore;
    }


    private ArrayList<Score> getGameType(String game) {
        ArrayList<Score> gameType;
        switch (game) {
            case "Minesweeper":
                gameType = minesweeperLocal;
                break;
            default:

                gameType = minesweeperLocal;
                break;
        }
        return gameType;
    }

    public boolean addTopScore(String game, Score newScore) {
        boolean isAdded = true;
        try {
            ArrayList<Score> modList = getGameType(game);
            for (Score oldScore : modList) {
                if (newScore.getScore() >= oldScore.getScore()) {
                    int newIdx = modList.indexOf(oldScore);
                    modList.add(newIdx, newScore);
                }
            }

        } catch (Exception e) {
            isAdded = false;
        }
        return isAdded;
    }

}
