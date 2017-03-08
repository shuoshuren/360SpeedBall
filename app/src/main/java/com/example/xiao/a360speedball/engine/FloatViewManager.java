package com.example.xiao.a360speedball.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.xiao.a360speedball.view.FloatCircleView;
import com.example.xiao.a360speedball.view.FloatMenuView;

import java.lang.reflect.Field;

/**
 * Created by xiao on 2017/3/6.
 */

public class FloatViewManager {

    private static final String TAG = "xc";

    private static FloatViewManager sInstance;
    /**
     * 通过windowManager来操控浮窗体的显示，隐藏和位置的控制
     */
    private WindowManager mWindowManager;

    private Context mContext;

    private FloatCircleView mCircleView;

    private WindowManager.LayoutParams layoutParams;
    private float orgionX;
    private float orgionY;

    private FloatMenuView menuView;

    /**
     * 触摸的监听
     */
    private View.OnTouchListener mCircleViewOnTouchListener;

    {
        mCircleViewOnTouchListener = new View.OnTouchListener() {
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        orgionX = event.getRawX();
                        orgionY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX();
                        float y = event.getRawY();
                        float dx = x - startX;
                        float dy = y - startY;
                        mCircleView.setDragState(true);
                        layoutParams.x += dx;
                        layoutParams.y += dy;
                        mWindowManager.updateViewLayout(mCircleView, layoutParams);
                        startX = x;
                        startY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        float upX = event.getRawX();
                        float upY = event.getRawY();
                        int centerWidth = getScreenWidth() / 2;
                        if (upX >= centerWidth) {
                            layoutParams.x = getScreenWidth() - mCircleView.getWidth();
                        } else {
                            layoutParams.x = 0;
                        }
                        mCircleView.setDragState(false);
                        mWindowManager.updateViewLayout(mCircleView, layoutParams);

                        float deltX = upX-orgionX;
                        float deltY = upY-orgionY;
                        if(Math.sqrt(deltX*deltX+deltY*deltY)>6){
                            Log.i("xc","false");
                            return true;
                        }else{
                            return false;
                        }

                }


                return false;
            }
        };
    }

    private FloatViewManager(Context context) {
        this.mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mCircleView = new FloatCircleView(mContext);

        Log.i("xc","mContext:"+mContext.getClass().getSimpleName());


        mCircleView.setOnTouchListener(mCircleViewOnTouchListener);
        mCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏CircleView,显示菜单栏
                hideFloatCircleView();
                showFloatMenuView();
                menuView.startAnni();
            }
        });
        menuView = new FloatMenuView(mContext);

    }

    public static FloatViewManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FloatViewManager.class) {
                if (sInstance == null) {
                    sInstance = new FloatViewManager(context);
                }
            }

        }
        return sInstance;
    }

    /**
     * 显示浮窗小球
     */
    public void showFloatCircleView() {
        if(layoutParams== null){
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = mCircleView.getWidth();
            layoutParams.height = mCircleView.getHeight();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        }

        mWindowManager.addView(mCircleView, layoutParams);


    }

    /**
     * 显示菜单栏
     */
    public void showFloatMenuView() {
        Log.i(TAG, "showFloatMenuView: 显示菜单栏");

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.width = getScreenWidth();
        layoutParams.height = getScreenHeight() - getStatusHeight();
        layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        mWindowManager.addView(menuView, layoutParams);


    }

    /**
     * 隐藏浮窗小球
     */
    public void hideFloatCircleView() {

        mWindowManager.removeView(mCircleView);
    }

    /**
     * 隐藏菜单栏
     */
    public void hideFloatMenuView() {
        Log.i(TAG, "hideFloatMenuView: 隐藏菜单栏");
        mWindowManager.removeView(menuView);
    }

    /**
     * 获取屏幕的宽度
     * @return
     */
    public int getScreenWidth() {
        return mWindowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     * @return
     */
    public int getScreenHeight() {
        return mWindowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 获取状态栏的高度
     * @return
     */
    public int getStatusHeight(){
        try{
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = field.getInt(o);
            int height = mContext.getResources().getDimensionPixelSize(x);
            return height;
        }catch (Exception e){

        }
        return 0;


    }


}
