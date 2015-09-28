package com.rolling.ten_thousand_hours.instamataterial.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.rolling.ten_thousand_hours.instamataterial.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 10000_hours on 2015/9/27.
 */
public class CommentsActivity extends AppCompatActivity {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.contentRoot) 
    LinearLayout contentRoot;
    @Bind(R.id.rvComments)
    RecyclerView rvComments;
    @Bind(R.id.llAddComment)
    LinearLayout llAddComment;

    private int drawingStartLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        setupToolbar();
        setupComments();

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        // TODO: 2015/9/28  明天再做吧  :) 
//        if ( savedInstanceState == null ) {
//            contentRoot.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnPreDrawListener(
//
//            ));

        }

    private void setupComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        // TODO: 2015/9/28 设置适配器
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white);
    }

    // TODO: 2015/9/28 设置打开动画，content 动画， 

}

