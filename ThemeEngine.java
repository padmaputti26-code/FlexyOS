package com.flexyos.c71;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public final class ThemeEngine {
    private static final String PREF = "flexy_theme";
    private static final String KEY_GLASS = "glass_strength";
    private static final String KEY_BLUR = "blur_strength";
    private static final String KEY_ACCENT = "accent";

    private ThemeEngine() {}

    public static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static int glass(Context c) {
        return prefs(c).getInt(KEY_GLASS, 58);
    }

    public static int blur(Context c) {
        return prefs(c).getInt(KEY_BLUR, 50);
    }

    public static int accent(Context c) {
        return prefs(c).getInt(KEY_ACCENT, Color.rgb(160, 190, 255));
    }

    public static void setGlass(Context c, int v) {
        prefs(c).edit().putInt(KEY_GLASS, Math.max(20, Math.min(95, v))).apply();
    }

    public static void setBlur(Context c, int v) {
        prefs(c).edit().putInt(KEY_BLUR, Math.max(0, Math.min(100, v))).apply();
    }

    public static void setAccent(Context c, int color) {
        prefs(c).edit().putInt(KEY_ACCENT, color).apply();
    }
}
