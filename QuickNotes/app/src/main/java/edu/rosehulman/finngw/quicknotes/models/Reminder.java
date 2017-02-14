package edu.rosehulman.finngw.quicknotes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Reminder extends Card implements Parcelable {

    private String date;
    private String completed;

    public Reminder() {
    }

    public Reminder(String t, String de, String uid, int y, int m, int d) {
        super(t, de, uid);
        if(m < 10) {
            date = y + "0" + m;
        } else {
            date = y + "" + m;
        }
        if(d < 10) {
            date = date.concat("0" + d);
        } else {
            date = date.concat("" + d);
        }
        completed = "false";
    }

    protected Reminder(Parcel in) {
        date = in.readString();
        completed = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) {
            return new Reminder(in);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(completed);
    }
}
