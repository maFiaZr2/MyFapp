package com.gree.myfapp.Utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.gree.myfapp.MyApplication;

/**
 * Created by asus on 2016/10/23.
 */

public class PhoneDisplay {
    private int width;
    private int height;

    public PhoneDisplay(Display display) {
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    public  int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
