package edu.rosehulman.finngw.quicknotes.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.fragments.ReminderListFragment;
import edu.rosehulman.finngw.quicknotes.models.Reminder;
import edu.rosehulman.finngw.quicknotes.utilities.Constants;
import edu.rosehulman.finngw.quicknotes.utilities.SharedPreferencesUtils;

public class ReminderRecyclerViewAdapter extends RecyclerView.Adapter<ReminderRecyclerViewAdapter.ViewHolder> {

    private final ReminderListFragment mReminderListFragment;

    private final ReminderListFragment.OnReminderSelectedListener mReminderSelectedListener;
    private String mUid;
    private DatabaseReference mRemindersRef;
    private ArrayList<Reminder> mReminders = new ArrayList<>();

    public ReminderRecyclerViewAdapter(ReminderListFragment reminderListFragment, ReminderListFragment.OnReminderSelectedListener listener) {

        mReminderListFragment = reminderListFragment;
        mReminderSelectedListener = listener;

        mUid = SharedPreferencesUtils.getCurrentUser(reminderListFragment.getContext());Log.d(Constants.TAG, "Current user: " + mUid);

        assert (!mUid.isEmpty()); // Consider: use if (BuildConfig.DEBUG)

        mRemindersRef = FirebaseDatabase.getInstance().getReference(Constants.REMINDERS_PATH);
        Query mReminderQuery = mRemindersRef.orderByChild("uid").equalTo(mUid);
        mReminderQuery.addChildEventListener(new RemindersChildEventListener());
    }

    public void firebasePush(String reminderTitle, String reminderDescription, String date, int year, int month, int day) {
        Reminder reminder = new Reminder(reminderTitle, reminderDescription, mUid, year, month, day);
        DatabaseReference reminderRef = mRemindersRef.push();
        String reminderKey = reminderRef.getKey();
        reminderRef.setValue(reminder);
    }

    public void firebaseEdit(Reminder reminder, String newReminderTitle, String newReminderDescription) {
        reminder.setTitle(newReminderTitle);
        reminder.setDescription(newReminderDescription);
        mRemindersRef.child(reminder.getKey()).setValue(reminder);
    }

    public void firebaseRemove(Reminder reminderToRemove) {
        mRemindersRef.child(reminderToRemove.getKey()).removeValue();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reminder, parent, false);
        return new ReminderRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mReminderTitleTextView.setText(mReminders.get(position).getTitle());
        holder.mReminderDescriptionTextView.setText(mReminders.get(position).getDescription());
        holder.mReminderDateTextView.setText(mReminders.get(position).getDate());
        holder.mReminderCompletedTextView.setText(mReminders.get(position).getCompleted());
    }

    @Override
    public int getItemCount() { return mReminders.size(); }

    class RemindersChildEventListener implements ChildEventListener {
        // While we don't push up deletes, we need to listen for other owners deleting our course.

        private void add(DataSnapshot dataSnapshot) {
            Reminder reminder = dataSnapshot.getValue(Reminder.class);
            reminder.setKey(dataSnapshot.getKey());
            mReminders.add(reminder);
        }

        private int remove(String key) {
            for (Reminder reminder : mReminders) {
                if (reminder.getKey().equals(key)) {
                    int foundPos = mReminders.indexOf(reminder);
                    mReminders.remove(reminder);
                    return foundPos;
                }
            }
            return -1;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(Constants.TAG, "My reminder: " + dataSnapshot);
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int position = remove(dataSnapshot.getKey());
            if (position >= 0) {
                notifyItemRemoved(position);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(DatabaseError firebaseError) {
            Log.e("TAG", "onCancelled. Error: " + firebaseError.getMessage());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mReminderTitleTextView;
        private TextView mReminderDescriptionTextView;
        private TextView mReminderDateTextView;
        private TextView mReminderCompletedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mReminderTitleTextView = (TextView) itemView.findViewById(R.id.title_reminder_text);
            mReminderDescriptionTextView = (TextView) itemView.findViewById(R.id.description_reminder_text);
            mReminderDateTextView = (TextView) itemView.findViewById(R.id.date_reminder_text);
            mReminderCompletedTextView = (TextView) itemView.findViewById(R.id.completed_reminder_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferencesUtils.setCurrentCourseKey(mReminderListFragment.getContext(), mReminders.get(getAdapterPosition()).getKey());
            Reminder reminder = mReminders.get(getAdapterPosition());
            mReminderSelectedListener.onReminderSelected(reminder);
        }

        @Override
        public boolean onLongClick(View v) {
            Reminder reminder = mReminders.get(getAdapterPosition());
            mReminderListFragment.showReminderDialog(reminder);
            return true;
        }
    }
}
