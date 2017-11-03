package com.example.ervand.voloappnotes.model;

import java.util.UUID;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private int id = UUID.randomUUID().hashCode();
    private String email;
    private String password;
    private RealmList<Note> noteList;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int id, String email, String password,RealmList<Note> noteList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.noteList = noteList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", noteList=" + noteList +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(RealmList<Note> noteList) {
        this.noteList = noteList;
    }
}
