package com.freetamilebooks.freetamilebooks.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class TypefaceUtils {

    public static final int LIGHT = 1;
    public static final int MEDIUM = 2;

    public static final String S_LIGHT = "NotoSansTamil-Regular.ttf";
    public static final String S_MEDIUM = "NotoSansTamil-Bold.ttf";

    public static Typeface getTypeFace(Context context, int tf) {
        String typeFace = null;

        switch (tf) {
            case LIGHT:
                typeFace = S_LIGHT;
                break;

            case MEDIUM:
                typeFace = S_MEDIUM;
                break;

            default:
                typeFace = S_LIGHT;
        }

        AssetManager assets = context.getAssets();
        return Typeface.createFromAsset(assets, "fonts/" + typeFace);
    }

}
