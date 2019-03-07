package fr.outlook.marro.laurent.firebaseoc.Models;

import android.support.annotation.Nullable;

public class User {

    private String uid, username, email;
    @Nullable private String photoURL;

    public User() { }

    public User(String uid, String username, String email,  String photoURL) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.photoURL = photoURL;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getEmail() { return email;}
    public String getphotoURL() { return photoURL; }

    // --- SETTERS ---
    public void setUid(String uid) { this.uid = uid; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email;}
    public void setUrlPicture(String photoURL) { this.photoURL = photoURL; }
}