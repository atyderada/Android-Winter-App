package edu.rosehulman.finngw.quicknotes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.models.Note;


public class NoteDetailFragment extends Fragment {

    private static final String ARG_NOTE = "note";

    private Note mNote;

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    public static NoteDetailFragment newInstance(Note note) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNote = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.note_detail_title);
        EditText descriptionText = (EditText) view.findViewById(R.id.note_detail_description);
        titleView.setText(mNote.getTitle());
        descriptionText.setText(mNote.getDescription());
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

}
