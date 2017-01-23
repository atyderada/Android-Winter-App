package edu.rosehulman.finngw.quicknotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by deradaam on 1/23/2017.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private List<Card> mCards;
    private Callback mCallback;
    //private DatabaseReference mCardsRef;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardListAdapter(List<Card> myDataset, Callback callback) {
        mCallback = callback;
        mCards = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_row_view, parent, false);
        // set the view's size, margins, paddings and layout parameters ...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mDescriptionView.setText(mCards.get(position).title);
        holder.mContentView.setText(mCards.get(position).description);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public interface Callback {
        public void onEdit(Card card);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item contains a description and content
        public TextView mDescriptionView;
        public TextView mContentView;

        public ViewHolder(View v) {
            super(v);
            mDescriptionView = (TextView) v.findViewById(R.id.description_text);
            mContentView = (TextView) v.findViewById(R.id.content_text);
        }
    }
}
