package com.deshario.bloodbank.Configs;

/**
 * Created by Deshario on 1/16/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager{

    private static String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences pref;
    private Editor editor;
    private Context _context;

    private String username;
    private String firebase_token;
    private String blood_group;
    private String phone;
    private String created;

    private static final String PREF_NAME = "bloodbank";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String FIREBASE_TOKEN = "token";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void clearAll(){
        SharedPreferences settings = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public void clearSingle(String KeyName){
        SharedPreferences settings = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        settings.edit().remove(KeyName).apply();
    }

//    public void setToken(String token){
//        editor.putString(FIREBASE_TOKEN,token);
//        editor.commit(); // save changes
//    }
//
//    public String getToken(){
//        return pref.getString(FIREBASE_TOKEN,null);
//    }
}