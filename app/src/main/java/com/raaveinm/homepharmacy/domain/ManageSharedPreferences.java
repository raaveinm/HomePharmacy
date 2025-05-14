package com.raaveinm.homepharmacy.domain;

import android.content.Context;
import android.content.SharedPreferences;

public class ManageSharedPreferences {
    private final SharedPreferences sharedPreferences;

    public ManageSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("login_data", Context.MODE_PRIVATE);
    }

    public String getUsername () {
        return (sharedPreferences.getString("username", "ERR_USER_DOES_NOT_EXIST"));
    }

    public Integer getPassword () {
        return (sharedPreferences.getInt("password", -1));
    }

    public void changePassword (Integer password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("password", password);
        editor.apply();
    }

    public void setUsername (String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public void deleteData () {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void loggedIn () {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged_in", true);
        editor.apply();
    }

    public void loggedOut () {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("logged_in", false);
        editor.apply();
    }

    public boolean isLoggedIn () {
        return (sharedPreferences.getBoolean("logged_in", false));
    }
}
