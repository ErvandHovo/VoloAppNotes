package com.example.ervand.voloappnotes.view.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.model.Note;
import com.example.ervand.voloappnotes.view.activities.MainActivity;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class UpdateAndAddFragment extends Fragment implements View.OnClickListener,
        SwitchFragment {

    private String title;
    private String description;
    private String dateInfo;
    private String timeInfo;
    private Calendar calendar;
    private int color;
    private boolean notificationEnabled;
    private Note note;
    private Date date;
    private Unbinder unbinder;
    private Realm realm;

    @BindView(R.id.txtDateText)
    public TextView txtDateText;

    @BindView(R.id.txtTimeText)
    public TextView txtTimeText;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtDescription)
    EditText edtDescription;

    @BindView(R.id.btnColorChooser)
    TextView txtbtnColorChooser;

    @BindView(R.id.txtNotificationState)
    TextView txtNotificationState;

    @BindView(R.id.chbNotificationState)
    CheckBox chbNotificationState;

    public UpdateAndAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_and_add,
                container, false);

        unbinder = ButterKnife.bind(this, view);

        color = Color.BLACK;
        return view;
    }


    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();
        calendar = Calendar.getInstance();
        if (savedInstanceState != null) {
            updateFieldsSavedInstanceStateWhenNotNullSavedInstanceState(savedInstanceState);
        } else if (note != null) {
            updateFieldsSavedInstanceStateWhenNotNullNote();
        }
        updateFieldsWhenClickedItemView();
    }

    private void openDatePicker() {
        DatePickerDialog.OnDateSetListener setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                txtDateText.setText(calendar.get(Calendar.YEAR) + " / " +
                        calendar.get(Calendar.MONTH) + " / " + calendar.get(Calendar.DAY_OF_MONTH));
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(this.getContext(),
                setListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private void openTimePicker() {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                txtTimeText.setText(calendar.get(Calendar.HOUR_OF_DAY) + " : "
                        + calendar.get(Calendar.MINUTE));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                listener, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void openColorPickerDialog() {
        ColorPickerDialogBuilder.with(getContext()).initialColor(0).
                setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {

                    }
                }).
                setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] integers) {
                        color = selectedColor;
                        txtbtnColorChooser.setTextColor(color);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int cancel) {
                        int colorCancel = Color.BLACK;
                        txtbtnColorChooser.setTextColor(colorCancel);
                    }
                }).
                build().show();
    }

    public void onSaveButtonClick() {
        //create new note
        if (!edtTitle.getText().toString().equals("") &&
                !edtDescription.getText().toString().equals("")) {
            if (note == null) {
                createNewNote();
            } else {
                //update note
                updateFieldsWhenClickedSaveButton();
            }
            if (chbNotificationState.isChecked()) {
                notificationCreate();
            }
            Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please input fields Title and Description",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewNote() {
        date = new Date();
        date.setTime(calendar.getTimeInMillis());

        Note noteItem = new Note(
                edtTitle.getText().toString(),
                edtDescription.getText().toString(),
                txtbtnColorChooser.getCurrentTextColor(),
                chbNotificationState.isChecked(),
                date);
        realm.beginTransaction();
        MainActivity.user.getNoteList().add(noteItem);
        realm.commitTransaction();
        switchFragment(new NotesFragment(), R.id.mainContainer, "Note page");
    }

    @OnClick({R.id.btnColorChooser, R.id.btnDateChooser,
            R.id.btnTimeChooser, R.id.btnSaveNote, R.id.chbNotificationState, R.id.btnDeleteNote,
            R.id.lLayoutUpdateAndAddFragment})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnColorChooser:
                openColorPickerDialog();
                break;
            case R.id.btnDateChooser:
                openDatePicker();
                break;
            case R.id.btnTimeChooser:
                openTimePicker();
                break;
            case R.id.btnSaveNote:
                onSaveButtonClick();
                break;
            case R.id.btnDeleteNote:
                openDialogDelete();
                break;
            case R.id.chbNotificationState:
                if (chbNotificationState.isChecked()) {
                    txtNotificationState.setText(R.string.notification_state_on);
                } else {
                    txtNotificationState.setText(R.string.notification_state_off);
                }
                break;
            case R.id.lLayoutUpdateAndAddFragment:
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }

    private void openDialogDelete() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("Delete note");
        dialog.setMessage("Are you sure you want to delete this entry?");
        dialog.setNegativeButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realm.beginTransaction();
                MainActivity.user.getNoteList().remove(note);
                realm.commitTransaction();
                switchFragment(new NotesFragment(), R.id.mainContainer, "Notes page");
            }
        })
                .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = dialog.create();
        alert.show();
    }


    private void notificationCreate() {
        Intent intent = new Intent();
        intent.setAction("voloappnotes.notify");
        intent.putExtra("description", edtDescription.getText().toString());
        intent.putExtra("title", edtTitle.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().
                getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void updateFieldsWhenClickedSaveButton() {
        realm.beginTransaction();
        String string = txtTimeText.getText().toString() + txtDateText.getText().toString();
        DateFormat format = new SimpleDateFormat("MMMM dd HH:mm:ss zzzz yyyy",
                Locale.ENGLISH);
        try {
            Date updateDate = format.parse(string);
            note.setDate(updateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        note.setTitle(edtTitle.getText().toString());
        note.setDescription(edtDescription.getText().toString());
        note.setColor(color);
        note.setNotificationState(chbNotificationState.isChecked());
        realm.commitTransaction();
        switchFragment(new NotesFragment(), R.id.mainContainer, "Notes page");
    }

    private void updateFieldsSavedInstanceStateWhenNotNullSavedInstanceState(Bundle bundle) {
        title = bundle.getString("title");
        description = bundle.getString("description");
        dateInfo = bundle.getString("dateInfo");
        timeInfo = bundle.getString("timeInfo");
        calendar = (Calendar) bundle.getSerializable("calendar");
        color = bundle.getInt("color");
        notificationEnabled = bundle.getBoolean("notificationEnabled");
        note = bundle.getParcelable("noteItem");
    }

    private void updateFieldsSavedInstanceStateWhenNotNullNote() {
        title = note.getTitle();
        description = note.getDescription();
        dateInfo = String.valueOf(note.getDate().getDate());
        timeInfo = String.valueOf(note.getDate().getTime());
        color = note.getColor();
        notificationEnabled = note.isNotificationState();
    }

    private void updateFieldsWhenClickedItemView() {
        edtTitle.setText(title);
        edtDescription.setText(description);
        txtDateText.setText(dateInfo);
        txtTimeText.setText(timeInfo);
        txtbtnColorChooser.setTextColor(color);
        chbNotificationState.setChecked(notificationEnabled);
    }

    @Override
    public void switchFragment(Fragment fragment, int containerId, String TAG) {
        getFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", edtTitle.getText().toString());
        outState.putString("description", edtDescription.getText().toString());
        outState.putString("dateInfo", dateInfo);
        outState.putString("timeInfo", timeInfo);
        outState.putSerializable("calendar", calendar);
        outState.putInt("color", color);
        outState.putBoolean("notificationEnabled", chbNotificationState.isChecked());
        outState.putParcelable("note", note);
    }
}
