package com.example.ervand.voloappnotes.view.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.model.Note;
import com.example.ervand.voloappnotes.view.activities.MainActivity;
import com.example.ervand.voloappnotes.view.fragments.SwitchFragment;
import com.example.ervand.voloappnotes.view.fragments.UpdateAndAddFragment;
import com.example.ervand.voloappnotes.view.viewHolders.NoteViewHolder;
import io.realm.RealmList;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> implements
        View.OnClickListener, SwitchFragment {

    private LayoutInflater inflater;
    private Context context;
    private RealmList<Note> noteList;
    private MainActivity activity;

    public NoteAdapter(Context context, RealmList<Note> noteList) {
        this.noteList = noteList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        if (noteList != null) {
            final Note noteModel = noteList.get(position);
            holder.initData(noteModel);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity = (MainActivity) view.getContext();
                    UpdateAndAddFragment updateAndAddFragment = new UpdateAndAddFragment();
                    updateAndAddFragment.setNote(noteList.get(position));
                    switchFragment(updateAndAddFragment, R.id.mainContainer,
                            "Update and Add page");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (noteList == null) {
            noteList = new RealmList<>();
        }
        return noteList.size();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void switchFragment(Fragment fragment, int containerId, String TAG) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment).addToBackStack(null).commit();
    }
}
