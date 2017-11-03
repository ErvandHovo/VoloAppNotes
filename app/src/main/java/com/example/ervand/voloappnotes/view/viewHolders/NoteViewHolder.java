package com.example.ervand.voloappnotes.view.viewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.model.Note;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView txtTitle, txtDescription, txtDate;
    View viewColor;
    CheckBox checkBoxNotification;

    public NoteViewHolder(View itemView) {
        super(itemView);

        this.txtTitle = itemView.findViewById(R.id.txtTitle);
        this.txtDescription = itemView.findViewById(R.id.txtDescription);
        this.txtDate = itemView.findViewById(R.id.txtDate);
        this.viewColor = itemView.findViewById(R.id.viewColor);
        this.checkBoxNotification = itemView.findViewById(R.id.checkboxNotification);
    }

    public void initData(Note note) {
        txtTitle.setText(note.getTitle());
        txtDescription.setText(note.getDescription());
        if (note.getDate() == null){
            txtDate.setText("Date And Time");
        }else {
            txtDate.setText(note.getDate().toString());
        }
        viewColor.setBackgroundColor(note.getColor());
        checkBoxNotification.setChecked(note.isNotificationState());
    }
}
