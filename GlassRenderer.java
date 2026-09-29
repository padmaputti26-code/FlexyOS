package com.flexyos.c71;

import android.content.Context;
import android.graphics.*;

public final class GlassRenderer {
    private GlassRenderer() {}

    public static void background(Canvas c, int w, int h) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Cool ocean / blue-grey wallpaper inspired by the supplied C71 reference.
        p.setShader(new LinearGradient(0, 0, 0, h * .72f,
                Color.rgb(116, 139, 151), Color.rgb(45, 61, 69), Shader.TileMode.CLAMP));
        c.drawRect(0, 0, w, h, p);
        p.setShader(null);

        // Dark rocky foreground.
        Path rocks = new Path();
        rocks.moveTo(0, h * .58f);
        rocks.cubicTo(w*.12f,h*.53f,w*.22f,h*.67f,w*.38f,h*.62f);
        rocks.cubicTo(w*.56f,h*.56f,w*.70f,h*.70f,w*.84f,h*.61f);
        rocks.cubicTo(w*.91f,h*.57f,w*.97f,h*.63f,w,h*.59f);
        rocks.lineTo(w,h); rocks.lineTo(0,h); rocks.close();
        p.setColor(0xFF151D21); c.drawPath(rocks,p);

        // Layered water bands.
        for (int i=0;i<8;i++) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(dp(c, 5+i*2));
            p.setColor(0x553E98A7);
            Path wave = new Path();
            float yy = h*.70f + i*h*.035f;
            wave.moveTo(-20, yy);
            wave.cubicTo(w*.22f, yy-h*.045f, w*.42f, yy+h*.055f, w*.67f, yy-h*.015f);
            wave.cubicTo(w*.83f, yy-h*.045f, w*.93f, yy+h*.03f, w+20, yy-h*.01f);
            c.drawPath(wave,p);
        }
        p.setStyle(Paint.Style.FILL);
        p.setShader(new RadialGradient(w*.52f,h*.74f,w*.58f,
                new int[]{0x55B9EEF4,0x001C6975},null,Shader.TileMode.CLAMP));
        c.drawCircle(w*.52f,h*.74f,w*.58f,p);
        p.setShader(null);
    }

    private static float dp(Canvas c, float v) { return v; }

    public static void panel(Context ctx, Canvas c, float l, float t, float r, float b, float radius) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        int alpha = ThemeEngine.glass(ctx) * 255 / 100;
        p.setStyle(Paint.Style.FILL);
        p.setColor((alpha << 24) | 0xFFFFFF);
        p.setShadowLayer(16, 0, 6, 0x45000000);
        c.drawRoundRect(l,t,r,b,radius,radius,p);
        p.clearShadowLayer();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1.5f);
        p.setColor(0x75FFFFFF);
        c.drawRoundRect(l,t,r,b,radius,radius,p);
        p.setStyle(Paint.Style.FILL);
    }
}
