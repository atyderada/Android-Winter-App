package edu.rosehulman.finngw.quicknotes.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.activities.MainActivity;
import edu.rosehulman.finngw.quicknotes.adapters.ReminderRecyclerViewAdapter;
import edu.rosehulman.finngw.quicknotes.models.Reminder;

public class ReminderListFragment extends Fragment {

    private ReminderRecyclerViewAdapter mAdapter;

    private ReminderListFragment.OnReminderSelectedListener mListener;

    public ReminderListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getContext();

        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.note_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
        mAdapter = new ReminderRecyclerViewAdapter(this, mListener);
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
    public void showReminderDialog(Reminder reminder) {
        AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity)getActivity());
        builder.setTitle("Reminder Options");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Edit", null);
        builder.setNeutralButton("Remove", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Show editing
            }
        });
        builder.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof edu.rosehulman.finngw.quicknotes.fragments.NoteListFragment.OnNoteSelectedListener) {
            mListener = (ReminderListFragment.OnReminderSelectedListener) context;
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

    public interface OnReminderSelectedListener {
        void onReminderSelected(Reminder selectedReminder);
    }
}

