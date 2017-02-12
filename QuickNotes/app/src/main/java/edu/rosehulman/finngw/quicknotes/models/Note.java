package edu.rosehulman.finngw.quicknotes.models;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Note extends Card {

    private String title;
    private String description;

    public Note() {}

    public Note(String t, String d) {
        super(t, d);
    }
}
