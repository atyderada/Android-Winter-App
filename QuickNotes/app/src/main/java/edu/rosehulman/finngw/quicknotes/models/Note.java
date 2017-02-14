package edu.rosehulman.finngw.quicknotes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Note extends Card implements Parcelable {
    public Note() {}

    public Note(String t, String d, String id) {
        super(t, d, id);
    }

    protected Note(Parcel in) {
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
