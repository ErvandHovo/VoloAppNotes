package com.example.ervand.voloappnotes.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
import io.realm.RealmObject;

public class Note extends RealmObject implements Parcelable{

    private int notificationId = 0;
    private String title;
    private String description;
    private int color;
    private boolean notificationState;
    private Date date;

    public Note() {
    }

    public Note(String title, String description,
                int color, boolean notificationState, Date date) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.notificationState = notificationState;
        this.date = date;
    }

    public Note(Parcel in) {
        title = in.readString();
        description = in.readString();
        color = in.readInt();
        notificationState = in.readByte() != 0;
        date = (Date) in.readSerializable();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNotificationState() {
        return notificationState;
    }

    public void setNotificationState(boolean notificationState) {
        this.notificationState = notificationState;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", color=" + color +
                ", notificationState=" + notificationState +
                ", date=" + date +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(color);
        parcel.writeByte((byte) (notificationState ? 1 : 0));
        parcel.writeSerializable(date);
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
