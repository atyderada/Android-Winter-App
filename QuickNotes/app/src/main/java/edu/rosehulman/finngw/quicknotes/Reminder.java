package edu.rosehulman.finngw.quicknotes;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Reminder extends Card {
    public Date date;

    public Reminder(int d, int m, int y) {
        date.day = d;
        date.month = m;
        date.year = y;
    }

    public void setDate(int d, int m, int y) {
        date.day = d;
        date.month = m;
        date.year = y;
    }
}
