package edu.rosehulman.finngw.quicknotes;

/**
 * Created by deradaam on 1/19/2017.
 */

public class Note extends Card {

    public String title;
    public String description;

    public Note() {
        super();
    }

    public Note(String t, String d) {
        super(t, d);
    }
}
