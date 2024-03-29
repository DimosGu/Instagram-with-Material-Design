package com.rolling.ten_thousand_hours.instamaterial.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.rolling.ten_thousand_hours.instamaterial.R;
import com.rolling.ten_thousand_hours.instamaterial.Utils;
import com.rolling.ten_thousand_hours.instamaterial.adapter.FeedAdapter;
import com.rolling.ten_thousand_hours.instamaterial.view.FeedContextMenu;
import com.rolling.ten_thousand_hours.instamaterial.view.FeedContextMenuManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        FeedAdapter.OnFeedItemClickListener, FeedContextMenu.OnFeedContextMenuItemClickListener{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rvFeed)
    RecyclerView rvFeed;
    @Bind(R.id.btnCreate)
    ImageButton btnCreate;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;

    private MenuItem inboxMenuItem;
    private FeedAdapter feedAdapter;

    protected boolean pendingIntroAnimation = false;
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupFeed ();
        setupToolbar();
        /*
         *  设定在activity启动的时候才播放动画（旋转屏幕的时候不播放）
         *  menu 的动画过程是不能在onCreate() 中实现的，应该在 onCreateMenu()中实现
         */
        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        } else {
            feedAdapter.updateItems(false);
        }
    }

    /**
     * 设置Toolbar
     */
    private void setupToolbar() {
        //import android.support.v7.widget.Toolbar 包不能导错了
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
    }

    /**
     * 配置适配器
     */
    private void setupFeed() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this) {
                    @Override
                    protected int getExtraLayoutSpace(RecyclerView.State state) {
                        return 300;
                    }
                };
        feedAdapter = new FeedAdapter(this);
        feedAdapter.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setLayoutManager(linearLayoutManager);
        rvFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        inboxMenuItem = menu.findItem(R.id.action_inbox);
        inboxMenuItem.setActionView(R.layout.menu_item_view);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    /**
     * -- ToolBar 的动画
     * 首先将所有元素都通过移动屏幕之外隐藏起来（包括ActionFloatingButton）
     * 接着让Toolbar 元素一个接着一个的开始动画（规范）
     * 设置动画完成监听，完成时调用  content的动画
     */
    private void startIntroAnimation() {
        // 将ActionFloatingButton 藏在屏幕可视范围的下面
        btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        //将 Toolbar和 InboxMenuItem 藏在屏幕可见范围的上方
        int actionbarSize = Utils.dpTopx(56);
        toolbar.setTranslationY(-actionbarSize);
        inboxMenuItem.getActionView().setTranslationY(-actionbarSize);

        toolbar.animate()
                .translationY(0) //纵坐标移动到0的文字
                .setDuration(ANIM_DURATION_TOOLBAR) //动画的响应时间
                .setStartDelay(300)
                .start();

        ivLogo.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400)
                .start();

        inboxMenuItem.getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    /**
     * -- content的动画
     *
     */
    private void startContentAnimation() {
        btnCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        feedAdapter.updateItems(false);
    }

    /**
     * 点击跳转到评论界面，但是打开activity的时候不能有过度和切换的效果
     * （让用户有在同一个界面的错觉）
     * @param view 被点击的评论
     * @param position 被点击的位置
     */
    @Override
    public void onCommentsClick(View view, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);
        /*
         * Get location on screen for tapped view
         * 获取点击的位置，实现从点击的位置展开评论页面
         */
        int[] startingLocation = new int[2];
        view.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        startActivity(intent);

        //Disable enter transition for new Activity 屏蔽切换动画
        overridePendingTransition(0, 0);

    }

    @Override
    public void onMoreClick(View view, int position) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(view, position, this);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }
}
