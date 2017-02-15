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
import edu.rosehulman.finngw.quicknotes.models.Alarm;


public class AlarmDetailFragment extends Fragment {

    private static final String ARG_ALARM = "alarm";
    private static Callback mCallback;
    private Alarm mAlarm;

    public AlarmDetailFragment() {
        // Required empty public constructor
    }

    public static AlarmDetailFragment newInstance(Alarm alarm) {
        AlarmDetailFragment fragment = new AlarmDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALARM, alarm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlarm = getArguments().getParcelable(ARG_ALARM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_detail, container, false);
        final EditText titleView = (EditText) view.findViewById(R.id.alarm_detail_title);
        final EditText descriptionText = (EditText) view.findViewById(R.id.alarm_detail_description);
        titleView.setText(mAlarm.getTitle());
        descriptionText.setText(mAlarm.getDescription());
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.editText(mAlarm, titleView.getText().toString(), descriptionText.getText().toString());
            }
        });
        return view;
    }

    public interface Callback {
        public void editText(Alarm mAlarm, String s, String s1);
    }
}
