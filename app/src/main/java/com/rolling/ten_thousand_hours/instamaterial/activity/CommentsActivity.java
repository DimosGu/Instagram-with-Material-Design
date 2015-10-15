package com.rolling.ten_thousand_hours.instamaterial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.rolling.ten_thousand_hours.instamaterial.R;
import com.rolling.ten_thousand_hours.instamaterial.Utils;
import com.rolling.ten_thousand_hours.instamaterial.adapter.CommentsAdapter;
import com.rolling.ten_thousand_hours.instamaterial.view.SendCommentButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论界面
 * Created by 10000_hours on 2015/9/27.
 */
public class CommentsActivity extends AppCompatActivity implements SendCommentButton.OnSendClickLister{
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    private int drawingStartLocation;
    private CommentsAdapter commentsAdapter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.contentRoot)
    LinearLayout contentRoot;
    @Bind(R.id.rvComments)
    RecyclerView rvComments;
    @Bind(R.id.llAddComment)
    LinearLayout llAddComment;

    //绑定评论布局和按钮
    @Bind(R.id.etComment)
    EditText etComment;
    @Bind(R.id.btnSendComment)
    SendCommentButton btnSendComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        setupToolbar();
        setupComments();
        setupSendCommentButton();

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if ( savedInstanceState == null ) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickLister(this);
    }

    /**
     * 开始动画
     */
    private void startIntroAnimation() {
        ViewCompat.setElevation(toolbar, 0);
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
//        llAddComment.setTranslationY(100); 改为200毫秒
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(toolbar, Utils.dpTopx(8));
                        animationContent();
                    }
                }).start();
    }

    private void animationContent() {
        commentsAdapter.updateItems();
        llAddComment.animate()
                .translationY(0)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);

        rvComments.setHasFixedSize(true); // TODO: 2015/10/3 查一下这个什么意思

        commentsAdapter = new CommentsAdapter(this);
        rvComments.setAdapter(commentsAdapter);

        // disable overscroll effect in comments list
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);

        //这里和博客写的会有些不一样 addOnScrollListener
        rvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    /*
     * 点击发送按钮
     */
//    废弃这个方法了，用下面那个
//    @OnClick(R.id.btnSendComment)
//    public void onSendCommentClick () {
//        commentsAdapter.addItem();
//        commentsAdapter.setAnimationsLocked(false);
//        commentsAdapter.setDelayEnterAnimation(false);
//        rvComments.smoothScrollBy(0,
//                rvComments.getChildAt(0).getHeight()
//                        * commentsAdapter.getItemCount());
//    }
    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            commentsAdapter.addItem();
            commentsAdapter.setAnimationsLocked(false);
            commentsAdapter.setDelayEnterAnimation(false);
            rvComments.smoothScrollBy(0,
                    rvComments.getChildAt(0).getHeight()
                    * commentsAdapter.getItemCount());

            etComment.setText(null);
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
        }
    }

    // 检查文本框是否有内容
    private boolean validateComment () {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(
                    AnimationUtils.loadAnimation( this, R.anim.shake_error));
            return false;
        }
        return true;
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
    }

    /**
     * 评论界面的推出动画处理：
     * 1，播放内容部分的滑出动画 2，屏蔽activity退出动画
     */
    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(toolbar, 0);
        contentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0,0);
                    }
                }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        return true;
    }
}

