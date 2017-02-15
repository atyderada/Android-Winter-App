package edu.rosehulman.finngw.quicknotes.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.models.Alarm;


public class AlarmDetailFragment extends Fragment {

    private static final String ARG_ALARM = "alarm";
    private static Callback mCallback;
    private Alarm mAlarm;

    public AlarmDetailFragment() {
        // Required empty public constructor
    }

    public static AlarmDetailFragment newInstance(Alarm alarm, Callback callback) {
        mCallback = callback;
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
        final EditText timeText = (EditText) view.findViewById(R.id.alarm_detail_time);

        titleView.setText(mAlarm.getTitle());
        descriptionText.setText(mAlarm.getDescription());
        String time = "";
        if (mAlarm.getTime().length() < 4) {
            time = time + mAlarm.getTime().charAt(0);
            time += ":";
            time += mAlarm.getTime().substring(1, 3);
        } else {
            time = time + mAlarm.getTime().charAt(0) + "" + mAlarm.getTime().charAt(1);
            time += ":";
            time += mAlarm.getTime().substring(2, 4);
        }
        timeText.setText(time);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAlarm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.editAlarm(mAlarm, titleView.getText().toString(), descriptionText.getText().toString());
            }
        });
        return view;
    }

    public interface Callback {
        public void editAlarm(Alarm mAlarm, String s, String s1);
    }
}
