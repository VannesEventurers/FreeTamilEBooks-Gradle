package com.jskaleel.fte.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.jskaleel.fte.R;

public class AlertUtils {

    public static void showAlert(Context context, String title, String message,
                                 DialogInterface.OnClickListener onClick, boolean cancelable) {
        new AlertDialog.Builder(context).setMessage(message)
                .setTitle(TextUtils.isNullOrEmpty(title) ? context.getString(R.string.app_name) : title)
                .setCancelable(cancelable).setNeutralButton(android.R.string.ok, onClick).create().show();
    }

    public static void showAlert(Context context, String message) {
        showAlert(context, null, message, null, true);
    }

    public static void showAlert(Context context, String title, String message) {
        showAlert(context, title, message, null, true);
    }

    public static void showAlertWithYesNo(Context context, String title, String message,
                                          DialogInterface.OnClickListener onClick, boolean cancelable) {
        new AlertDialog.Builder(context).setMessage(message)
                .setTitle(TextUtils.isNullOrEmpty(title) ? context.getString(R.string.app_name) : title)
                .setCancelable(cancelable).setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, onClick).create().show();
    }

    public static void showAlertWithYesNo(Context context, String message, DialogInterface.OnClickListener onClick) {
        showAlertWithYesNo(context, null, message, onClick, true);
    }

    public static void showAlertWithYesNo(Context context, String title, String message, DialogInterface.OnClickListener onClick) {
        showAlertWithYesNo(context, title, message, onClick, true);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message, View view) {
        int[] values = new int[2];
        view.getLocationOnScreen(values);
        // int x = values[0];
        int y = values[1];

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, y);
        toast.show();
    }

    public static void showToastOnTop(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

}
