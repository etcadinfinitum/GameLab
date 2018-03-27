package com.example.lizzy.gamelab;

import java.util.ArrayList;

/**
 * A game top score manager. Intended to automatically validate new high scores and add them to a sorted
 * ArrayList.
 * Created by lizzy on 3/24/18.
 */

public class Scorekeeper {

    private ArrayList<Score> minesweeperLocal = new ArrayList<Score>(5);
    private ArrayList<Score> tttLocal = new ArrayList<Score>(5);

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
    public boolean checkNewTopScore(GameName game, int score) {
        boolean isTopScore = false;
        ArrayList<Score> gameType = getGameType(game);
        if (gameType != null) {
            for (Score thisScore : gameType) {
                if (score >= thisScore.getScore()) {
                    isTopScore = true;
                }
            }
        }
        return isTopScore;
    }

    /**
     * Returns the appropriate ArrayList object for the game type being queried.
     * @param game The GameName enumerated value
     * @return The appropriate score array for the given game type
     */
    private ArrayList<Score> getGameType(GameName game) {
        ArrayList<Score> gameType;
        switch (game) {
            case MINESWEEPER:
                gameType = minesweeperLocal;
                break;
            case TICTACTOE:
                gameType = tttLocal;
                break;
            default:

                gameType = null;
                break;
        }
        return gameType;
    }

    /**
     * Adds score to
     * @param game
     * @param newScore
     * @return
     */
    public boolean addTopScore(GameName game, Score newScore) {
        boolean isAdded = false;
        try {
            ArrayList<Score> modList = getGameType(game);
            for (Score oldScore : modList) {
                if (!isAdded && newScore.getScore() >= oldScore.getScore()) {
                    int newIdx = modList.indexOf(oldScore);
                    modList.add(newIdx, newScore);
                    isAdded = true;
                }
            }

        } catch (Exception e) {
            isAdded = false;
        }
        return isAdded;
    }

}
