package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Random;

/**
 * A view class for the Boggle game implementation. Manages the layout, and acts as an observer to the
 * Boggle game engine class.
 */
public class Boggle_Game extends AppCompatActivity implements Observer {

    private ArrayList<String> userWords;
    private LinkedList currentWord;

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

        // instantiate the model
        Boggle_Model model = new Boggle_Model(this);
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

        // set up buttons for game board, and randomly place board letters on board
        GridLayout board = (GridLayout) findViewById(R.id.boggle_board);
        Button[][] boardButtons = new Button[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Random random = new Random();
                int idx = random.nextInt(lettersToAdd.size());
                boardButtons[row][col] = new Button(this);
                boardButtons[row][col].setAllCaps(false);
                boardButtons[row][col].setText(lettersToAdd.get(idx));
                lettersToAdd.remove(idx);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(row, 1), GridLayout.spec(col, 1));
                params.height = (int) Math.floor(metrics.density * 75);
                params.width = (int) Math.floor(metrics.density * 75);
                board.addView(boardButtons[row][col], params);
                // need to add listeners to buttons (or to viewgroup?)
            }
        }

    }

    /**
     * The interface-mandated implementation of the update method. This method is called by the
     * Observable-type object (the game engine), and prompts the view to process the observable object's
     * information as needed. For this implementation, update will assess whether the user's last move
     * was legal imput (after the word is compared to the underlying dictionary) and will add the word
     * to the score list as appropriate.
     * @param model The game engine's state
     * @param someObject ???
     */
    public void update(Observable model, Object someObject) {

    }
}
