package com.example.sun.rvlayoutmanagerdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/1/17.
 */

public class PictureActivity extends AppCompatActivity {

    private ImageView iv;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        iv = (ImageView) findViewById(R.id.iv_pic);
        ViewCompat.setTransitionName(iv, "pics");

        int pic_position = getIntent().getIntExtra("pic_position", 0);
        Bitmap bitmap = null;
        InputStream open = null;
        try {
            String[] imgses = getResources().getAssets().list("imgs");
            open = getResources().getAssets().open(imgses[pic_position - 1]);
            bitmap = BitmapFactory.decodeStream(open);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        iv.setImageBitmap(bitmap);
        photoViewAttacher = new PhotoViewAttacher(iv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (photoViewAttacher != null) {
            photoViewAttacher.cleanup();
        }
    }
}
