package edu.rosehulman.finngw.quicknotes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.adapters.NoteRecyclerViewAdapter;
import edu.rosehulman.finngw.quicknotes.models.Note;

public class NoteListFragment extends Fragment {

    private NoteRecyclerViewAdapter mAdapter;

    private OnNoteSelectedListener mListener;

    public NoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getContext();

        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        Log.d("NNNNNN", "inflated note list");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.note_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        registerForContextMenu(recyclerView);
        mAdapter = new NoteRecyclerViewAdapter(this, mListener);
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
        if (context instanceof OnNoteSelectedListener) {
            mListener = (OnNoteSelectedListener) context;
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

    public interface OnNoteSelectedListener {
        void onNoteSelected(Note selectedNote);
    }
}
