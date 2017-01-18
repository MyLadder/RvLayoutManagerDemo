package com.example.sun.rvlayoutmanagerdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.sun.rvlayoutmanagerdemo.CardConfig.MAX_SHOW_COUNT;
import static com.example.sun.rvlayoutmanagerdemo.CardConfig.SCALE_GAP;
import static com.example.sun.rvlayoutmanagerdemo.CardConfig.TRANS_Y_GAP;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<MyBean> list;
    private List<Bitmap> list_imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        list_imgs = new ArrayList<>();
        try {
            String[] list = getResources().getAssets().list("imgs");
            for (String s : list) {
                InputStream open = getAssets().open(s);
                Bitmap bitmap = BitmapFactory.decodeStream(open);
                list_imgs.add(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerView = ((RecyclerView) findViewById(R.id.recyclerView));
        recyclerView.setLayoutManager(new MyLayoutManager());

        this.list = new ArrayList<>();
        this.list.add(new MyBean("SABER" , 1));
        this.list.add(new MyBean("SABEER" , 2));
        this.list.add(new MyBean("SABEEER" , 3));
        this.list.add(new MyBean("SABEEEER" , 4));
        this.list.add(new MyBean("SABEEEEER" , 5));
        myAdapter = new MyAdapter(list, list_imgs, this);
        recyclerView.setAdapter(myAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //实现循环的要点
                MyBean remove = MainActivity.this.list.remove(viewHolder.getLayoutPosition());
                MainActivity.this.list.add(0,remove);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                //先根据滑动的dxdy 算出现在动画的比例系数fraction
                double swipValue = Math.sqrt(dX * dX + dY * dY);
                double fraction = swipValue / getThreshold(viewHolder);
                //边界修正 最大为1
                if (fraction > 1) {
                    fraction = 1;
                }
                //对每个ChildView进行缩放 位移
                int childCount = recyclerView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = recyclerView.getChildAt(i);
                    //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
                    int level = childCount - i - 1;
                    if (level > 0) {
                        child.setScaleX((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));

                        if (level < MAX_SHOW_COUNT - 1) {
                            child.setScaleY((float) (1 - SCALE_GAP * level + fraction * SCALE_GAP));
                            child.setTranslationY((float) (TRANS_Y_GAP * level - fraction * TRANS_Y_GAP));
                        }
                    }
                }
            }

            //水平方向是否可以被回收掉的阈值
            public float getThreshold(RecyclerView.ViewHolder viewHolder) {
                return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
