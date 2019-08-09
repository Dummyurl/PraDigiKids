package com.pratham.pradigikids.custom.spotlight;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

public interface Target {

    Point getPoint();

    Rect getRect();

    View getView();

    int getViewLeft();

    int getViewRight();

    int getViewTop();

    int getViewBottom();

    int getViewWidth();

    int getViewHeight();
}