package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

/**
 * Minesweeper game menu view.
 */
public class Minesweeper extends AppCompatActivity {
    protected String level = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //

        // level selection
        final Button easy = (Button) findViewById(R.id.level_easy);
        final Button medium = (Button) findViewById(R.id.level_med);
        final Button hard = (Button) findViewById(R.id.level_hard);

        // set level preference for engine Activity
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(true);
                medium.setSelected(false);
                hard.setSelected(false);
                level = "Easy";
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(false);
                medium.setSelected(true);
                hard.setSelected(false);
                level = "Medium";
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setSelected(false);
                medium.setSelected(false);
                hard.setSelected(true);
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
