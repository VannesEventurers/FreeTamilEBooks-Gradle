package com.freetamilebooks.freetamilebooks.utils;

import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

public class TextUtils {

    public static final String encodeToBase64(CharSequence content) {
        if (content == null) {
            return null;
        }
        byte[] bytes = Base64.encode(content.toString().getBytes(), Base64.DEFAULT);
        return new String(bytes).trim();
    }

    public static final String encodeToBase64(byte[] data) {
        byte[] bytes = Base64.encode(data, Base64.DEFAULT);
        return new String(bytes).trim();
    }

    public static final String decodeBase64(String base64String) {
        if (base64String == null) {
            return base64String;
        }

        try {
            return new String(Base64.decode(base64String, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return base64String;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    public static boolean isNullOrEmpty(CharSequence value) {
        return value == null || value.toString().equals("");
    }

    public static boolean isEmpty(EditText editText) {
        if (editText != null) {
            return isNullOrEmpty(editText.getText().toString());
        }

        return true;
    }

    public static boolean isEmpty(TextView textView) {
        if (textView != null) {
            return isNullOrEmpty(textView.getText());
        }

        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidWebUrl(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.WEB_URL.matcher(target).matches();
        }
    }

    public static String arrayToString(ArrayList<String> array, String delimiter) {
        StringBuilder builder = new StringBuilder();
        if (array.size() > 0) {
            builder.append(array.get(0));
            for (int i = 1; i < array.size(); i++) {
                builder.append(delimiter);
                builder.append(array.get(i));
            }
        }
        return builder.toString();
    }
}
