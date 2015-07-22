package com.jskaleel.fte.utils;

import android.util.Log;

public class FTELog {

    public static void print(String str) {
        if (str.length() > 4000) {
            Log.e("IMDLog", str.substring(0, 4000));
            print(str.substring(4000));
        } else {
            Log.e("IMDLog", str);
        }
    }
}
