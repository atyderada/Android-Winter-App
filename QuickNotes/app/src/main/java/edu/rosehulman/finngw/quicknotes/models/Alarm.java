package edu.rosehulman.finngw.quicknotes.models;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Alarm extends Card {

    private String time;
    private String snoozeTime;

    public Alarm(){}

    public Alarm(String t, String d, int h, int m) {
        super(t, d);
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
}
