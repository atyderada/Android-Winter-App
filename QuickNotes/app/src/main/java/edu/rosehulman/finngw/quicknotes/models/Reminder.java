package edu.rosehulman.finngw.quicknotes.models;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Reminder extends Card {

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
}
