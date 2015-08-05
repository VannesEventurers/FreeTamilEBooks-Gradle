package com.jskaleel.fte.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {

    private static final String PREFS_NAME = "user_preference";
    private static final String BOOK_RESPONSE = "book_response";
    private static final String IS_APP_RATED = "is_app_rated";

    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    public UserPreference(Context context) {
        mContext = context;
        mPreference = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
    }

    public void setIsAppRated(boolean firstTimeInstalled) {
        mEditor.putBoolean(IS_APP_RATED, firstTimeInstalled);
        mEditor.commit();
    }

    public boolean getIsAppRated() {
        return mPreference.getBoolean(IS_APP_RATED, false);
    }

    public void setBookResponse(String bookResponse){
        mEditor.putString(BOOK_RESPONSE, bookResponse);
        mEditor.commit();
    }

    public String getBookResponse() {
        return mPreference.getString(BOOK_RESPONSE, "");
    }

}
