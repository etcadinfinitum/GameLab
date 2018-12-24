package com.example.lizzy.gamelab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
// for testing only
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add some code to handle loading the dictionary... make static??
        GameLabApplication application = GameLabApplication.getInstance();
        application.setDictionary();


        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        finish();
    }
}