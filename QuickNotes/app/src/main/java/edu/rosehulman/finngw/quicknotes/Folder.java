package edu.rosehulman.finngw.quicknotes;

import java.util.ArrayList;

/**
 * Created by deradaam on 1/20/2017.
 */

public class Folder {

    public ArrayList<Card> list;
    public String name;

    public Folder(String n) {
        list = new ArrayList<Card>();
        name = n;
    };

    public void changeName(String n) {
        name = n;
    }

    public void addCard(Card c) {
        list.add(c);
    }
}
