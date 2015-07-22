package com.jskaleel.fte.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {

    private static final String PREFS_NAME = "user_preference";
    private static final String BOOK_RESPONSE = "book_response";

    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    public UserPreference(Context context) {
        mContext = context;
        mPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
    }

    public void setBookResponse(String bookResponse){
        mEditor.putString(BOOK_RESPONSE, bookResponse);
        mEditor.commit();
    }

    public String getBookResponse() {
        return mPreference.getString(BOOK_RESPONSE, "");
    }

}
