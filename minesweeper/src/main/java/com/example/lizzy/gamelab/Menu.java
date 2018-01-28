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

public class Menu extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

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
    }

    private void goToMinesweeperMenu() {
        Intent intent = new Intent(this, Minesweeper.class);
        startActivity(intent);
    }

}
