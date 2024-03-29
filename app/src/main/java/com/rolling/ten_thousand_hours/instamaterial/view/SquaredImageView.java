package com.rolling.ten_thousand_hours.instamaterial.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 定义的圆形用户头像控件
 * Created by 10000_hours on 2015/9/26.
 */
public class SquaredImageView extends ImageView {
    public SquaredImageView (Context context) {
        super(context);
    }

    public SquaredImageView (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SquaredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // TODO: 2015/10/3 去百度学习一下 TargetApi 
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquaredImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
