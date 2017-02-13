package edu.rosehulman.finngw.quicknotes.models;

import com.google.firebase.database.Exclude;

/**
 * Created by deradaam on 1/19/2017.
 */

public abstract class Card {

    private String key;
    private String uid;

    private String title;
    private String description;

    public Card() {}

    public Card(String t, String d, String id) {
        title = t;
        description = d;
        uid = id;
    }

    @Exclude
    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}

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
