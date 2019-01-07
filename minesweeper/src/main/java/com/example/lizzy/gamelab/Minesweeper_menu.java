package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

/**
 * Minesweeper_menu game menu view.
 */
public class Minesweeper_menu extends AppCompatActivity {
    protected String level = "Hard";
    private float dpScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dpScale = getResources().getDisplayMetrics().density;

        // level selection
        final Button easy = (Button) findViewById(R.id.level_easy);
        easy.setAllCaps(false);
        final Button medium = (Button) findViewById(R.id.level_med);
        medium.setAllCaps(false);
        final Button hard = (Button) findViewById(R.id.level_hard);
        hard.setAllCaps(false);
        final TextView levelHint = (TextView) findViewById(R.id.ms_game_level_hint);

        // set level preference for engine Activity
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(true);
                easy.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                medium.setSelected(false);
                medium.setBackgroundColor(getResources().getColor(R.color.colorAccent4, null));
                hard.setSelected(false);
                hard.setBackgroundColor(getResources().getColor(R.color.colorAccent4, null));
                level = "Easy";
                levelHint.setText("About as easy as it gets! 1 in 10 squares will be mines.");
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(false);
                easy.setBackgroundColor(getResources().getColor(R.color.colorAccent4, null));
                medium.setSelected(true);
                medium.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                hard.setSelected(false);
                hard.setBackgroundColor(getResources().getColor(R.color.colorAccent4, null));
                level = "Medium";
                levelHint.setText("Goldilocks level. 1 in 8 squares will be mines.");
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(false);
                easy.setBackgroundColor(getResources().getColor(R.color.colorAccent4, null));
                medium.setSelected(false);
                medium.setBackgroundColor(getResources().getColor(R.color.colorAccent4, null));
                hard.setSelected(true);
                hard.setBackgroundColor(getResources().getColor(R.color.colorAccent2, null));
                level = "Hard";
                levelHint.setText("About as tough as it gets! 1 in 6 squares will be mines.");
            }
        });

        // create "new game" button
        Button newGame = (Button) findViewById(R.id.new_game);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame(level);
            }
        });

    }

    protected void startNewGame(String difficulty) {
        Intent intent = new Intent(this, Minesweeper_game.class);
        intent.removeExtra("Level");
        intent.putExtra("Level", difficulty);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
