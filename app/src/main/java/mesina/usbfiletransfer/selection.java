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

}
