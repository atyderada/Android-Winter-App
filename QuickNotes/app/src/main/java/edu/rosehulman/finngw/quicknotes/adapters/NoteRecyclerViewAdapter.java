package edu.rosehulman.finngw.quicknotes.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.rosehulman.finngw.quicknotes.R;
import edu.rosehulman.finngw.quicknotes.fragments.NoteDetailFragment;
import edu.rosehulman.finngw.quicknotes.fragments.NoteListFragment;
import edu.rosehulman.finngw.quicknotes.models.Note;
import edu.rosehulman.finngw.quicknotes.utilities.Constants;
import edu.rosehulman.finngw.quicknotes.utilities.SharedPreferencesUtils;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> implements NoteDetailFragment.Callback {

    private final NoteListFragment mNoteListFragment;

    private String mUid;
    private DatabaseReference mNotesRef;
    private ArrayList<Note> mNotes = new ArrayList<>();
    private final NoteListFragment.OnNoteSelectedListener mNoteSelectedListener;

    public NoteRecyclerViewAdapter(NoteListFragment noteListFragment, NoteListFragment.OnNoteSelectedListener listener) {

        mNoteListFragment = noteListFragment;
        mNoteSelectedListener = listener;

        mUid = SharedPreferencesUtils.getCurrentUser(noteListFragment.getContext());

        assert (!mUid.isEmpty()); // Consider: use if (BuildConfig.DEBUG)

        mNotesRef = FirebaseDatabase.getInstance().getReference(Constants.NOTES_PATH);
        Query mNotesQuery = mNotesRef.orderByChild("uid").equalTo(mUid);
        mNotesQuery.addChildEventListener(new NoteRecyclerViewAdapter.NotesChildEventListener());
    }

    public void firebasePush(String noteTitle, String noteDescription) {
        Note note = new Note(noteTitle, noteDescription, mUid);
        DatabaseReference noteRef = mNotesRef.push();
        String noteKey = noteRef.getKey();
        noteRef.setValue(note);
    }

    public void firebaseEdit(Note note, String newNoteTitle, String newNoteDescription) {
        note.setTitle(newNoteTitle);
        note.setDescription(newNoteDescription);
        mNotesRef.child(note.getKey()).setValue(note);
    }

    public void firebaseRemove(Note noteToRemove) {
        mNotesRef.child(noteToRemove.getKey()).removeValue();
    }

    @Override
    public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_note, parent, false);
        return new NoteRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mNoteTitleTextView.setText(mNotes.get(position).getTitle());
        holder.mNoteDescriptionTextView.setText(mNotes.get(position).getDescription());
    }

    @Override
    public int getItemCount() { return mNotes.size(); }

    @Override
    public void editText(Note mNote, String s, String s1) {
        firebaseEdit(mNote, s, s1);
    }

    class NotesChildEventListener implements ChildEventListener {
        // While we don't push up deletes, we need to listen for other owners deleting our course.

        private void add(DataSnapshot dataSnapshot) {
            Note note = dataSnapshot.getValue(Note.class);
            note.setKey(dataSnapshot.getKey());
            mNotes.add(0, note);
        }

        private int remove(String key) {
            for (Note note : mNotes) {
                if (note.getKey().equals(key)) {
                    int foundPos = mNotes.indexOf(note);
                    mNotes.remove(note);
                    return foundPos;
                }
            }
            return -1;
        }


        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(Constants.TAG, "My course: " + dataSnapshot);
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int position = remove(dataSnapshot.getKey());
            if (position >= 0) {
                notifyItemRemoved(position);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(DatabaseError firebaseError) {
            Log.e("TAG", "onCancelled. Error: " + firebaseError.getMessage());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mNoteTitleTextView;
        private TextView mNoteDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNoteTitleTextView = (TextView) itemView.findViewById(R.id.title_note_text);
            mNoteDescriptionTextView = (TextView) itemView.findViewById(R.id.description_note_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferencesUtils.setCurrentCourseKey(mNoteListFragment.getContext(), mNotes.get(getAdapterPosition()).getKey());
            Note note = mNotes.get(getAdapterPosition());
            mNoteSelectedListener.onNoteSelected(note);
        }

        @Override
        public boolean onLongClick(View v) {
            Note course = mNotes.get(getAdapterPosition());
            mNoteListFragment.showNoteDialog(course);
            return true;
        }
    }
}
