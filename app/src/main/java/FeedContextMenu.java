import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rolling.ten_thousand_hours.instamaterial.R;
import com.rolling.ten_thousand_hours.instamaterial.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 10000_hours on 2015/10/16.
 */
public class FeedContextMenu extends LinearLayout {
    private static final int CONTEXT_MENU_WIDTH = Utils.dpTopx(240);

    private int feedItem = -1;

    private OnFeedContextMenuItemClickListener onItemClickListener;

    public FeedContextMenu(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.mipmap.bg_container_shadow_9);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void bindToItem (int feedItem) {
        this.feedItem = feedItem;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
    }

    public void dismiss () {
        ((ViewGroup) getParent()).removeView(FeedContextMenu.this);
    }

    @OnClick(R.id.btnReport)
    public void onReportClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onReportClick(feedItem);
        }
    }

    @OnClick(R.id.btnReport)
    public void onSharePhotoClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onSharePhotoClick(feedItem);
        }
    }

    @OnClick(R.id.btnReport)
    public void onCopyShareUrlClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCopyShareUrlClick(feedItem);
        }
    }

    @OnClick(R.id.btnReport)
    public void onCancelClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCancelClick(feedItem);
        }
    }

    // 这里采用的是观察者模式  // TODO: 2015/10/16 google学习观察者模式
    /*
        将内部的各种点击事件暴露给外部调用者
     */
    public void setOnFeedMenuItemClickListener (OnFeedContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnFeedContextMenuItemClickListener {
        public void onReportClick(int feedItem);

        public void onSharePhotoClick(int feedItem);

        public void onCopyShareUrlClick(int feedItem);

        public void onCancelClick(int feedItem);
    }

}
