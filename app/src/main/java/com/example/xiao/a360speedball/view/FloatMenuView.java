package com.example.xiao.a360speedball.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.xiao.a360speedball.R;
import com.example.xiao.a360speedball.engine.FloatViewManager;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by xiao on 2017/3/7.
 */

public class FloatMenuView extends LinearLayout {

    private LinearLayout menuLL;

    private TranslateAnimation anim;

    public FloatMenuView(Context context) {
        super(context);
        initView();
    }

    public FloatMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FloatMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View root = inflater.inflate(R.layout.float_menu_view, null,false);
        menuLL = (LinearLayout) root.findViewById(R.id.menu_ll);
        anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0
                , Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(500);
        anim.setFillAfter(true);
        menuLL.setAnimation(anim);
        menuLL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                FloatViewManager manager = FloatViewManager.getInstance(getContext());
                manager.hideFloatMenuView();
                manager.showFloatCircleView();

            }
        });
        addView(root);
    }

    public void startAnni() {

        anim.start();

    }
}
