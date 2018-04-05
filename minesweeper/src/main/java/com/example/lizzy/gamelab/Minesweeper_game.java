package com.example.lizzy.gamelab;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import java.util.Observer;
import java.util.Observable;
import java.lang.Math;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

/**
 * View/controller class for the minesweeper game. Acts as an Observer to the model class.
 * References button objects and controls functionality flow.
 */
public class Minesweeper_game extends AppCompatActivity implements Observer {

    protected GameDifficulty gameLevel = GameDifficulty.DUMMY;
    private String gameLevelString = "";
    private Button[][] theButtons;
    private int boardHeight = 0;
    private int boardWidth = 0;
    private MinesweeperModel model;
    private int lastRowSelected = -1;
    private int lastColSelected = -1;
    private GameName gameType = GameName.MINESWEEPER;
    private float dpScale;
    private DisplayMetrics metrics;
    private long startTime = 0;
    private long endTime = 0;
    private long scoreTime = 0;
    private Scorekeeper scores;
    private EditText winnerName;
    private boolean addNewScore = false;
    private int newScoreVal = 0;

    /**
     * The initializer for the minesweeper board.
     * @param savedInstanceState The activity-trigger instance
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
                break;
            case "Medium":
                gameLevel = GameDifficulty.MEDIUM;
                break;
            case "Hard":
                gameLevel = GameDifficulty.HARD;
                break;
            default:
                gameLevel = GameDifficulty.DUMMY;
                break;
        }
        gameLevelString = level;

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
                - getActionBarHeight() - getStatusBarHeight() - getNavigationBarHeight());

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
        GridLayout layout = (GridLayout) findViewById(R.id.minesweeper_button_grid);
        layout.setRowCount(theButtons.length);
        layout.setColumnCount(theButtons[0].length);

        // create buttons and add to gridlayout
        for (int row = 0; row < theButtons.length; row++) {
            for (int col = 0; col < theButtons[row].length; col++) {
                theButtons[row][col] = new Button(this);
                theButtons[row][col].setId((1000 * row) + col);
                theButtons[row][col].setOnClickListener(move);
                theButtons[row][col].setBackgroundColor(getResources().getColor(R.color.newcell, null));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row, 1), GridLayout.spec(col, 1));
                params.width = buttonSizeX;
                params.height = buttonSizeY;
                layout.addView(theButtons[row][col], params);
            }
        }

    }

    /**
     * A helper method to get the height of the system status bar.
     * @return The height of the status bar in pixels
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * A helper method to get the height of the application's action bar.
     * @return The height of the action bar in pixels
     */
    private int getActionBarHeight() {
        int value = 0;
        if (getActionBar() != null) {
            value = getActionBar().getHeight();
        } else {
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                value = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
        }
        System.out.println("action bar height is: " + value);
        return value;
    }

    /**
     * A helper method to get the height of the system navigation bar.
     * @return The height of the navigation bar in pixels
     */
    private int getNavigationBarHeight() {
        int value = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            value = getResources().getDimensionPixelSize(resourceId);
        }
        return value;
    }

    /**
     * A helper method to print the system screen dimensions to the console.
     */
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

    /**
     * Declare the clickListener object for new moves.
     */
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
        if (lastRowSelected == -1) {
            startTime = System.currentTimeMillis();
        }
        int buttonID = view.getId();
        // System.out.println("id is " + buttonID);
        int row = (buttonID) / 1000;
        int col = buttonID % 1000;
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
            endTime = System.currentTimeMillis();
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

        // add score if appropriate
        if (gameState.didUserWin()) {
            scores = new Scorekeeper(this);
            scoreTime = (endTime - startTime) / 1000;
            int multiplier = 0;
            switch (gameLevel) {
                case EASY:
                    multiplier = 1;
                    break;
                case MEDIUM:
                    multiplier = 2;
                    break;
                case HARD:
                    multiplier = 3;
                    break;
                default:
                    multiplier = 1;
                    break;
            }
            newScoreVal = (int) Math.floor(gameState.getMineQuant() * multiplier * 40 / scoreTime);
            if (scores.checkNewTopScore(gameType, newScoreVal)) {
                addNewScore = true;
                System.out.println("New top score registered - instantiating the textbox for winner name");
                winnerName = new EditText(this);
                winnerName.setHint(R.string.anon);
            }
        }

        // add popup indicating win/loss
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setNegativeButton("Back to Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (addNewScore) {
                    Score newScore = new Score(winnerName.getText().toString(), newScoreVal, gameLevelString);
                    scores.addTopScore(gameType, newScore);
                    scores = null;
                }
                Intent menuIntent = new Intent(getApplicationContext(), Menu.class);
                menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menuIntent);
            }
        });

        dialog.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (addNewScore) {
                    Score newScore = new Score(winnerName.getText().toString(), newScoreVal, gameLevelString);
                    scores.addTopScore(gameType, newScore);
                    scores = null;
                }
                Intent newGameIntent = new Intent(getApplicationContext(), Minesweeper_game.class);
                newGameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                while (newGameIntent.getExtras() != null) {
                    System.out.println("Extras are currently " + newGameIntent.getExtras() + "; removing...");
                    newGameIntent.removeExtra("Level");
                    System.out.println("Extras are currently " + newGameIntent.getExtras() + "; removing...");
                }
                newGameIntent.putExtra("Level", gameLevelString);
                startActivity(newGameIntent);
            }
        });
        if (addNewScore) {
            dialog.setCancelable(true);
            dialog.setTitle("You won!");
            dialog.setMessage(getString(R.string.win_message) + " " + getString(R.string.top_score));
            dialog.setView(winnerName);
            dialog.show();
        } else if (!gameState.getGameStatus() && gameState.didUserWin()) {
            dialog.setCancelable(true);
            dialog.setTitle("You won!");
            dialog.setMessage(R.string.win_message);
            dialog.setView(winnerName);
            dialog.show();
        } else if (!gameState.getGameStatus()) {
            dialog.setCancelable(true);
            dialog.setTitle("You lost!");
            dialog.setMessage(R.string.loss_message);
            dialog.show();
        }

    }

}
