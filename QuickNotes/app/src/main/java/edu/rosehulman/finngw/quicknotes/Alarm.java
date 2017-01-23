package edu.rosehulman.finngw.quicknotes;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Alarm extends Card {

    public Time time;
    public Time snoozeTime;

    public Alarm(String t, String d, int h, int m) {
        super(t, d);
        time = new Time(h, m);
        snoozeTime = new Time(h, m + 5);
    }

    public void setTime(int h, int m) {
        time.hour = h;
        time.minute = m;
        snoozeTime.hour = h;
        snoozeTime.minute = m + 5;
    }
}
