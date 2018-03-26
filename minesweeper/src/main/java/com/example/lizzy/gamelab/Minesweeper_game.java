package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import java.util.Observer;
import java.util.Observable;
import java.lang.Math;
import android.content.Intent;
import android.widget.Button;
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
    private int lastRowSelected = -1;
    private int lastColSelected = -1;
    private GameName gameType = GameName.MINESWEEPER;
    private float dpScale;
    private DisplayMetrics metrics;

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
            case "Medium":
                gameLevel = GameDifficulty.MEDIUM;
            case "Hard":
                gameLevel = GameDifficulty.HARD;
        }

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // store screen dimensions in a Point object, then find optimal dimension of buttons

        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        printScreenInfo();

        // setting preliminary values for board dimensions
        boardHeight = 10;
        boardWidth = 8;

        screenWidth = (screenWidth - (int) (2 * getResources().getDimension(R.dimen.activity_horizontal_margin)));
        screenHeight = (screenHeight - (int) (2 * getResources().getDimension(R.dimen.activity_horizontal_margin))
                - getActionBarHeight() - getStatusBarHeight());

        int buttonSize = (int) Math.floor(0.25 * metrics.xdpi);

        // determine minimum # of cells that can fit in the 1/4 inch dimension space
        boardHeight = screenHeight / buttonSize;
        boardWidth = screenWidth / buttonSize;

        int buttonSizeX = (int) Math.floor(screenWidth / boardWidth);
        int buttonSizeY = (int) Math.floor(screenHeight / boardHeight);

        // generate game model and button array with given board width & board height values
        model = new MinesweeperModel(this, boardHeight, boardWidth, gameLevel);
        theButtons = new Button[boardHeight][boardWidth];
        System.out.println("Creating button board with x, y dims: " + boardWidth + ", " + boardHeight + "; buttons have dims x, y:  " + buttonSizeX + ", " + buttonSizeY);

        // create a grid layout with proper xml dimensions determined by input/preset values
        GridLayout layout = (GridLayout) findViewById(R.id.button_grid);
        layout.setRowCount(theButtons.length);
        layout.setColumnCount(theButtons[0].length);

        // create buttons and add to gridlayout
        for (int row = 0; row < theButtons.length; row++) {
            for (int col = 0; col < theButtons[row].length; col++) {
                theButtons[row][col] = new Button(this);
                theButtons[row][col].setId(100 + (10 * row) + col);
                theButtons[row][col].setOnClickListener(move);
                theButtons[row][col].setBackgroundColor(getResources().getColor(R.color.newcell, null));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row, 1), GridLayout.spec(col, 1));
                params.width = buttonSizeX;
                params.height = buttonSizeY;
                layout.addView(theButtons[row][col], params);
            }
        }

    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getActionBarHeight() {
        int value = 0;
        if (getActionBar() != null) {
            value = getActionBar().getHeight();
        }
        return value;
    }

    private void printScreenInfo() {
        System.out.println( "'logical' density :" +  metrics.density);
        // density interms of dpi
        System.out.println( "'DPI' density :" +  metrics.densityDpi);
        // horizontal pixel resolution
        System.out.println( "width pix :" +  metrics.widthPixels);
        // vertical pixel resolution
        System.out.println( "height pix : " + metrics.heightPixels);
        // actual horizontal dpi
        System.out.println( "xdpi :" +  metrics.xdpi);
        // actual vertical dpi
        System.out.println( "ydpi :" +  metrics.ydpi);
        // width of 16DP section in this UI
        System.out.println("margin width : " + getResources().getDimension(R.dimen.activity_horizontal_margin));
    }

    View.OnClickListener move = new View.OnClickListener() {
        /**
         * Override onClick method for cells - defined by the makeMove method below
         * @param view The pressed cell which is initiating the move
         */
        @Override
        public void onClick(View view) {
            makeMove(view);
        }
    };

    /**
     * Triggers model to change board state. Called by the actionListener on each of the cells.
     * @param view The pressed cell to make the move on
     */
    private void makeMove(View view) {
        int buttonID = view.getId();
        // System.out.println("id is " + buttonID);
        int row = (buttonID - 100) / 10;
        int col = buttonID % 10;
        lastRowSelected = row;
        lastColSelected = col;
        // System.out.println("calling model's game state method with row " + row + " and col " + col);
        model.changeBoardState(row, col);
    }

    /**
     * The model-determined method to trigger the UI to update the board state.
     * @param gameState The observable-type Minesweeper_menu model (model returns the 'this' object)
     * @param someObject ???
     */
    public void update(Observable gameState, Object someObject) {
        GameSquare[][] boardState = ((MinesweeperModel) gameState).getBoardState();

        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                if (boardState[row][col].getClicked() && boardState[row][col].getNewClick())  {
                    int hint = boardState[row][col].getHint();
                    int color = 0;
                    CharSequence hintDisplay = "0";
                    switch (hint) {
                        case 1 : hintDisplay = "1";
                            color = getResources().getColor(R.color.ms1, null);
                            break;
                        case 2 : hintDisplay = "2";
                            color = getResources().getColor(R.color.ms2, null);
                            break;
                        case 3 : hintDisplay = "3";
                            color = getResources().getColor(R.color.ms3, null);
                            break;
                        case 4 : hintDisplay = "4";
                            color = getResources().getColor(R.color.ms4, null);
                            break;
                        case 5 : hintDisplay = "5";
                            color = getResources().getColor(R.color.ms5, null);
                            break;
                        case 6 : hintDisplay = "6";
                            color = getResources().getColor(R.color.ms6, null);
                            break;
                        case 7 : hintDisplay = "7";
                            color = getResources().getColor(R.color.ms7, null);
                            break;
                        case 8 : hintDisplay = "8";
                            color = getResources().getColor(R.color.ms8, null);
                            break;
                        default:
                            break;

                    }
                    theButtons[row][col].setText(hintDisplay);
                    theButtons[row][col].setTextColor(color);
                    if (boardState[row][col].getBomb()) {
                        theButtons[row][col].setText("M");
                        theButtons[row][col].setTextColor(getResources().getColor(R.color.ms7, null));
                    }
                    theButtons[row][col].setBackgroundColor(getResources().getColor(R.color.selected, null));
                    theButtons[row][col].setEnabled(false);
                    boardState[row][col].setOldClick();
                }


            }
        }
        if (! ((MinesweeperModel) gameState).getGameStatus()) {
            gameOver((MinesweeperModel) gameState);
        }
    }

    /**
     * Helper method to handle special board needs if game is over.
     * @param gameState The game model state
     */
    private void gameOver(MinesweeperModel gameState) {
        GameSquare[][] boardState = (gameState).getBoardState();
        // color cell of final move depending on game outcome - red if lost, green if won
        if (gameState.didUserWin())  {
            theButtons[lastRowSelected][lastColSelected].setBackgroundColor(getResources().getColor(R.color.winmove, null));
            Scorekeeper scores = new Scorekeeper();
        } else {
            theButtons[lastRowSelected][lastColSelected].setBackgroundColor(getResources().getColor(R.color.ms3, null));
        }

        // disable all actionlisteners on buttons
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                if (theButtons[row][col].isEnabled()) {
                    theButtons[row][col].setEnabled(false);
                }

            }
        }
    }

}
