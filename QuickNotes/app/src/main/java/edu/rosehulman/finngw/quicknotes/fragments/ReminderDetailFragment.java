package edu.rosehulman.finngw.quicknotes.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.models.Reminder;


public class ReminderDetailFragment extends Fragment {

    private static final String ARG_REMINDER = "reminder";

    private Reminder mReminder;

    public ReminderDetailFragment() {
        // Required empty public constructor
    }

    public static ReminderDetailFragment newInstance(Reminder reminder) {
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
        TextView titleView = (TextView) view.findViewById(R.id.reminder_detail_title);
        EditText descriptionText = (EditText) view.findViewById(R.id.reminder_detail_description);
        titleView.setText(mReminder.getTitle());
        descriptionText.setText(mReminder.getDescription());
        return view;
    }

}
