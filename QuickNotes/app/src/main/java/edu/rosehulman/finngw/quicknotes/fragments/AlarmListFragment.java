package edu.rosehulman.finngw.quicknotes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.adapters.AlarmRecyclerViewAdapter;
import edu.rosehulman.finngw.quicknotes.models.Alarm;

public class AlarmListFragment extends Fragment {

    private AlarmRecyclerViewAdapter mAdapter;

    private AlarmListFragment.OnAlarmSelectedListener mListener;

    public AlarmListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getContext();

        View rootView = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.note_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
        mAdapter = new AlarmRecyclerViewAdapter(this, mListener);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

/*
private void showDeleteConfirmationDialog(final Note note) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(getString(R.string.remove_question_format, note.getName()));
    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Utils.removeCourse(getActivity(), note);
        }
    });
    builder.setNegativeButton(android.R.string.cancel, null);
    builder.create().show();
}
*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AlarmListFragment.OnAlarmSelectedListener) {
            mListener = (AlarmListFragment.OnAlarmSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAlarmSelectedListener {
        void onAlarmSelected(Alarm selectedAlarm);
    }
}
