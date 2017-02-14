package edu.rosehulman.finngw.quicknotes.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.activities.MainActivity;

public class BaseFragment extends Fragment {

    private Toolbar mToolbar;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.base_content, container, false);

        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setBackgroundColor(Color.WHITE);

        final TextView mTitleTextView = (TextView)rootView.findViewById(R.id.title_input);
        final TextView mDescriptionTextView = (TextView)rootView.findViewById(R.id.description_input);

        ImageButton mAlarmImgeView = (ImageButton) rootView.findViewById(R.id.alarm_button);
        mAlarmImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addAlarm(mTitleTextView.getText().toString(), mDescriptionTextView.getText().toString());
                mTitleTextView.setText("");
                mDescriptionTextView.setText("");
            }
        });

        ImageButton mReminderImageView = (ImageButton)rootView.findViewById(R.id.reminder_button);
        mReminderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addReminder(mTitleTextView.getText().toString(), mDescriptionTextView.getText().toString());
                mTitleTextView.setText("");
                mDescriptionTextView.setText("");
            }
        });

        ImageButton mNoteImageView = (ImageButton)rootView.findViewById(R.id.note_button);
        mNoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).addNote(mTitleTextView.getText().toString(), mDescriptionTextView.getText().toString());
                mTitleTextView.setText("");
                mDescriptionTextView.setText("");
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}