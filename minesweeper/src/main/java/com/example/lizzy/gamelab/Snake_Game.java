package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridLayout;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A class to manage UI and gameplay inputs for Snake adaption.
 */
public class Snake_Game extends AppCompatActivity {

    private GridLayout snakeGrid;
    private int gridWidth;
    private int gridHeight;
    private View[][] gridCells;
    private int actualCellWidth;
    private int actualCellHeight;
    private Queue snake;
    private int appleX;
    private int appleY;
    private int currX;
    private int currY;
    private SnakeDirection direction;

    /**
     * The initializer for the activity.
     * @param savedInstanceState The activity-trigger instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                System.out.println("children have been measured. col count is " + snakeGrid.getColumnCount() + "; row count is " +
                        snakeGrid.getRowCount() + "; cell dimensions are " + actualCellWidth + " by " + actualCellHeight);
                placeChildren();
                makeSnake();
                makeApple();
            }
        });

        Timer theTimer = new Timer();

        TimerTask moveSnake = new TimerTask() {
            @Override
            public void run() {
                makeNextMove();
            }
        };

        theTimer.schedule(moveSnake, 1000);
    }

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
        snakeGrid.requestLayout();
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
            if (!snake.contains(gridCells[row][col])) {
                emptyView = true;
                appleX = col;
                appleY = row;
            }
        }
        gridCells[appleY][appleX].setBackground(null);
    }

    /**
     * A helper method to execute end-of-game logic.
     */
    private void gameOver() {

    }

    /**
     * A helper method to make the next move on the board.
     */
    private void makeNextMove() {
        switch (direction) {
            case UP:    currY--;    break;
            case DOWN:  currY++;    break;
            case LEFT:  currX--;    break;
            case RIGHT: currX++;    break;
        }
        if (currY < 0 || currY >= snakeGrid.getRowCount() || currX < 0 || currX >= snakeGrid.getColumnCount() ||
                snake.contains(gridCells[currY][currX])) {
            gameOver();
        } else if (currY == appleY && currX == appleX) {
            eatApple();
        } else {
            snake.push(gridCells[currY][currX]);
        }
    }

    private void eatApple() {

    }

}
