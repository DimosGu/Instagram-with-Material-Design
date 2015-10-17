package com.rolling.ten_thousand_hours.instamaterial.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.rolling.ten_thousand_hours.instamaterial.R;
import com.rolling.ten_thousand_hours.instamaterial.Utils;
import com.rolling.ten_thousand_hours.instamaterial.view.SquaredImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by 10000_hours on 2015/9/13.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context context;
    private  int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private  boolean animateItems = false;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private OnFeedItemClickListener onFeedItemClickListener;

    public FeedAdapter (Context context) {
        this.context = context;
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        /**
         * ButterKnife 7.0.0 : '@Bind' 代替了 ‘@InjectView’ 和 '@Ingectviews'
         * 'ButterKnife.bind' 和 'ButterKnife.unbind' 分别替换了 'ButterKnife.inject' 和 `ButterKnife.reset`
         */
        @Bind(R.id.ivFeedCenter)
        SquaredImageView ivFeedCenter;
        @Bind(R.id.ivFeedBottom)
        ImageView ivFeedBottom;
        @Bind(R.id.btnMore)
        ImageButton btnMore;
        @Bind(R.id.btnComments)
        ImageButton btnComments;
        @Bind(R.id.btnLike)
        ImageButton btnLike;
        @Bind(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;

        public CellFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from( context ).inflate(R.layout.item_feed, parent, false);
        return new CellFeedViewHolder(view);
    }

    private void runEnterAnimation ( View view, int position ) {
//        if ( position >= ANIMATED_ITEMS_COUNT - 1 ) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if ( position > lastAnimatedPosition ) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        if (position % 2 == 0) {
            holder.ivFeedCenter.setImageResource(R.mipmap.img_feed_center_1);
            holder.ivFeedBottom.setImageResource(R.mipmap.img_feed_bottom_1);
        } else {
            holder.ivFeedCenter.setImageResource(R.mipmap.img_feed_center_2);
            holder.ivFeedBottom.setImageResource(R.mipmap.img_feed_bottom_2);
        }
        updateLikesCounter(holder, false);

        //设置监听和Tag
        holder.ivFeedBottom.setOnClickListener(this);
        holder.ivFeedBottom.setTag(position);
        holder.btnComments.setOnClickListener(this);
        holder.btnComments.setTag(position);
        holder.btnMore.setOnClickListener(this);
        holder.btnMore.setTag(position);
    }

    private void updateLikesCounter(CellFeedViewHolder holder, boolean animated) {
        int currentLikesCount = likesCount.get(holder.getAdapterPosition()) + 1;
        // TODO: 2015/10/18 google 一些这个R.plurals是个什么情况
        // 可参考：http://developer.android.com/intl/zh-cn/guide/topics/resources/string-resource.html#Plurals
        String likesCountText = context.getResources().getQuantityString(
                R.plurals.likes_count, currentLikesCount, currentLikesCount
        );

        if (animated) {
            //点击like按钮的时候动态更新（TextSwitcher.setText()切换并设置下一个TextView的值）
            holder.tsLikesCounter.setText(likesCountText);
        } else {
            //onBindViewHolder()中被调用，无需动画（设置正在显示的TextView）
            holder.tsLikesCounter.setCurrentText(likesCountText);
        }

        likesCount.put(holder.getAdapterPosition(), currentLikesCount);
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    @Override
    public void onClick(View view) {
        final  int viewId = view.getId();
        if (viewId == R.id.btnComments) {
            if (onFeedItemClickListener != null) {
                onFeedItemClickListener.onCommentsClick(view, (Integer) view.getTag());
            }
        } else if (viewId == R.id.btnMore) {
            if (onFeedItemClickListener != null) {
                onFeedItemClickListener.onMoreClick(view, (Integer)view.getTag());
            }
        }
    }

    public void setOnFeedItemClickListener (OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public interface OnFeedItemClickListener {
        public void onCommentsClick (View view, int position);
        public void onMoreClick (View view, int position);
    }

    // 为recycleView适配数据
    public void updateItems ( boolean animated) {
        itemsCount = 10;
        animateItems = animated;
        fillLikesWithRandomValues();
        notifyDataSetChanged();
    }

    private void fillLikesWithRandomValues() {
        for (int i = 0; i < getItemCount(); i++) {
            likesCount.put(i, new Random().nextInt(100));
        }
    }

}
