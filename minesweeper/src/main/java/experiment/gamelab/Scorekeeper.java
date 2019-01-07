package experiment.gamelab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import android.content.Context;

/**
 * A game top score manager. Intended to automatically validate new high scores and add them to a sorted
 * ArrayList.
 * Created by lizzy on 3/24/18.
 */
public class Scorekeeper implements Serializable {

    private ArrayList<ArrayList> scoreLists;
    private ArrayList<Score> minesweeperLocal;
    private ArrayList<Score> tttLocal;
    private ArrayList<Score> boggleLocal;
    private ArrayList<Score> snakeLocal;
    private Context appReference;

    /**
     * Constructor for the scorekeeper object. Will load scores
     * @param context The application context that is instantiating the Scorekeeper object
     */
    public Scorekeeper(Context context) {
        appReference = context;

        // deserialize score lists
        try {
            FileInputStream input = context.openFileInput("scores.ser");
            ObjectInputStream objects = new ObjectInputStream(input);
            scoreLists = (ArrayList<ArrayList>) objects.readObject();
            for (int i = 0; i < scoreLists.size(); i++) {
                switch (i) {
                    case 0:
                        minesweeperLocal = (ArrayList<Score>) scoreLists.get(i);
                        break;
                    case 1:
                        tttLocal = (ArrayList<Score>) scoreLists.get(i);
                        break;
                    case 2:
                        boggleLocal = (ArrayList<Score>) scoreLists.get(i);
                        break;
                    case 3:
                        snakeLocal = (ArrayList<Score>) scoreLists.get(i);
                        break;
                }
            }

        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("FileNotFoundException occurred. Most likely, scores.ser could not be found or is in a different directory.");
            System.out.println("Creating new ArrayList objects to serialize");
            scoreLists = new ArrayList<>(4);
            minesweeperLocal = new ArrayList<>(5);
            tttLocal = new ArrayList<>(5);
            boggleLocal = new ArrayList<>(5);
            snakeLocal = new ArrayList<>(5);
            scoreLists.add(0, minesweeperLocal);
            scoreLists.add(1, tttLocal);
            scoreLists.add(2, boggleLocal);
            scoreLists.add(3, snakeLocal);
        } catch (ClassCastException e) {

        } catch (Exception e) {
            System.out.println("An unspecified exception occurred.");
            e.printStackTrace();
        }
    }

    /**
     * A method to validate whether the score of a successfully won game is a top score for the device.
     * @param game The name of the game, passed as a string.
     * @param score The score of the newly won game, to be tested against the top scores in the existing ArrayList.
     * @return A boolean flag indicating whether the score is a top score (true --> new top score!)
     */
    public boolean checkNewTopScore(GameName game, int score) {
        boolean isTopScore = false;
        ArrayList<Score> gameType = getGameType(game);
        if (gameType != null) {
            if (gameType.size() > 0) {
                for (Score thisScore : gameType) {
                    if (thisScore == null || score >= thisScore.getScore()) {
                        isTopScore = true;
                    }
                }
                if (!isTopScore && gameType.size() < 5) {
                    isTopScore = true;
                }
            } else {
                isTopScore = true;
            }
        }
        return isTopScore;
    }

    /**
     * Returns the appropriate ArrayList object for the game type being queried.
     * @param game The GameName enumerated value
     * @return The appropriate score array for the given game type
     */
    public ArrayList<Score> getGameType(GameName game) {
        ArrayList<Score> gameType;
        switch (game) {
            case MINESWEEPER:
                gameType = minesweeperLocal;
                break;
            case TICTACTOE:
                gameType = tttLocal;
                break;
            case BOGGLE:
                gameType = boggleLocal;
                break;
            case SNEK:
                gameType = snakeLocal;
                break;
            default:
                gameType = null;
                break;
        }
        return gameType;
    }

    /**
     * Adds score to list (in order), then removes any scores that are no longer in the top 5.
     * @param game The game type of the new top score
     * @param newScore The new top score (Score object)
     * @return A boolean flag indicating whether the score has been added to the corresponding arraylist
     */
    public boolean addTopScore(GameName game, Score newScore) {
        boolean isAdded = false;
        ArrayList<Score> modList = getGameType(game);
        try {
            if (modList.size() > 0) {
                for (Score oldScore : modList) {
                    if (!isAdded && newScore.getScore() >= oldScore.getScore()) {
                        int newIdx = modList.indexOf(oldScore);
                        modList.add(newIdx, newScore);
                        isAdded = true;
                    }
                }
                if (!isAdded) {
                    modList.add(modList.size(), newScore);
                    isAdded = true;
                }
            } else {
                modList.add(0, newScore);
                isAdded = true;
            }
            if (isAdded) {
                System.out.println("score was added to " + game + " list; new score is " + newScore.getScore() + "; winner " + newScore.getWinnerName());
            }
        } catch (Exception e) {
            System.out.println("Score was not added - unspecified exception. Game type is " + game + "; arrayList is " + modList);
            e.printStackTrace();
            isAdded = false;
        }
        while (modList.size() > 5) {
            modList.remove(5);
        }
        boolean written = serializeLists();
        return isAdded && written;
    }

    /**
     * A helper method to write the (possibly) modified ArrayLists to a file in the file-system.
     * @return A boolean flag indicating whether the score has been added to the list (true --> added)
     */
    private boolean serializeLists() {
        boolean scoresWritten = false;
        try {
            FileOutputStream output = appReference.openFileOutput("scores.ser", 0);
            ObjectOutputStream objectOutput = new ObjectOutputStream(output);
            objectOutput.writeObject(scoreLists);
            objectOutput.close();
            output.close();
            System.out.println("score list data is: " + scoreLists.toString());
            scoresWritten = true;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException occurred. Scores have not been written to .ser " +
                    "file. Should not occur with implementation since a new file will be created by the openFileOutput method");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException occurred. Scores have not been written to .ser file.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unspecified exception occurred. Scores have not been written to .ser file.");
            e.printStackTrace();
        }
        return scoresWritten;
    }

}