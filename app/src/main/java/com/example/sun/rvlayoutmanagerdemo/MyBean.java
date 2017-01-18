package com.example.sun.rvlayoutmanagerdemo;

/**
 * Created by Administrator on 2017/1/16.
 */

public class MyBean {
    String name;
    int position;

    public MyBean(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
