package com.rolling.ten_thousand_hours.instamaterial.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewAnimator;

import com.rolling.ten_thousand_hours.instamaterial.R;

/**
 * Created by 10000_hours on 2015/10/8.
 */
public class SendCommentButton extends ViewAnimator implements View.OnClickListener{
    public static final int STATE_SEND = 0;
    public static final int STATE_DONE = 1;
    
    private  static final long RESET_STATE_DELAY_MILLIS = 2000; //切换状态
    
    private int currentState;

    private OnSendClickLister onSendClickLister;

    private Runnable revertStateRunnable = new Runnable() {
        @Override
        public void run() {
            setCurrentState(STATE_SEND);      
        }
    };

    public void setCurrentState(int stateSend) {
        if (stateSend == STATE_SEND) {
            return;
        }

        currentState = stateSend;
        if (stateSend == STATE_DONE) {
            setEnabled(false);
            postDelayed(revertStateRunnable, RESET_STATE_DELAY_MILLIS);
            setInAnimation(getContext(), R.anim.slide_in_done);
            setOutAnimation(getContext(), R.anim.slide_in_send);
        } else if (stateSend == STATE_SEND) {
            setEnabled(true);
            setInAnimation(getContext(), R.anim.slide_in_send);
            setOutAnimation(getContext(), R.anim.slide_out_done);
        }
        showNext();
    }

    public SendCommentButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_comment_button, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        currentState = STATE_SEND;
        super.setOnClickListener(this);
    }

    /**
     * 防止activity结束的时候按钮的状态还没有切换回来
     * 用此方法去掉回调方法
     */
    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(revertStateRunnable);
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        if (onSendClickLister != null) {
            onSendClickLister.onSendClickListener(this);
        }
    }

    public void setOnSendClickLister (OnSendClickLister onSendClickLister) {
        this.onSendClickLister = onSendClickLister;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        //Do nothing, you have you own onClickListener implementation (OnSendClickListener)
    }

    public interface  OnSendClickLister {
        public void onSendClickListener(View v);
    }
}
