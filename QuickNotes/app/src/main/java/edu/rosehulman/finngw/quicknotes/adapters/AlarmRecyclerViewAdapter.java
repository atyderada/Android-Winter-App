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

import java.util.ArrayList;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.fragments.AlarmListFragment;
import edu.rosehulman.finngw.quicknotes.models.Alarm;
import edu.rosehulman.finngw.quicknotes.utilities.Constants;
import edu.rosehulman.finngw.quicknotes.utilities.SharedPreferencesUtils;

public class AlarmRecyclerViewAdapter extends RecyclerView.Adapter<AlarmRecyclerViewAdapter.ViewHolder> {

    private final AlarmListFragment mAlarmListFragment;

    private final AlarmListFragment.OnAlarmSelectedListener mAlarmSelectedListener;
    private String mUid;
    private DatabaseReference mAlarmsRef;
    private ArrayList<Alarm> mAlarms = new ArrayList<>();

    public AlarmRecyclerViewAdapter(AlarmListFragment alarmListFragment, AlarmListFragment.OnAlarmSelectedListener listener) {

        mAlarmListFragment = alarmListFragment;
        mAlarmSelectedListener = listener;

        mUid = SharedPreferencesUtils.getCurrentUser(alarmListFragment.getContext());

        assert (!mUid.isEmpty()); // Consider: use if (BuildConfig.DEBUG)

        mAlarmsRef = FirebaseDatabase.getInstance().getReference(Constants.ALARMS_PATH);
        mAlarmsRef.addChildEventListener(new AlarmsChildEventListener());
    }

    public void firebasePush(String alarmTitle, String alarmDescription, int hour, int minutes) {
        Alarm alarm = new Alarm(alarmTitle, alarmDescription, mUid, hour, minutes);
        DatabaseReference alarmRef = mAlarmsRef.push();
        String alarmKey = alarmRef.getKey();
        alarmRef.setValue(alarm);
    }

    public void firebaseEdit(Alarm alarm, String newAlarmTitle, String newAlarmDescription) {
        alarm.setTitle(newAlarmTitle);
        alarm.setDescription(newAlarmDescription);
        mAlarmsRef.child(alarm.getKey()).setValue(alarm);
    }

    public void firebaseRemove(Alarm alarmToRemove) {
        mAlarmsRef.child(alarmToRemove.getKey()).removeValue();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_alarm, parent, false);
        return new AlarmRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAlarmTitleTextView.setText(mAlarms.get(position).getTitle());
        holder.mAlarmDescriptionTextView.setText(mAlarms.get(position).getDescription());
        holder.mAlarmTimeTextView.setText(mAlarms.get(position).getTime());
    }

    @Override
    public int getItemCount() { return mAlarms.size(); }

    class AlarmsChildEventListener implements ChildEventListener {
        // While we don't push up deletes, we need to listen for other owners deleting our course.

        private void add(DataSnapshot dataSnapshot) {
            Alarm alarm = dataSnapshot.getValue(Alarm.class);
            alarm.setKey(dataSnapshot.getKey());
            mAlarms.add(alarm);
        }

        private int remove(String key) {
            for (Alarm alarm : mAlarms) {
                if (alarm.getKey().equals(key)) {
                    int foundPos = mAlarms.indexOf(alarm);
                    mAlarms.remove(alarm);
                    return foundPos;
                }
            }
            return -1;
        }


        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(Constants.TAG, "My alarm: " + dataSnapshot);
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
        private TextView mAlarmTitleTextView;
        private TextView mAlarmDescriptionTextView;
        private TextView mAlarmTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mAlarmTitleTextView = (TextView) itemView.findViewById(R.id.title_alarm_text);
            mAlarmDescriptionTextView = (TextView) itemView.findViewById(R.id.description_alarm_text);
            mAlarmTimeTextView = (TextView) itemView.findViewById(R.id.time_alarm_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Alarm alarm = mAlarms.get(getAdapterPosition());
            mAlarmSelectedListener.onAlarmSelected(alarm);
        }

        @Override
        public boolean onLongClick(View v) {
            Alarm alarm = mAlarms.get(getAdapterPosition());
            //mNoteListFragment.showNoteDialog(course);
            return true;
        }
    }
}
