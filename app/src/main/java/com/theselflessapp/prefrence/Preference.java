package com.theselflessapp.prefrence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.google.gson.Gson;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.modal.LogInPOJO;


public class Preference implements Constant {

    static SharedPreferences sharedpreferences;
    static SharedPreferences.Editor editor;
    Context context;

    public Preference(final Context context) {
        this.context = context;
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        editor = sharedpreferences.edit();
    }

    public static void setUserId(Context ctx, String user_id) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(KEY_USER_ID, user_id);
        editor.commit();
    }

    public static String getUserId(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String user_id = sharedpreferences.getString(KEY_USER_ID, null);
        return user_id;
    }

    public static void setUser(Context ctx, LogInPOJO login) {
        Gson gson = new Gson();
        String json_user = gson.toJson(login);
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(KEY_USER, json_user);
        editor.commit();
    }

    public static LogInPOJO getUser(Context ctx) {
        Gson gson = new Gson();
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String json_user = sharedpreferences.getString(KEY_USER, null);
        LogInPOJO user = gson.fromJson(json_user, LogInPOJO.class);
        return user;
    }


    public final void setData(final String key, final String data) {
        editor.putString(key, data);
        editor.commit();
    }


    public final String getData(final String key) {
        return sharedpreferences.getString(key, null);
    }

    public static void setLogOut(Context ctx, boolean isLogOut) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putBoolean(KEY_LOGOUT, isLogOut);
        editor.commit();
    }

    public static Boolean getLogOut(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(KEY_LOGOUT, true);
    }

    public static void setRememberMe(Context ctx, Boolean isRemind) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putBoolean(KEY_IS_REMEMBER, isRemind);
        editor.commit();
    }

    public static boolean getRememberMe(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        boolean isRemind = sharedpreferences.getBoolean(KEY_IS_REMEMBER, false);
        return isRemind;
    }

    public static void setRememberMeCredentials(Context ctx, String username, String password) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public static String getUserName(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String userName = sharedpreferences.getString(KEY_USERNAME, null);
        return userName;
    }


    public static String getPassword(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String password = sharedpreferences.getString(KEY_PASSWORD, null);
        return password;
    }
}
