package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

/**
 * Minesweeper_menu game menu view.
 */
public class Minesweeper_menu extends AppCompatActivity {
    protected String level = "";
    private float dpScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dpScale = getResources().getDisplayMetrics().density;

        // level selection
        final Button easy = (Button) findViewById(R.id.level_easy);
        final Button medium = (Button) findViewById(R.id.level_med);
        final Button hard = (Button) findViewById(R.id.level_hard);

        // set level preference for engine Activity
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(true);
                easy.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                medium.setSelected(false);
                medium.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                hard.setSelected(false);
                hard.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                level = "Easy";
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(false);
                easy.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                medium.setSelected(true);
                medium.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                hard.setSelected(false);
                hard.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                level = "Medium";
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(false);
                easy.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                medium.setSelected(false);
                medium.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                hard.setSelected(true);
                hard.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                level = "Hard";
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
        intent.putExtra("Level", difficulty);
        startActivity(intent);
    }

}
