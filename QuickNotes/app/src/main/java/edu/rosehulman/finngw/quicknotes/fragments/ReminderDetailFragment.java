package edu.rosehulman.finngw.quicknotes.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.models.Reminder;


public class ReminderDetailFragment extends Fragment {

    private static final String ARG_REMINDER = "reminder";
    private static Callback mCallback;
    private Reminder mReminder;

    public ReminderDetailFragment() {
        // Required empty public constructor
    }

    public static ReminderDetailFragment newInstance(Reminder reminder, Callback callback) {
        mCallback = callback;
        ReminderDetailFragment fragment = new ReminderDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REMINDER, reminder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReminder = getArguments().getParcelable(ARG_REMINDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder_detail, container, false);
        final EditText titleView = (EditText) view.findViewById(R.id.reminder_detail_title);
        final EditText descriptionText = (EditText) view.findViewById(R.id.reminder_detail_description);
        final EditText dateText = (EditText) view.findViewById(R.id.reminder_date_description);
        final EditText completedText = (EditText) view.findViewById(R.id.reminder_completed_description);

        titleView.setText(mReminder.getTitle());
        descriptionText.setText(mReminder.getDescription());
        String date = mReminder.getDate().substring(4,6);
        date += "/" + mReminder.getDate().substring(6, 8);
        date += "/" + mReminder.getDate().substring(0, 4);
        dateText.setText(date);
        completedText.setText("Completed: " + mReminder.getCompleted());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabReminder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.editReminder(mReminder, titleView.getText().toString(), descriptionText.getText().toString());
            }
        });
        return view;
    }

    public interface Callback {
        public void editReminder(Reminder mReminder, String s, String s1);
    }
}
