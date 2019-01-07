package experiment.gamelab;

import android.app.Application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;

public class GameLabApplication extends Application {

    private HashSet<String> dictionary;
    private static GameLabApplication theInstance = null;

    public static GameLabApplication getInstance() {
        return theInstance;
    }

    /**
     * Create the dictionary
     */
    public void setDictionary() {
        try {
            System.out.println("opening and reading asset file for dictionary (GameLabApplication class)");
            // AssetManager getDictionaryAsset = new AssetManager("dictionary.ser");
            ObjectInputStream stream = new ObjectInputStream(this.getAssets().open("dictionary.ser"));
            dictionary = (HashSet<String>) stream.readObject();
            System.out.println("finished reading dictionary file");
        } catch (FileNotFoundException e) {
            System.out.println("---------> building dictionary from scratch (no .ser file) <---------");
        } catch (IOException e) {
            System.out.println("---------> an ioException occurred <---------");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("---------> an unspecified error occurred <---------");
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public HashSet<String> getDictionary() {
        return dictionary;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        theInstance = this;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
