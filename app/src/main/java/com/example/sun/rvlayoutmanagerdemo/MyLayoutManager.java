package com.example.sun.rvlayoutmanagerdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import static com.example.sun.rvlayoutmanagerdemo.CardConfig.MAX_SHOW_COUNT;
import static com.example.sun.rvlayoutmanagerdemo.CardConfig.SCALE_GAP;
import static com.example.sun.rvlayoutmanagerdemo.CardConfig.TRANS_Y_GAP;

/**
 * Created by Administrator on 2017/1/16.
 */

public class MyLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount > MAX_SHOW_COUNT) {
            //从可见的最底层View开始layout，依次层叠上去
            for (int position = itemCount - MAX_SHOW_COUNT; position < itemCount; position++) {
                View viewForPosition = recycler.getViewForPosition(position);
                addView(viewForPosition);
                measureChildWithMargins(viewForPosition, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(viewForPosition);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(viewForPosition);
                //我们在布局时，将childView居中处理，这里也可以改为只水平居中
                layoutDecoratedWithMargins(viewForPosition, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(viewForPosition),
                        heightSpace / 2 + getDecoratedMeasuredHeight(viewForPosition));
                /**
                 * TopView的Scale 为1，translationY 0
                 * 每一级Scale相差0.05f，translationY相差7dp左右
                 *
                 * 观察人人影视的UI，拖动时，topView被拖动，Scale不变，一直为1.
                 * top-1View 的Scale慢慢变化至1，translation也慢慢恢复0
                 * top-2View的Scale慢慢变化至 top-1View的Scale，translation 也慢慢变化只top-1View的translation
                 * top-3View的Scale要变化，translation岿然不动
                 */

                //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
                int level = itemCount - position - 1;
                //除了顶层不需要缩小和位移
                if (level > 0 /*&& level < mShowCount - 1*/) {
                    //每一层都需要X方向的缩小
                    viewForPosition.setScaleX(1 - SCALE_GAP * level);
                    //前N层，依次向下位移和Y方向的缩小
                    if (level < MAX_SHOW_COUNT - 1) {
                        viewForPosition.setTranslationY(TRANS_Y_GAP * level);
                        viewForPosition.setScaleY(1 - SCALE_GAP * level);
                    } else {//第N层在 向下位移和Y方向的缩小的成都与 N-1层保持一致
                        viewForPosition.setTranslationY(TRANS_Y_GAP * (level - 1));
                        viewForPosition.setScaleY(1 - SCALE_GAP * (level - 1));
                    }
                }
            }
        }
    }
}
