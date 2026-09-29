package com.flexyos.c71;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class SettingsActivity extends Activity {
    int dp(float x) {
        return (int)(x * getResources().getDisplayMetrics().density + .5f);
    }

    TextView title(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextColor(Color.WHITE);
        t.setTextSize(size);
        t.setPadding(dp(20), dp(12), dp(20), dp(12));
        return t;
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, dp(42), 0, 0);

        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(18,23,38), Color.rgb(76,87,120)}
        );
        root.setBackground(bg);

        root.addView(title("FlexyOS", 32));
        root.addView(title("C71 Edition", 14));

        root.addView(title("Appearance", 20));

        SeekBar glass = new SeekBar(this);
        glass.setMax(95);
        glass.setProgress(ThemeEngine.glass(this));
        root.addView(glass);
        TextView glassLabel = title("Glass intensity", 15);
        root.addView(glassLabel);

        glass.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int p, boolean f) {
                ThemeEngine.setGlass(SettingsActivity.this, p);
                glassLabel.setText("Glass intensity: " + p + "%");
            }
            public void onStartTrackingTouch(SeekBar b) {}
            public void onStopTrackingTouch(SeekBar b) {}
        });

        SeekBar blur = new SeekBar(this);
        blur.setMax(100);
        blur.setProgress(ThemeEngine.blur(this));
        root.addView(blur);
        TextView blurLabel = title("Blur intensity", 15);
        root.addView(blurLabel);

        blur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int p, boolean f) {
                ThemeEngine.setBlur(SettingsActivity.this, p);
                blurLabel.setText("Blur intensity: " + p + "%");
            }
            public void onStartTrackingTouch(SeekBar b) {}
            public void onStopTrackingTouch(SeekBar b) {}
        });

        TextView info = title(
                "FlexyOS adapts to Android/OEM capabilities. " +
                "Protected realme system UI cannot be replaced by a normal launcher.",
                13
        );
        info.setTextColor(0xCCFFFFFF);
        root.addView(info);

        setContentView(root);
    }
}
