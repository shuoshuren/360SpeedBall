package com.example.xiao.a360speedball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.xiao.a360speedball.R;


/**
 * 浮窗圆形小球
 * Created by xiao on 2017/3/6.
 */

public class FloatCircleView extends View {

    private int mWidth = 150;
    private int mHeight = 150;

    private Paint mCirclePaint;//画圆的画笔
    private Paint mTextPaint;//画文字的画笔

    private String mText = "50%";//绘制的文本信息

    private boolean isDraging = false;

    private Context mContext;

    private Bitmap mBitmap;

    public FloatCircleView(Context context) {
        this(context, null);
    }

    public FloatCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        Bitmap src = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_round);
        mBitmap = Bitmap.createScaledBitmap(src,mWidth,mHeight,true);
        initView();
    }


    private void initView() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.GRAY);
        mCirclePaint.setAntiAlias(true);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setFakeBoldText(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isDraging) {
            canvas.drawBitmap(mBitmap,0,0,null);
        }else{
            canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mCirclePaint);

            float x = mWidth / 2 - mTextPaint.measureText(mText) / 2;
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float dy = (fontMetrics.descent + fontMetrics.ascent) / 2;
            float y = mHeight / 2 - dy;

            canvas.drawText(mText, x, y, mTextPaint);
        }

    }


    /**
     * 设置拖拽时的状态
     *
     * @param state
     */
    public void setDragState(boolean state) {
        isDraging = state;
        invalidate();

    }
}
