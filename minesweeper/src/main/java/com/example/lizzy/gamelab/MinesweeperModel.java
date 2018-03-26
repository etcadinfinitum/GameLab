package com.example.lizzy.gamelab;

import java.util.Observable;
import java.util.Observer;

/**
 * A class which dictates the functionality of the Minesweeper_menu game engine.
 */
public class MinesweeperModel extends Observable {


    private GameSquare[][] squareTraits;

    // private variables
    private boolean activeGame;
    private boolean didUserWinGame;

    /**
     * Constructor for game logic. Initializes board with
     * @param controls POSSIBLY NOT NEEDED??
     * @param boardHeight dimensions of array (designed for Y-axis display)
     * @param boardWidth dimensions of array (designed for X-axis display)
     * @param diff The user-specified game difficulty setting (enum value GameDifficulty)
     */
    public MinesweeperModel(Observer controls, int boardHeight, int boardWidth, GameDifficulty diff) {
        super();
        addObserver(controls);
        activeGame = true;
        squareTraits = new GameSquare[boardHeight][boardWidth];
        for (int row = 0; row < squareTraits.length; row++) {
            for (int col = 0; col < squareTraits[row].length; col++) {
                squareTraits[row][col] = new GameSquare();
            }
        }
        determineSegmentMines(diff);
        determineSegmentValue();
        setChanged();
        notifyObservers(this);
    }

    /**
     * Helper method; randomly assigns mines to game cells.
     * @param diff The user-specified level of gameplay difficulty
     */
    private void determineSegmentMines(GameDifficulty diff) {
        // get board dimensions, determine number of mines to select
        int mineQty = squareTraits.length * squareTraits[0].length;
        switch (diff) {
            case EASY: mineQty /= 10;
                break;
            case MEDIUM: mineQty /= 8;
                break;
            case HARD: mineQty /= 6;
                break;
            case DUMMY: mineQty = 1;
                break;
            case CUSTOM: mineQty /= 4;
                    // need to work in user-specified dimensions
                break;
        }

        //
        for (int i = 1; i <= mineQty; i++) {
            boolean isValid = false;
            while (!isValid) {
                int mineX = (int)Math.floor(Math.random() * squareTraits.length);
                int mineY = (int)Math.floor(Math.random() * squareTraits[0].length);
                if (!squareTraits[mineX][mineY].getBomb()) {
                    squareTraits[mineX][mineY].setBomb(true);
                    isValid = true;
                }
            }
        }

    }

    /**
     * Helper method; calculates board's numeric values.
     */
    private void determineSegmentValue() {
        for (int row = 0; row < squareTraits.length; row++) {
            for (int col = 0; col < squareTraits[row].length; col++) {
                if (squareTraits[row][col].getBomb()) {
                    increment(row, col, 0);
                }
            }
        }
    }

    /**
     * Helper method to locate new minefield cells, then recursively walk the board to increment
     * the surrounding cells' hint value. Called exclusively by determineSegmentValue.
     * @param currX
     * @param currY
     * @param reps
     */
    private void increment(int currX, int currY, int reps) {
        if (currX < 0 || currY < 0 || currX >= squareTraits.length ||
                currY >= squareTraits[0].length || reps > 1) {

        } else {
            if (reps > 0) {
                squareTraits[currX][currY].incrementHint();
            }
            reps++;
            increment(currX + 1, currY + 1, reps);
            increment(currX - 1, currY + 1, reps);
            increment(currX + 1, currY - 1, reps);
            increment(currX - 1, currY - 1, reps);
            increment(currX - 1, currY, reps);
            increment(currX + 1, currY, reps);
            increment(currX, currY + 1, reps);
            increment(currX, currY - 1, reps);
        }
    }

    /**
     * How to handle a user's move.
     * @param buttonX x index of pressed button
     * @param buttonY y index of pressed button
     */
    public void changeBoardState(int buttonX, int buttonY) {
        if (squareTraits[buttonX][buttonY].getBomb()) {
            activeGame = false;
            for (int row = 0; row < squareTraits.length; row++) {
                for (int col = 0; col < squareTraits[row].length; col++) {
                    if (squareTraits[row][col].getBomb()) {
                        squareTraits[row][col].clicked();
                    }
                }
            }
        } else {
            uncoverSquares(buttonX, buttonY);
        }
        isGameOver();
        setChanged();
        notifyObservers(this);
    }

    /**
     * Recursion helper method for uncovering clicked cells
     * @param currX
     * @param currY
     */
    private void uncoverSquares(int currX, int currY) {
        if (currX < 0 || currY < 0 || currX >= squareTraits.length ||
                currY >= squareTraits[0].length || squareTraits[currX][currY].getClicked()) {
            // do nothing - resolve recursive call
        } else if (squareTraits[currX][currY].getHint() != 0) {
            squareTraits[currX][currY].clicked();
        } else {
            squareTraits[currX][currY].clicked();
            uncoverSquares(currX + 1, currY);
            uncoverSquares(currX, currY + 1);
            uncoverSquares(currX + 1, currY + 1);
            uncoverSquares(currX + 1, currY - 1);
            uncoverSquares(currX - 1, currY);
            uncoverSquares(currX, currY - 1);
            uncoverSquares(currX - 1, currY - 1);
            uncoverSquares(currX - 1, currY + 1);
        }
    }


    private void isGameOver() {
        if (activeGame) {
            boolean gameWon = true;
            for (int row = 0; row < squareTraits.length; row++) {
                for (int col = 0; col < squareTraits[row].length; col++) {
                    if (!squareTraits[row][col].getClicked() && !squareTraits[row][col].getBomb()) {
                        gameWon = false;
                    }
                }
            }
            if (gameWon) {
                didUserWinGame = true;
                activeGame = false;
            }
        } else {
            didUserWinGame = false;
        }

    }

    /**
     * Allows controller/view requests for gameboard state
     * @return The instance array of board cells containing respective traits
     */
    public GameSquare[][] getBoardState() {
        return squareTraits;
    }

    /**
     * Indicates whether the game is still in progress (if returns true, the user
     * has not won or lost yet).
     * @return A T/F flag indicating whether the game is still active
     */
    public boolean getGameStatus() {
        return activeGame;
    }

    /**
     * If getGameStatus() returns true, this method will be invoked. The method
     * returns a T/F value indicating whether the user won or lost the game.
     * @return A T/F flag indicating whether the user won the game
     */
    public boolean didUserWin() {
        return didUserWinGame;
    }

}
