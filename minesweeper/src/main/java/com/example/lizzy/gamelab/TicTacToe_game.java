package com.example.lizzy.gamelab;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Button;
import android.view.View;
import android.graphics.Point;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.lang.Math;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * View/controller class for the tic-tac-toe game. Acts as an Observer to the corresponding model class.
 * References button objects and controls functionality flow.
 */
public class TicTacToe_game extends AppCompatActivity implements Observer {

    private DisplayMetrics metrics;
    private TicTacToe_model model;
    private int lastRowSelected = -1;
    private int lastColSelected = -1;
    Button[][] theButtons;
    private boolean Xturn;
    private Handler cpuMoveHandler;

    /**
     * Initializer for the tic-tac-toe board.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tic_tac_toe_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = new TicTacToe_model(this);

        theButtons = new Button[3][3];

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // printScreenInfo();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        screenHeight = (screenHeight - getActionBarHeight() - getStatusBarHeight() - getNavigationBarHeight());

        int buttonSize = optimizeButtonSizePixels(screenWidth, screenHeight);
        int marginSize = (int) Math.floor((screenWidth - (3 * buttonSize)) / 4);
        LinearLayout layout = (LinearLayout) findViewById(R.id.tictactoe_grid);

        for (int row = 0; row < theButtons.length; row++) {
            for (int col = 0; col < theButtons[row].length; col++ ) {
                int buttonID = getResources().getIdentifier("b" + Integer.toString(row) + Integer.toString(col), "id", getPackageName());
                Button thisButton = (Button) findViewById(buttonID);
                thisButton.setBackgroundColor(getResources().getColor(R.color.newcell, null));
                thisButton.setOnClickListener(move);
            }
        }
        Xturn = true;
        cpuMoveHandler = new Handler();
    }

    /**
     * Make a runnable object for the
     */
    Runnable cpuMove = new Runnable() {
        @Override
        public void run() {
            makeCPUMove();
        }
    };

    /**
     * Create a click listener for the buttons.
     */
    View.OnClickListener move = new View.OnClickListener() {
        @Override
        public void onClick (View moveButton) {
            int id = moveButton.getId();
            // System.out.println("button ID: " + id);
            String buttonName = getResources().getResourceEntryName(id);
            // System.out.println("button name: " + buttonName);
            buttonName = buttonName.substring(1);
            int buttonIdx = Integer.parseInt(buttonName);
            lastRowSelected = buttonIdx / 10;
            lastColSelected = buttonIdx % 10;
            model.move(lastRowSelected, lastColSelected, Xturn);
        }
    };

    /**
     * Helper method to determine optimal button/margin size.
     * @param screenWidth The measured width of the screen (in pixels)
     * @param screenHeight The measured content height of the screen (in pixels)
     * @return The determined dimension of the square game buttons in pixels
     */
    private int optimizeButtonSizePixels(int screenWidth, int screenHeight) {
        int buttonSize = 0;
        if (screenHeight > screenWidth) {
            buttonSize = (int) Math.floor(screenWidth/ 3.8);
        } else {
            buttonSize = (int) Math.floor(screenHeight / 3.8);
        }
        return buttonSize;
    }

    /**
     * Helper method to get height of system status bar (@ top of screen, with time, notifications, etc)
     * @return The height of the status bar, in pixels
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
        // System.out.println("action bar height is: " + value);
        return value;
    }

    /**
     * Helper method to get height of navigation bar at bottom of screen (has arrow, circle, square etc,
     * Android implementation of physical navigation buttons)
     * @return The height of the navigation bar, in pixels
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
     * Helper method to print information about device screen size & resources.
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
     * The method which is called by the observable-type object when changes have been made to the game's model.
     * The update method triggers the board to refresh/update
     * @param observe The model object to whom this class is an observer.
     * @param someObject Some silly object - required by observer interface
     */
    @Override
    public void update(Observable observe, Object someObject) {
        int buttonID = getResources().getIdentifier("b" + Integer.toString(lastRowSelected) + Integer.toString(lastColSelected), "id", getPackageName());
        Button thisButton = (Button) findViewById(buttonID);
        CharSequence hintDisplay = ((TicTacToe_model) observe).getSquare(lastRowSelected, lastColSelected).whichTeam();
        thisButton.setEnabled(false);
        thisButton.setBackgroundColor(getResources().getColor(R.color.selected, null));
        thisButton.setText(hintDisplay);
        thisButton.setTextColor(getResources().getColor(R.color.ms7, null));
        if (! model.isGameStillGoing()) {
            gameOver();
        } else {
            Xturn = !Xturn;
        }
        if (!Xturn) {
            cpuMoveHandler.postDelayed(cpuMove, 750);
        }
    }


    /**
     *
     */
    private void makeCPUMove() {
        ArrayList<Point> availableMoves = new ArrayList<>(12);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int thisButtonID = getResources().getIdentifier("b" + Integer.toString(row) + Integer.toString(col), "id", getPackageName());
                Button thisButton = (Button) findViewById(thisButtonID);
                if (thisButton.isEnabled()) {
                    availableMoves.add(new Point(row, col));
                }
            }
        }
        Random shuffle = new Random();
        int nextMove = shuffle.nextInt(availableMoves.size());
        System.out.println("created available moves list: " + availableMoves.toString());
        System.out.println("picked item with idx " + nextMove + "; x/y are " + availableMoves.get(nextMove).x + ", " + availableMoves.get(nextMove).y);
        lastRowSelected = availableMoves.get(nextMove).x;
        lastColSelected = availableMoves.get(nextMove).y;
        model.move(lastRowSelected, lastColSelected, Xturn);
    }

    /**
     * A helper method to disable all buttons when game is over.
     */
    private void removeAllEnabledButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int thisButtonID = getResources().getIdentifier("b" + Integer.toString(row) + Integer.toString(col), "id", getPackageName());
                Button thisButton = (Button) findViewById(thisButtonID);
                if (thisButton.isEnabled()) {
                    thisButton.setEnabled(false);
                }
            }
        }
    }

    /**
     * A helper method to consolidate the actions that need to be performed when the game is over.
     */
    private void gameOver() {
        removeAllEnabledButtons();
        Point[] winningMoves = model.getWinningSquares();
        for (int i = 0; i < winningMoves.length; i++) {
            if (winningMoves[i] != null) {
                int winButtonID = getResources().getIdentifier("b" + Integer.toString(winningMoves[i].x) + Integer.toString(winningMoves[i].y), "id", getPackageName());
                Button winButton = (Button) findViewById(winButtonID);
                winButton.setBackgroundColor(getResources().getColor(R.color.winmove, null));
            }
        }

        // win/loss dialogs
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setNegativeButton("Back to Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent menuIntent = new Intent(getApplicationContext(), Menu.class);
                menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menuIntent);
            }
        });

        dialog.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent newGameIntent = new Intent(getApplicationContext(), TicTacToe_game.class);
                newGameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newGameIntent);
            }
        });

        if (!model.isGameStillGoing() && model.isStalemate()) {
            dialog.setCancelable(true);
            dialog.setTitle("Stalemate!");
            dialog.setMessage(R.string.stalemate_message);
            dialog.show();
        } else if (!model.isGameStillGoing() && model.didXwin()) {
            dialog.setCancelable(true);
            dialog.setTitle("X won!");
            dialog.setMessage(R.string.ttt_x_won);
            dialog.show();
        } else if (!model.isGameStillGoing() && !model.didXwin()) {
            dialog.setCancelable(true);
            dialog.setTitle("O won!");
            dialog.setMessage(R.string.ttt_o_won);
            dialog.show();
        }
    }


}
