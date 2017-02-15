package edu.rosehulman.finngw.quicknotes.fragments;

import android.support.design.widget.FloatingActionButton;
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
    private static Callback mCallback;
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
        final EditText titleView = (EditText) view.findViewById(R.id.reminder_detail_title);
        final EditText descriptionText = (EditText) view.findViewById(R.id.reminder_detail_description);
        titleView.setText(mReminder.getTitle());
        descriptionText.setText(mReminder.getDescription());
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.editText(mReminder, titleView.getText().toString(), descriptionText.getText().toString());
            }
        });
        return view;
    }

    public interface Callback {
        public void editText(Reminder mReminder, String s, String s1);
    }
}
