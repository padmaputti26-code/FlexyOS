package com.flexyos.c71;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    FlexyLauncher launcher;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b); configureSystemBars(); launcher = new FlexyLauncher(this); setContentView(launcher);
    }
    void configureSystemBars() {
        getWindow().setStatusBarColor(Color.TRANSPARENT); getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }
    @Override protected void onResume(){super.onResume(); if(launcher!=null){launcher.reloadApps();launcher.invalidate();}}

    class FlexyLauncher extends View {
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); ArrayList<AppEntry> apps=new ArrayList<>();
        boolean drawer=false; float downX,downY; long downTime;
        FlexyLauncher(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);reloadApps();}
        int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
        void reloadApps(){apps=AppRepository.load(MainActivity.this);}
        @Override protected void onDraw(Canvas c){int w=getWidth(),h=getHeight();GlassRenderer.background(c,w,h); if(drawer)drawDrawer(c,w,h); else drawHome(c,w,h);}

        void text(Canvas c,String s,float x,float y,float size,int color,Paint.Align align){p.setTypeface(Typeface.create("sans",Typeface.NORMAL));p.setTextAlign(align);p.setTextSize(dp(size));p.setColor(color);c.drawText(s,x,y,p);}

        void drawHome(Canvas c,int w,int h){
            // Search pill matching the reference layout.
            GlassRenderer.panel(MainActivity.this,c,dp(48),dp(92),w-dp(48),dp(150),dp(32));
            text(c,"G",dp(76),dp(129),27,0xFF4285F4,Paint.Align.CENTER);
            text(c,"⌕",w-dp(82),dp(130),30,0xFF667781,Paint.Align.CENTER);

            // Date/time kept subtle so the wallpaper and icons remain dominant.
            text(c,new SimpleDateFormat("HH:mm").format(new Date()),dp(22),dp(38),16,0xEEFFFFFF,Paint.Align.LEFT);

            int cols=4; int top=dp(192); int cell=w/cols; int tile=dp(70); int gapY=dp(112);
            int count=Math.min(12,apps.size());
            for(int i=0;i<count;i++){
                int row=i/cols,col=i%cols; float cx=cell*col+cell/2f; float x=cx-tile/2f; float y=top+row*gapY;
                drawThemedIcon(c,apps.get(i),x,y,tile);
                String n=apps.get(i).label; if(n.length()>12)n=n.substring(0,12)+"…";
                text(c,n,cx,y+tile+dp(23),11,0xF2FFFFFF,Paint.Align.CENTER);
            }

            // Page indicator and glass dock.
            text(c,"•  •",w/2f,h-dp(112),13,0xDDFFFFFF,Paint.Align.CENTER);
            GlassRenderer.panel(MainActivity.this,c,dp(38),h-dp(94),w-dp(38),h-dp(16),dp(30));
            int dockCount=Math.min(4,apps.size());
            for(int i=0;i<dockCount;i++){
                float cx=(i+.5f)*w/dockCount; drawThemedIcon(c,apps.get(i),cx-dp(27),h-dp(82),dp(54));
            }
        }

        void drawThemedIcon(Canvas c,AppEntry a,float x,float y,float size){
            // Translucent rounded tile + white/cyan icon treatment like the supplied theme.
            Paint q=new Paint(Paint.ANTI_ALIAS_FLAG); q.setStyle(Paint.Style.FILL);
            q.setColor(0xB9DDF2F5); q.setShadowLayer(10,0,4,0x38000000); c.drawRoundRect(x,y,x+size,y+size,dp(20),dp(20),q); q.clearShadowLayer();
            q.setStyle(Paint.Style.STROKE); q.setStrokeWidth(1.4f); q.setColor(0x99FFFFFF); c.drawRoundRect(x,y,x+size,y+size,dp(20),dp(20),q);
            Drawable d=a.icon; d.setBounds((int)x+dp(13),(int)y+dp(13),(int)(x+size-dp(13)),(int)(y+size-dp(13)));
            d.setTint(0xFFF8FFFF); d.draw(c);
        }

        void drawDrawer(Canvas c,int w,int h){
            p.setColor(0xB91B2830);c.drawRect(0,0,w,h,p);
            text(c,"Apps",dp(24),dp(72),28,Color.WHITE,Paint.Align.LEFT);
            int cols=4,cell=w/cols,top=dp(122),tile=dp(62);
            for(int i=0;i<apps.size();i++){int row=i/cols,col=i%cols;float cx=cell*col+cell/2f;float y=top+row*dp(96);drawThemedIcon(c,apps.get(i),cx-tile/2,y,tile);String n=apps.get(i).label;if(n.length()>12)n=n.substring(0,12)+"…";text(c,n,cx,y+tile+dp(20),10.5f,0xF0FFFFFF,Paint.Align.CENTER);}
        }

        @Override public boolean onTouchEvent(MotionEvent e){
            if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();downTime=e.getEventTime();return true;}
            if(e.getAction()==MotionEvent.ACTION_UP){float dx=e.getX()-downX,dy=e.getY()-downY;
                if(Math.abs(dx)<dp(15)&&Math.abs(dy)<dp(15)&&e.getEventTime()-downTime>550){startActivity(new Intent(MainActivity.this,SettingsActivity.class));return true;}
                if(!drawer&&dy<-dp(55)){drawer=true;invalidate();return true;} if(drawer&&dy>dp(55)){drawer=false;invalidate();return true;}
                if(!drawer&&dy<dp(30)&&downY>dp(170)){int cols=4,cell=getWidth()/cols,tile=dp(70),top=dp(192),row=(int)((downY-top)/dp(112)),col=(int)(downX/cell),idx=row*cols+col;if(row>=0&&col>=0&&idx<Math.min(12,apps.size())){Intent in=getPackageManager().getLaunchIntentForPackage(apps.get(idx).packageName);if(in!=null)startActivity(in);}}
                if(drawer){int cols=4,cell=getWidth()/cols,row=(int)((downY-dp(122))/dp(96)),col=(int)(downX/cell),idx=row*cols+col;if(row>=0&&col>=0&&idx<apps.size()){Intent in=getPackageManager().getLaunchIntentForPackage(apps.get(idx).packageName);if(in!=null)startActivity(in);}}
                return true;
            } return true;
        }
    }
}
