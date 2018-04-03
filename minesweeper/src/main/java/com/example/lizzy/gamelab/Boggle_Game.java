package com.example.lizzy.gamelab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Random;

/**
 * A view class for the Boggle game implementation. Manages the layout, and acts as an observer to the
 * Boggle game engine class.
 */
public class Boggle_Game extends AppCompatActivity implements Observer {

    private Boggle_Model model;
    private ArrayList<String> userWords;
    private LinkedList currentWord;
    private int hint = 3;
    private EditText winnerName;
    private String gameLevelString = "4x4";
    private Scorekeeper scores;
    private Button lastButton;
    private Button[][] boardButtons;
    private GridLayout boardShell;
    private int boardSize;
    private TableRow currentRow;
    private TableLayout words;

    /**
     * The initializer for the activity.
     * @param savedInstanceState The activity-trigger instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boggle__game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boardSize = 16;

        // instantiate the model
        model = new Boggle_Model(this);
        // instantiate the list of accepted words
        userWords = new ArrayList<>(50);
        // instantiate the linked list for current submission
        currentWord = new LinkedList();

        // get generated board letters
        String[] letters = model.getThisBoard();
        // convert letters object to ArrayList for easy removal of items
        ArrayList<String> lettersToAdd = new ArrayList<>(16);
        for (int i = 0; i < letters.length; i++) {
            lettersToAdd.add(letters[i]);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        boardShell = (GridLayout) findViewById(R.id.boggle_board);

        boardShell.setOnTouchListener(boardListener);

        // set up buttons for game board, and randomly place board letters on board
        boardButtons = new Button[4][4];

        for (int i = 0; i < boardSize; i++) {
            int row = i / 4;
            int col = i % 4;
            Random random = new Random();
            int idx = random.nextInt(lettersToAdd.size());
            boardButtons[row][col] = new Button(this);
            boardButtons[row][col].setAllCaps(false);
            boardButtons[row][col].setText(lettersToAdd.get(idx));
            boardButtons[row][col].setClickable(false);
            boardButtons[row][col].setFocusable(false);
            lettersToAdd.remove(idx);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row, 1), GridLayout.spec(col, 1));
            params.height = (int) Math.floor(metrics.density * 75);
            params.width = (int) Math.floor(metrics.density * 75);
            boardShell.addView(boardButtons[row][col], params);
        }

        words = (TableLayout) findViewById(R.id.boggle_words);


        final TextView timerText = (TextView) findViewById(R.id.boggle_timer_time);
        timerText.setText("01:01");

        CountDownTimer timer = new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) Math.round(millisUntilFinished / 1000);
                String timeRemaining = "00:";
                if (Integer.toString(seconds).length() < 2) {
                    timeRemaining += "0" + Integer.toString(seconds);
                } else {
                    timeRemaining += Integer.toString(seconds);
                }
                timerText.setText(timeRemaining);
            }

            @Override
            public void onFinish() {
                model.makeMove(currentWord);
                for (int row = 0; row < 4; row++) {
                    for (int col = 0; col < 4; col++) {
                        boardButtons[row][col].setEnabled(false);
                    }
                }
                gameOver();

            }
        };
        timer.start();

    }

    public ViewGroup.OnTouchListener boardListener = new ViewGroup.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    // System.out.println("-----> board's touch listener detected movement. coordinates X: " + event.getX() + " Y: " + event.getY());
                    // System.out.println("---> detected action " + event.getAction() + " (0=down, 2=move), checking to see if button text should be added");
                    Button button = getButtonFromScreen(event.getX(), event.getY());
                    if (button != null) {
                        lastButton = button;
                        CharSequence text = button.getText();
                        // System.out.println("adding letter from button: " + text);
                        currentWord.add((text.toString()).toLowerCase(), button);
                        // System.out.println("current word is " + currentWord.getFullString());
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // System.out.println("-----> action event has ended. call model.makeMove with current word: " + currentWord.getFullString());
                    model.makeMove(currentWord);
                    return true;
            }
            return true;
        }

    };


    /**
     * The interface-mandated implementation of the update method. This method is called by the
     * Observable-type object (the game engine), and prompts the view to process the observable object's
     * information as needed. For this implementation, update will assess whether the user's last move
     * was legal imput (after the word is compared to the underlying dictionary) and will add the word
     * to the score list as appropriate.
     * @param model The game engine's state
     * @param someObject The user's word which has been validated against the dictionary (or null if not valid)
     */
    public void update(Observable model, Object someObject) {
        // get user's word and validate it against dictionary
        String word = (String) someObject;
        if (word != null) {
            userWords.add(word);

            // if valid, add to game's score table
            TextView newWord = new TextView(this);
            newWord.setText(word);
            TextView newWordScore = new TextView(this);
            newWordScore.setText(Integer.toString(calculateWordScore(word)));

            // dynamically determine if new row should be created (assuming display should be 6 columns total)
            if (userWords.size() % 3 == 1) {
                TableRow newRow = new TableRow(this);
                currentRow = newRow;
                newRow.addView(newWord);
                newRow.addView(newWordScore);
                words.addView(newRow);
            } else {
                currentRow.addView(newWord);
                currentRow.addView(newWordScore);
            }
        }
        currentWord = null;
        currentWord = new LinkedList();
    }

    /**
     * A helper method to calculate the user's current score.
     * @return The user's score for this game
     */
    private int calculateScore() {
        int sum = 0;
        for (int i = 0; i < userWords.size(); i++) {
            sum += calculateWordScore(userWords.get(i));
        }
        return sum;
    }

    /**
     * A helper method to calculate the points scored for a given word.
     * @return The word's score
     */
    private int calculateWordScore(String word) {
        switch (word.length()) {
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    /**
     * A helper method to assess the user's score and generate popups,
     */
    private void gameOver() {
        scores = new Scorekeeper(this);
        final int userScore = calculateScore();
        final boolean addNewScore = scores.checkNewTopScore(GameName.BOGGLE, userScore);
        if (addNewScore) {
            winnerName = new EditText(this);
            winnerName.setHint(R.string.anon);
        }
        String words = "";
        for (int i = 0; i < userWords.size(); i++) {
            words += userWords.get(i) + " ";
        }
        System.out.println("game over. words added to score list are: " + words);

        // add popup indicating win/loss

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setNegativeButton("Back to Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (addNewScore) {
                    Score newScore = new Score(winnerName.getText().toString(), userScore, gameLevelString);
                    scores.addTopScore(GameName.BOGGLE, newScore);
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
                    Score newScore = new Score(winnerName.getText().toString(), userScore, gameLevelString);
                    scores.addTopScore(GameName.BOGGLE, newScore);
                    scores = null;
                }
                Intent newGameIntent = new Intent(getApplicationContext(), Boggle_Game.class);
                newGameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newGameIntent);
            }
        });

        if (addNewScore) {
            dialog.setCancelable(true);
            dialog.setTitle("Game Over!");
            dialog.setMessage("Your score was " + userScore + " from " + userWords.size() + " words." + getString(R.string.top_score));
            dialog.setView(winnerName);
            dialog.show();
        } else {
            dialog.setCancelable(true);
            dialog.setTitle("Game Over!");
            dialog.setMessage("Your score was " + userScore + " from " + userWords.size() + " words.");
            dialog.show();
        }

    }

    /**
     * A helper method which correlates the x-y coordinates of a touch event to an underlying button,
     * where applicable. This method will return a null reference if the touch event is outside of
     * the bounding boxes for all underlying buttons. The bounding box is a 150dp box centered in the
     * 300dp button, which ensures that the user will not accidentally select a different button when
     * the motion event is moving diagonally.
     * @param x The x-coordinate of the motion event
     * @param y The y-coordinate of the motion event
     * @return The button that corresponds with the given coordinates (null --> not within bounding box)
     */
    private Button getButtonFromScreen(float x, float y) {
        for (int i = 0; i < 16; i++) {
            Button thisButton = boardButtons[i / 4][i % 4];
            if (thisButton.getX() + 75 <= x && thisButton.getX() + thisButton.getWidth() - 75 >= x && thisButton.getY() + 75 <= y &&
                    thisButton.getY() + thisButton.getHeight() - 75 >= y) {
                // useful measurement outputs
                // System.out.println("----> button's x value + width: x: " + thisButton.getX() + " width: " + thisButton.getWidth());
                // System.out.println("----> button's y value + height: y: " + thisButton.getY() + " height: " + thisButton.getHeight());
                // System.out.println("----> x value: " + x + " y value: " + y);
                return thisButton;
            }
        }
        return null;
    }

}