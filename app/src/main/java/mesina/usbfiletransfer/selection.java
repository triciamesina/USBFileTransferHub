package mesina.usbfiletransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tricia on 3/16/2016.
 */
public class selection {

    String mFilename;
    String mDestination;

    public selection (String filename, String destination) {
        this.mFilename = filename;
        this.mDestination = destination;

    }

    public String getFilename() {
        return mFilename;
    }


    public String getDestination() {
        return mDestination;
    }
/*
    public static ArrayList<selection> createSelectedList() {

        ArrayList<selection> selection = new ArrayList<selection>();

        Context applicationContext = PasteActivity.getContextOfApplication();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String files = sharedPref.getString("selection", "No File Selected");
        ArrayList<String> filename = new ArrayList<String>(Arrays.asList(files.split(",")));

        String dests = sharedPref.getString("destination", "Destination");
        ArrayList<String> destination = new ArrayList<String>(Arrays.asList(dests.split(",")));

        for (int i = 0; i<filename.size(); i++) {

            selection.add(new selection(filename.get(i), destination.get(i)));
        }

        return selection;
    }
    */
}
