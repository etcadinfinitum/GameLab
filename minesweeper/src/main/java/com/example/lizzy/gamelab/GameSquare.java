package com.example.lizzy.gamelab;

/**
 * A simple class which describes the traits of a tile in a Minesweeper_menu game.
 * This class type is only for modeling the game; there is no UI specs in this
 * class. All methods and variables are accessed by @MinewsweeperModel.java.
 * @author Lizzy Presland
 * @version GameLab 1.0
 */
public class GameSquare {

    private boolean isMine;
    private int mineHint;
    private boolean isSelected;
    private boolean isNewClick;

    /**
     * Constructor for a tile object. Initializes the square to be a blank tile,
     * with no gameplay.
     */
    public GameSquare() {
        isSelected = false;
        isNewClick = true;
        mineHint = 0;
    }

    /**
     * Used to alter the tile's state on game initialization; this method set's
     * the individual tile's status to be a mine if determined by the engine.
     * @param isBomb The boolean argument which sets the tile's mine state.
     */
    public void setBomb(boolean isBomb) {
        isMine = isBomb;
    }

    /**
     * Query's the tile's mine status. Complementary to setBomb(boolean isBomb).
     * @return The mine status of the tile (t being a mine)
     */
    public boolean getBomb() {
        return isMine;
    }

    /**
     * This method is called to increment the number value on the tile if the
     * tile is not a mine and neighbors a mine on the game board. This method
     * is strictly for game initialization.
     * @return Success status for the value increment
     */
    public boolean incrementHint() {
        mineHint++;
        return true;
    }

    /**
     * This method is called to retrieve the mineHint value during gameplay.
     * @return An integer value representing how many neighboring mines the tile has
     */
    public int getHint() {
        return mineHint;
    }

    /**
     * This method is called to indicate the tile has been selected and underlying
     * values should be displayed by the view.
     *
     */
    public void clicked() {
        isSelected = true;
    }

    /**
     * This method is called to retrieve the display status of the tile when the board
     * display needs to be altered.
     * @return A t/f flag indicating whether the tile should be displayed (t indicating display)
     */
    public boolean getClicked() {
        return isSelected;
    }

    /**
     * A method which indicates the cell's age of click status. Used to 
     * @return True if the cell status was just clicked, false if previously clicked
     */
    public boolean getNewClick() { 
        return isNewClick;
    }

    /**
     * 
     */
    public void setOldClick() {
        isNewClick = false;
    }
}
