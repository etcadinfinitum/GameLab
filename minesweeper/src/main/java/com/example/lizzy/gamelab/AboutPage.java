package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // enable link "movement" actions when user clicks on HTML-hyperlinked string
        TextView theText = (TextView) findViewById(R.id.about_text);
        theText.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
