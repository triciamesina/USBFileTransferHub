package mesina.usbfiletransfer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tricia on 3/16/2016.
 */
public class selection implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<selection> CREATOR
            = new Parcelable.Creator<selection>() {
        public selection createFromParcel(Parcel in) {
            return new selection(in);
        }

        public selection[] newArray(int size) {
            return new selection[size];
        }
    };

    /** recreate object from parcel */
    private selection(Parcel in) {
        mFilename = in.readString();
        mDestination = in.readString();
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
