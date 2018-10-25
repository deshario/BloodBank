package com.deshario.bloodbank.Helper;

/**
 * Created by Deshario on 1/16/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "DesharioBloodbank";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String FIREBASE_TOKEN = "firebase_token";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit(); // save changes
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setToken(String token){
        editor.putString(FIREBASE_TOKEN,token);
        editor.commit(); // save changes
    }

    public String getToken(){
        return pref.getString(FIREBASE_TOKEN,null);
    }
}