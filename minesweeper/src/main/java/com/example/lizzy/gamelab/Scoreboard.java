package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;

/**
 * A view class for the scoreboard.
 */
public class Scoreboard extends AppCompatActivity {

    private Scorekeeper currentScores;

    /**
     * An initializer for the Scoreboard activity.
     * @param savedInstanceState The activity-trigger instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentScores = new Scorekeeper(this);
        addMinesweeperScores();
        addTictactoeScores();
    }

    /**
     * A helper method to add Minesweeper scores to the scoreboard.
     */
    private void addMinesweeperScores() {
        String text = "";
        TableLayout msScoreTable = (TableLayout) findViewById(R.id.ms_score_table);
        ArrayList<Score> msScores = currentScores.getGameType(GameName.MINESWEEPER);
        msScoreTable.addView(new TableRow(this),0);
        if (msScores.size() < 1) {
            TableRow emptyRow = addRowForEmptyTable();
            msScoreTable.addView(emptyRow);
        } else {
            TableRow header = addHeaderRow();
            msScoreTable.addView(header);
            for (int i = 0; i < msScores.size(); i++) {
                TableRow newRow = addRow(msScores.get(i), i);
                msScoreTable.addView(newRow);
            }
        }
    }

    /**
     * A helper method to add Tic-Tac-Toe scores to the scoreboard.
     */
    private void addTictactoeScores() {
        String text = "";
        TableLayout tttScoreText = (TableLayout) findViewById(R.id.ttt_score_table);
        ArrayList<Score> tttScores = currentScores.getGameType(GameName.TICTACTOE);
        for (int i = 0; i < tttScores.size(); i++) {
            // some stuff here
        }
    }

    /**
     * A helper method to add successive rows to the game's score table.
     * @param thisScore The score to be added as a row
     * @param idx The Score object's corresponding index in its arraylist (indicates order)
     * @return The newly constructed TableRow for the table
     */
    private TableRow addRow(Score thisScore, int idx) {
        TableRow newRow = new TableRow(this);

        String num = (idx + 1) + ". ";
        TextView col1 = new TextView(this);
        col1.setText(num);
        newRow.addView(col1);

        TextView col2 = new TextView(this);
        col2.setText(Integer.toString(thisScore.getScore()));
        newRow.addView(col2);

        TextView col3 = new TextView(this);
        col3.setText(thisScore.getWinnerName());
        newRow.addView(col3);

        TextView col4 = new TextView(this);
        col4.setText(thisScore.getDifficulty());
        newRow.addView(col4);

        return newRow;
    }

    /**
     * A helper method to create a default header row for the game types that do have scores to display.
     * @return The newly constructed header row for the score table
     */
    private TableRow addHeaderRow() {
        TableRow newRow = new TableRow(this);
        String num = "Rank: ";
        TextView col1 = new TextView(this);
        col1.setText(num);
        col1.setTypeface(null, BOLD);
        newRow.addView(col1);

        String score = "Score: ";
        TextView col2 = new TextView(this);
        col2.setText(score);
        col2.setTypeface(null, BOLD);
        newRow.addView(col2);

        String name = "Name: ";
        TextView col3 = new TextView(this);
        col3.setText(name);
        col3.setTypeface(null, BOLD);
        newRow.addView(col3);

        String level = "Level: ";
        TextView col4 = new TextView(this);
        col4.setText(level);
        col4.setTypeface(null, BOLD);
        newRow.addView(col4);

        return newRow;
    }

    /**
     * A helper method to create a row that says the score table is empty.
     */
    private TableRow addRowForEmptyTable() {
        TableRow newRow = new TableRow(this);
        String emptyRow = "No content to show here";
        TextView text = new TextView(this);
        text.setText(emptyRow);
        newRow.addView(text);
        return newRow;
    }
}
