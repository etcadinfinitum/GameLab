package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Observer;
import java.util.Observable;
import android.content.Intent;
import android.widget.Button;
import android.graphics.Point;
import android.widget.GridLayout;

/**
 * View/controller class for the minesweeper game. Acts as an Observer to the model class.
 * References button objects and controls functionality flow.
 */
public class Minesweeper_game extends AppCompatActivity implements Observer {

    protected GameDifficulty gameLevel = GameDifficulty.DUMMY;
    public Button[][] theButtons;
    private int boardHeight = 0;
    private int boardWidth = 0;
    private MinesweeperModel model;

    /**
     * The initializer for the minesweeper board.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper_game);
        Intent intent = getIntent();
        String level = intent.getStringExtra("Level");
        switch (level) {
            case "Easy":
                gameLevel = GameDifficulty.EASY;
                boardHeight = 10;
                boardWidth = 8;
            case "Medium":
                gameLevel = GameDifficulty.MEDIUM;
                boardHeight = 10;
                boardWidth = 8;
            case "Hard":
                gameLevel = GameDifficulty.HARD;
                boardHeight = 10;
                boardWidth = 8;
        }
        model = new MinesweeperModel(this, 10, 8, gameLevel);
        theButtons = new Button[8][10];

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int buttonSize = 0;
        if (screenWidth / 8 > screenHeight / 10) {
            buttonSize = (int) screenHeight / 10;
        } else {
            buttonSize = (int) screenWidth / 8;
        }
        // create a grid layout with proper xml dimensions determined by input/preset values
        GridLayout layout = (GridLayout) findViewById(R.id.button_grid);
        layout.setRowCount(theButtons.length);
        layout.setColumnCount(theButtons[0].length);
        GridLayout.LayoutParams cellSize = new GridLayout.LayoutParams();

        // create buttons and add to gridlayout
        for (int row = 0; row < theButtons.length; row++) {
            for (int col = 0; col < theButtons[row].length; col++) {
                theButtons[row][col] = new Button(this);
                theButtons[row][col].setId(100 + (10 * row) + col);
                theButtons[row][col].setOnClickListener(move);
                layout.addView(theButtons[row][col], new GridLayout.LayoutParams(GridLayout.spec(row, 1, GridLayout.CENTER), GridLayout.spec(col, 1, GridLayout.CENTER)));
            }
        }

    }

    View.OnClickListener move = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            makeMove(view);
        }
    };

    private void makeMove(View view) {
        int buttonID = view.getId();
        System.out.println("id is " + buttonID);
        int row = (buttonID - 100) / 10;
        int col = buttonID % 10;
        System.out.println("calling model's game state method with row " + row + " and col " + col);
        model.changeBoardState(row, col);
    }

    /**
     * The model-determined method to trigger the UI to update the board state.
     * @param gameState The observable-type Minesweeper model (model returns the this object)
     * @param someObject ???
     */
    public void update(Observable gameState, Object someObject) {
        GameSquare[][] boardState = ((MinesweeperModel) gameState).getBoardState();
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                if (boardState[row][col].getClicked()) {
                    // should this be a string id with colors etc?
                    theButtons[row][col].setText(boardState[row][col].getHint());
                    if (boardState[row][col].getBomb()) {
                        theButtons[row][col].setText("M");
                    }
                    theButtons[row][col].setEnabled(false);
                }

            }
        }

    }
}
