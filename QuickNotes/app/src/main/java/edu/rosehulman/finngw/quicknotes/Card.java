package edu.rosehulman.finngw.quicknotes;

/**
 * Created by deradaam on 1/19/2017.
 */

public abstract class Card {

    public String title;
    public String description;

    public Card() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
