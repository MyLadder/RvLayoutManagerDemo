package com.example.sun.rvlayoutmanagerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyBean> list;
    private List<Bitmap> list_imgs;
    private Context context;

    public MyAdapter(List<MyBean> list, List<Bitmap> list_imgs, Context context) {
        this.list = list;
        this.list_imgs = list_imgs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyBean myBean = list.get(position);
        holder.tv_name.setText(myBean.getName());
        holder.tv_page.setText(myBean.getPosition() + "/" + getItemCount());
        holder.iv_content.setImageBitmap(list_imgs.get(myBean.getPosition() - 1));
        holder.iv_content.setOnClickListener(holder);
        holder.iv_content.setTag(myBean.getPosition());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final Context context;
        private TextView tv_name;
        private TextView tv_page;
        private ImageView iv_content;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_page = (TextView) itemView.findViewById(R.id.tv_page);
            iv_content = (ImageView) itemView.findViewById(R.id.iv_content);
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PictureActivity.class);
            intent.putExtra("pic_position", ((int) v.getTag()));
            //AppCompat兼容Activity过场动画
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    ((MainActivity) context), v, "pics");
            try {
                ActivityCompat.startActivity((Activity) context, intent, optionsCompat.toBundle());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }
}
