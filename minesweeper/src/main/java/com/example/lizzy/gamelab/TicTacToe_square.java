package com.example.lizzy.gamelab;

/**
 * A simple class to store information about game cells for tic tac toe and provide accessors.
 * Created by lizzy on 3/26/18.
 */

public class TicTacToe_square {

    private CharSequence team;
    private boolean isClicked;

    /**
     * Constructor for class.
     */
    public TicTacToe_square() {
        isClicked = false;
        team = "";
    }

    /**
     * Set this cell as being selected and played.
     * @param team The designated "team" who picked the cell - either "X" or "O"
     */
    public void clicked(CharSequence team) {
        this.team = team;
        isClicked = true;
    }

    /**
     * Accessor to determine whether the cell has been played yet.
     * @return Boolean flag indicating whether the cell has been clicked yet - false means unplayed
     */
    public boolean isClicked() {
        return isClicked;
    }

    /**
     * Returns the designated team who picked the cell. Will return empty string if the method is called
     * but the cell has not been played by either team yet.
     * @return The string containing the name of the team which played that cell, if the cell has been played.
     */
    public CharSequence whichTeam() {
        return team;
    }

}
