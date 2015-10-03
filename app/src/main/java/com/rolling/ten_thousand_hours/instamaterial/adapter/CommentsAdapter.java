package com.rolling.ten_thousand_hours.instamaterial.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rolling.ten_thousand_hours.instamaterial.R;
import com.rolling.ten_thousand_hours.instamaterial.view.RoundedTransformation;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 10000_hours on 2015/9/28.
 */
public class CommentsAdapter extends RecyclerView.Adapter {
    private Context context;
    private int itemsCount = 0;
    private  int lastAnimatedPosition = -1;
    private int avatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    public CommentsAdapter (Context context) {
        this.context = context;
        avatarSize = context.getResources()
                .getDimensionPixelSize(R.dimen.btn_fab_size);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @Bind(R.id.tvComment)
        TextView tvComment;

        public CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation (viewHolder.itemView, position);
        CommentViewHolder commentsViewHolder = (CommentViewHolder) viewHolder;
        switch (position % 3) {
            case 0:
                commentsViewHolder.tvComment.setText("Lorem存有悲坐阿梅德，consectetur回扣.");
                break;
            case 1:
                commentsViewHolder.tvComment.setText("蛋糕存有悲坐阿梅德熊爪.");
                break;
            case 2:
                commentsViewHolder.tvComment.setText("蛋糕存有悲坐.阿梅德姜饼蛋糕." +
                        "软糖冰淇淋甜点冰杏仁苹果派甜点糖梅子.");
                break;
        }

        Picasso.with(context)
                .load(R.mipmap.ic_launcher)
                .centerCrop()
                .resize(avatarSize, avatarSize)
                .transform(new RoundedTransformation())
                .into(commentsViewHolder.ivUserAvatar);

    }

    private void runEnterAnimation(View itemView, int position) {
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            itemView.setTranslationY(100);
            itemView.setAlpha(0.f);
            itemView.animate()
                    .translationY(0)
                    .alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * position : 0)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            animationsLocked = true;
                        }
                    }).start();
        }
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public void updateItems() {
        itemsCount = 10;
        notifyDataSetChanged();
    }

    public void addItem () {
        itemsCount++ ;
        notifyItemInserted(itemsCount - 1);
    }

    public void setAnimationsLocked (boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation (boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

}
