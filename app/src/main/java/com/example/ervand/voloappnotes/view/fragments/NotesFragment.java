package com.example.ervand.voloappnotes.view.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.model.Note;
import com.example.ervand.voloappnotes.view.activities.MainActivity;
import com.example.ervand.voloappnotes.view.adapters.NoteAdapter;
import butterknife.BindView;

public class NotesFragment extends Fragment implements SwitchFragment{

    private RecyclerView notesListRecyclerView;
    private Note note;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notesListRecyclerView = view.findViewById(R.id.notes_list_rv);
        NoteAdapter adapter = new NoteAdapter(getActivity(), MainActivity.user.getNoteList());
        notesListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_delete_note_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                UpdateAndAddFragment fragment = new UpdateAndAddFragment();
                fragment.setNote(null);
              switchFragment(fragment,R.id.mainContainer,
                      "Update and Add page");
                break;
            case R.id.logout_note:
                note = new Note();
                cancelNotifications(note);
                switchFragment(new LoginFragment(),R.id.mainContainer,"Login page");
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelNotifications(Note notificationId) {
        Intent intent = new Intent(getContext(), BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                notificationId.getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)
                getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void switchFragment(Fragment fragment, int containerId, String TAG) {
        getFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();
    }

    
}
