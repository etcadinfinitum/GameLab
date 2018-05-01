package com.example.lizzy.gamelab;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class to manage UI and game play inputs for Snake game implementation.
 */
public class Snake_Game extends AppCompatActivity {

    private GridLayout snakeGrid;
    private int gridWidth;
    private int gridHeight;
    private View[][] gridCells;
    private int actualCellWidth;
    private int actualCellHeight;
    private Queue snake;
    private int currX;
    private int currY;
    private SnakeDirection direction;
    private Handler moveHandler;
    private int appleCount = 0;
    private int timerDelay = 500;
    private ArrayList<View> apples = new ArrayList<>(4);
    private EditText winnerName;
    private GestureDetector detector;
    private Scorekeeper scores;

    /**
     * The initializer for the activity.
     * @param savedInstanceState The activity-trigger instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_snake_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        snakeGrid = (GridLayout) findViewById(R.id.snake_grid);
        snakeGrid.post(new Runnable() {
            @Override
            public void run() {
                gridWidth = snakeGrid.getWidth();
                gridHeight = snakeGrid.getHeight();
                measureChildren();
                gridCells = new View[snakeGrid.getRowCount()][snakeGrid.getColumnCount()];
                placeChildren();
                makeSnake();
                makeApple();
                makeApple();
                makeApple();
                makeApple();
                setUpListeners.run();
                moveSnake.run();
            }
        });

        moveHandler = new Handler();
    }

    /**
     * A cleanup method to dispose of the runnable thread that is handling the snake's moves.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        moveHandler.removeCallbacks(moveSnake);
    }

    /**
     * The runnable object that manages threads and posts new messages to the handler.
     */
    Runnable moveSnake = new Runnable() {
        @Override
        public void run() {
            boolean goAgain = makeNextMove();
            if (goAgain) {
                moveHandler.postDelayed(moveSnake, timerDelay);
            } else {
                makeDialog();
            }
        }
    };

    Runnable setUpListeners = new Runnable() {
        @Override
        public void run() {
            GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent event) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
                    if (Math.abs(velocityX) > Math.abs(velocityY)) {
                        System.out.println("detected x velocity " + velocityX + " y velocity " + velocityY + "; left-right gesture detected");
                        if (velocityX > 0) {
                            System.out.println("detected x velocity " + velocityX + "; calling setDirectionRight");
                            setDirectionRight(snakeGrid);
                        } else {
                            System.out.println("detected x velocity " + velocityX + "; calling setDirectionLeft");
                            setDirectionLeft(snakeGrid);
                        }
                    } else {
                        System.out.println("detected x velocity " + velocityX + " y velocity " + velocityY + "; up-down gesture detected");
                        if (velocityY > 0) {
                            System.out.println("detected y velocity " + velocityY + "; calling setDirectionDown");
                            setDirectionDown(snakeGrid);
                        } else {
                            System.out.println("detected y velocity " + velocityY + "; calling setDirectionUp");
                            setDirectionUp(snakeGrid);
                        }
                    }
                    return true;
                }
            };

            detector = new GestureDetector(getApplicationContext(), gestureListener);

            snakeGrid.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return detector.onTouchEvent(event);
                }
            });
        }
    };



    /**
     * A helper method to dynamically determine size of the snake's grid and corresponding grid cells.
     * All pertinent results are stored in instance variables for access by the placeChildren() method.
     */
    private void measureChildren() {
        int optimalCellSizeDP = 20;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int optimalCellSize = (int) Math.floor(metrics.density * optimalCellSizeDP);
        int colCount = gridWidth / optimalCellSize;
        int rowCount = gridHeight / optimalCellSize;
        snakeGrid.setColumnCount(colCount);
        snakeGrid.setRowCount(rowCount);
        actualCellWidth = gridWidth / colCount;
        actualCellHeight = gridHeight / rowCount;
    }

    /**
     * A helper method to instantiate the grid's underlying views, dictate formatting of the views, and
     * repaint the activity canvas.
     */
    private void placeChildren() {
        int cellCount = snakeGrid.getColumnCount() * snakeGrid.getRowCount();
        int row;
        int col;
        for (int i = 0; i < cellCount; i++) {
            row = i / snakeGrid.getColumnCount();
            col = i % snakeGrid.getColumnCount();
            gridCells[row][col] = new View(this);
            gridCells[row][col].setBackground(getResources().getDrawable(R.drawable.ms_button_new, null));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row,1), GridLayout.spec(col,1));
            params.width = actualCellWidth;
            params.height = actualCellHeight;
            snakeGrid.addView(gridCells[row][col], params);
        }
    }

    /**
     * A helper method to initialize the snake and generate a default configuration of ~4 grid cells
     * (subject to change and/or user input).
     */
    private void makeSnake() {
        snake = new Queue();
        snake.push(gridCells[1][1]);
        snake.push(gridCells[2][1]);
        snake.push(gridCells[2][2]);
        snake.push(gridCells[2][3]);
        gridCells[1][1].setBackground(getResources().getDrawable(R.drawable.snake_snake, null));
        gridCells[2][1].setBackground(getResources().getDrawable(R.drawable.snake_snake, null));
        gridCells[2][2].setBackground(getResources().getDrawable(R.drawable.snake_snake, null));
        gridCells[2][3].setBackground(getResources().getDrawable(R.drawable.snake_snake, null));
        currX = 3;
        currY = 2;
        direction = SnakeDirection.RIGHT;
    }

    /**
     * A helper method to dynamically generate the next apple for the snake to pursue.
     */
    private void makeApple() {
        boolean emptyView = false;
        Random random = new Random();
        while (!emptyView) {
            int row = random.nextInt(snakeGrid.getRowCount());
            int col = random.nextInt(snakeGrid.getColumnCount());
            if (!snake.contains(gridCells[row][col]) && !apples.contains(gridCells[row][col])) {
                emptyView = true;
                apples.add(gridCells[row][col]);
                gridCells[row][col].setBackground(getResources().getDrawable(R.drawable.snake_apple, null));
            }
        }
    }

    /**
     * A helper method to make the next move on the board.
     */
    private boolean makeNextMove() {
        switch (direction) {
            case UP:    currY--;    break;
            case DOWN:  currY++;    break;
            case LEFT:  currX--;    break;
            case RIGHT: currX++;    break;
        }
        if (currX < 0 || currX >= snakeGrid.getColumnCount() || currY < 0 || currY >= snakeGrid.getRowCount() ||
                snake.contains(gridCells[currY][currX])) {
            return false;
        } else if (apples.contains(gridCells[currY][currX])) {
            eatApple();
            return true;
        } else {
            snake.push(gridCells[currY][currX]);
            gridCells[currY][currX].setBackground(getResources().getDrawable(R.drawable.snake_snake, null));
            View oldTail = snake.pop();
            oldTail.setBackground(getResources().getDrawable(R.drawable.ms_button_new, null));
            return true;
        }
    }

    /**
     * A helper method to lengthen snake and place a new apple. Only called when current new coordinates
     * of snake overlaps the coordinates of current apple.
     */
    private void eatApple() {
        appleCount++;
        if (appleCount % 5 == 0 && timerDelay > 100) {
            timerDelay = (int) Math.floor(timerDelay * 0.8);
        }
        snake.push(gridCells[currY][currX]);
        gridCells[currY][currX].setBackground(getResources().getDrawable(R.drawable.snake_snake, null));
        apples.remove(gridCells[currY][currX]);
        makeApple();
    }

    /**
     * A helper method to specify direction change when the corresponding user button is selected.
     * @param view The corresponding button that was clicked (@+id/snake_right)
     */
    public void setDirectionRight(View view) {
        direction = SnakeDirection.RIGHT;
    }

    /**
     * A helper method to specify direction change when the corresponding user button is selected.
     * @param view The corresponding button that was clicked (@+id/snake_left)
     */
    public void setDirectionLeft(View view) {
        direction = SnakeDirection.LEFT;
    }

    /**
     * A helper method to specify direction change when the corresponding user button is selected.
     * @param view The corresponding button that was clicked (@+id/snake_down)
     */
    public void setDirectionDown(View view) {
        direction = SnakeDirection.DOWN;
    }

    /**
     * A helper method to specify direction change when the corresponding user button is selected.
     * @param view The corresponding button that was clicked (@+id/snake_up)
     */
    public void setDirectionUp(View view) {
        direction = SnakeDirection.UP;
    }

    /**
     * A helper method to produce the dialog for a finished game.
     */
    private void makeDialog() {
        scores = new Scorekeeper(this);
        final boolean addScore = scores.checkNewTopScore(GameName.SNEK, appleCount);
        winnerName = new EditText(this);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setNegativeButton("Back to Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (addScore) {
                    scores.addTopScore(GameName.SNEK, new Score(winnerName.getText().toString(), appleCount, "N/A"));
                }
                Intent menuIntent = new Intent(getApplicationContext(), Menu.class);
                menuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(menuIntent);
            }
        });

        dialog.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (addScore) {
                    scores.addTopScore(GameName.SNEK, new Score(winnerName.getText().toString(), appleCount, "N/A"));
                }
                Intent newGameIntent = new Intent(getApplicationContext(), Snake_Game.class);
                newGameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newGameIntent);
            }
        });

        String appleString;
        switch (appleCount) {
            case 1:     appleString = "Your snake ate 1 apple.";    break;
            default:    appleString = String.format("Your snake ate %d apples.", appleCount);   break;
        }

        if (addScore) {
            appleString += " This is a new top score! Enter your name below:";
            winnerName = new EditText(this);
            winnerName.setHint(R.string.anon);
            dialog.setView(winnerName);
        }
        dialog.setTitle(R.string.snake_game_over);
        dialog.setMessage(appleString);
        dialog.show();
    }

}


