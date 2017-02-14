package edu.rosehulman.finngw.quicknotes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Alarm extends Card implements Parcelable {

    private String time;
    private String snoozeTime;

    public Alarm(){}

    public Alarm(String t, String d, String uid, int h, int m) {
        super(t, d, uid);
        if(h < 10) {
            time = 0 + "" + h + m;
        } else {
            time = "" + h + m;
        }
        if(m + 5 >= 60) {
            m = (m + 5) % 60;
            h = h + 1;
        }
        snoozeTime = "" + h + (m + 5);
    }

    protected Alarm(Parcel in) {
        time = in.readString();
        snoozeTime = in.readString();
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public void setTime(int h, int m) {
        time = "" + h + m;
        snoozeTime = "" + h + (m + 5);
    }

    public String getSnoozeTime() {
        return snoozeTime;
    }

    public void setSnoozeTime(String snoozeTime) {
        this.snoozeTime = snoozeTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(snoozeTime);
    }
}
