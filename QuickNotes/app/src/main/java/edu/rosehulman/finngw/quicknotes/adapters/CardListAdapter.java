package edu.rosehulman.finngw.quicknotes.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.rosehulman.finngw.quicknotes.models.Alarm;
import edu.rosehulman.finngw.quicknotes.models.Card;
import edu.rosehulman.finngw.quicknotes.models.Note;
import edu.rosehulman.finngw.quicknotes.models.Reminder;
import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.utilities.Constants;

/**
 * Created by deradaam on 1/23/2017.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private List<Card> mCards;
    private Callback mCallback;
    private Context mContext;
    private DatabaseReference mFoldersRef;

    //private DatabaseReference mCardsRef;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardListAdapter(Callback callback, Context context) {
        mCallback = callback;
        mCards = new ArrayList<>();
        mContext = context;
        mFoldersRef = FirebaseDatabase.getInstance().getReference().child("Folder");
        Query mCardsForUser = mFoldersRef.child("notes");
        mCardsForUser.addChildEventListener(new CardsChildEventListener());
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
        holder.mDescriptionView.setText(mCards.get(position).getTitle());
        holder.mContentView.setText(mCards.get(position).getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void switchFolder(String path) {
        mFoldersRef = mFoldersRef.child(path);
    }

    public void addAlarm(String title, String description) {

        final String alarmTitle = title;
        final String alarmDescription = description;

        // Launch Dialog
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Alarm alarm = new Alarm(alarmTitle, alarmDescription, hourOfDay, minute);
                DatabaseReference alarmRef = mFoldersRef.child(Constants.ALARMS_PATH).push();
                alarmRef.setValue(alarm);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Alarm Time");
        mTimePicker.show();
    }

    public void addNote(String title, String description) {
        final String noteTitle = title;
        final String noteDescription = description;

        Note note = new Note(noteTitle, noteDescription);
        DatabaseReference noteRef = mFoldersRef.child(Constants.NOTES_PATH).push();
        noteRef.setValue(note);

    }

    public void addReminder(String title, String description) {
        final String reminderTitle = title;
        final String reminderDescription = description;

        // launch dialog
        Calendar mCurrentDate = Calendar.getInstance();
        int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        int month = mCurrentDate.get(Calendar.MONTH);
        int year = mCurrentDate.get(Calendar.YEAR);
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Reminder reminder = new Reminder(reminderTitle, reminderDescription, year, month + 1, dayOfMonth);
                DatabaseReference reminderRef = mFoldersRef.child(Constants.REMINDERS_PATH).push();
                reminderRef.setValue(reminder);
            }
        }, year, month, day);
        mDatePicker.setTitle("Select a Date");
        mDatePicker.show();
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

    private class CardsChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
