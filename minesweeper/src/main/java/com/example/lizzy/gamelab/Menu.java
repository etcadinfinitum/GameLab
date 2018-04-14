package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

/**
 * The class managing the application main menu and the corresponding buttons for each available game.
 */
public class Menu extends AppCompatActivity {

    private TextView mTextMessage;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent menu = new Intent(getApplicationContext(), Menu.class);
                    menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(menu);
                    return true;
                case R.id.navigation_dashboard:
                    Intent scoreboard = new Intent(getApplicationContext(), Scoreboard.class);
                    scoreboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(scoreboard);
                    return true;
                case R.id.navigation_notifications:
                    Intent about = new Intent (getApplicationContext(), AboutPage.class);
                    about.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(about);
                    return true;
            }
            return false;
        }
    };

    /**
     * The initializer for the activity.
     * @param savedInstanceState The
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button minesweeper = (Button) findViewById(R.id.minesweeper);
        minesweeper.setText(R.string.title_activity_minesweeper);
        minesweeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMinesweeperMenu();
            }
        });

        Button tictactoe = (Button) findViewById(R.id.ttt);
        tictactoe.setText("Tic-Tac-Toe");
        tictactoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTicTacToe();
            }
        });

        Button boggle = (Button) findViewById(R.id.boggle);
        boggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBoggle();
            }
        });

        Button snake = (Button) findViewById(R.id.snek);
        snake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSnake();
            }
        });
    }

    private void goToMinesweeperMenu() {
        Intent intent = new Intent(this, Minesweeper_menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void goToTicTacToe() {
        Intent intent = new Intent(this, TicTacToe_game.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void goToBoggle() {
        Intent intent = new Intent(this, Boggle_Game.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void goToSnake() {
        Intent intent = new Intent(this, Snake_Game.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
