package com.example.xiao.a360speedball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiao on 2017/3/7.
 */

public class ProgressView extends View {

    private int width = 200;
    private int height = 200;
    private Paint circlePaint;
    private Paint progressPaint;
    private Paint textPaint;

    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private Path path = new Path();
    private int progress = 50;
    private int maxProgress = 100;

    private int currentProgress = 50;
    private int  count = 50;

    private boolean isSingleTap = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };



    GestureDetector detector;

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    private void initView() {

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.argb(0xff,0x3a,0x8c,0x6c));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.argb(0xff,0x4e,0xc9,0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);
        textPaint.setFakeBoldText(true);


        bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);


        detector = new GestureDetector(new ProgressGestureListener());

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });

        setClickable(true);


    }

    public class ProgressGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            isSingleTap = true;

            Log.i("xc","单击");
            startSingleTapAnni();

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            Log.i("xc","双击");

            startDoubleTapAnni();
            return super.onDoubleTap(e);
        }
    }

    /**
     * 开启单击动画
     */
    private void startSingleTapAnni() {
        handler.postDelayed(singleRun,200);
    }

    private SingleTapRunnable singleRun = new SingleTapRunnable();

    class SingleTapRunnable implements Runnable{
        @Override
        public void run() {
            count--;
            if(count>=0){
                invalidate();
                handler.postDelayed(singleRun,200);
            }else{
                isSingleTap = false;
                handler.removeCallbacks(singleRun);
                count=50;
            }

        }
    }

    /**
     * 开启双击的动画
     */
    private void startDoubleTapAnni() {

        handler.postDelayed(doubleRun,50);

    }

    private DoubleTapRunnable doubleRun = new DoubleTapRunnable();

    class DoubleTapRunnable implements Runnable{
        @Override
        public void run() {
            currentProgress++;
            if(currentProgress<=progress){
                handler.postDelayed(doubleRun,50);
                invalidate();
            }else{
                handler.removeCallbacks(doubleRun);
                currentProgress = 0;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmapCanvas.drawCircle(width/2,height/2,width/2,circlePaint);
        path.reset();
        float y = (float)(1-currentProgress*1.0f/maxProgress)*height;
        path.moveTo(width,y);
        path.lineTo(width,height);
        path.lineTo(0,height);
        path.lineTo(0,y);
        if(!isSingleTap){
            float d = (1-currentProgress*1.0f/progress)*10;
            for (int i = 0; i < 5; i++) {
                path.rQuadTo(10,-d,20,0);
                path.rQuadTo(10,d,20,0);
            }
        }else{
            float d = count*1.0f/50*10;
            if(count%2==0){
                for (int i = 0; i < 5; i++) {
                    path.rQuadTo(20,-d,40,0);
                    path.rQuadTo(20,d,40,0);
                }
            }else{
                for (int i = 0; i < 5; i++) {

                    path.rQuadTo(20,d,40,0);
                    path.rQuadTo(20,-d,40,0);
                }
            }
        }

        path.close();

        bitmapCanvas.drawPath(path,progressPaint);
        String text = currentProgress*100/maxProgress+"%";
        float textX = width / 2 - textPaint.measureText(text) / 2;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float dy = (fontMetrics.descent + fontMetrics.ascent) / 2;
        float textY = height / 2 - dy;
        bitmapCanvas.drawText(text,textX,textY,textPaint);

        canvas.drawBitmap(bitmap,0,0,null);


    }
}
