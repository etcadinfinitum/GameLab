package com.example.lizzy.gamelab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentScores = new Scorekeeper(this);
        addScores(GameName.BOGGLE);
        addScores(GameName.MINESWEEPER);
        addScores(GameName.SNEK);
        // addScores(GameName.TICTACTOE);
    }

    /**
     * A helper method to set up score table based on the game type argument.
     * @param gameName The game type to set up a scoreboard for
     */
    private void addScores(GameName gameName) {
        TableLayout scoreTable = null;
        switch (gameName) {
            case SNEK: scoreTable = (TableLayout) findViewById(R.id.snake_score_table);     break;
            case BOGGLE: scoreTable = (TableLayout) findViewById(R.id.boggle_score_table);  break;
            case TICTACTOE: scoreTable = (TableLayout) findViewById(R.id.ttt_score_table);  break;
            case MINESWEEPER: scoreTable = (TableLayout) findViewById(R.id.ms_score_table); break;
        }
        ArrayList<Score> scores = currentScores.getGameType(gameName);
        scoreTable.addView(new TableRow(this),0);
        if (scores.size() < 1) {
            TableRow emptyRow = addRowForEmptyTable();
            scoreTable.addView(emptyRow);
        } else {
            TableRow header = addHeaderRow();
            scoreTable.addView(header);
            for (int i = 0; i < scores.size(); i++) {
                TableRow newRow = addRow(scores.get(i), i);
                scoreTable.addView(newRow);
            }
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
