package edu.rosehulman.finngw.quicknotes.models;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Reminder extends Card {
    private String date;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private boolean completed;

    public Reminder() {
    }

    public Reminder(String t, String de, int y, int m, int d) {
        super(t, de);
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
        completed = false;
    }
}
