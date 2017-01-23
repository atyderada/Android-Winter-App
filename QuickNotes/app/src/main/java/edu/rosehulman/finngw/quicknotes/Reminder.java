package edu.rosehulman.finngw.quicknotes;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Reminder extends Card {
    public Date date;

    public Reminder() {
    }

    public Reminder(String t, String de, int d, int m, int y) {
        super(t, de);
        date = new Date(m, d, y);
    }

    public void setDate(int d, int m, int y) {
        date.day = d;
        date.month = m;
        date.year = y;
    }
}
