package com.sdsu.cs646.shameetha.draganddraw;

import android.graphics.PointF;

/**
 * Created by Shameetha on 4/7/15.
 */
public class Box {
    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    private PointF mOrigin;
    private float mRadius;

    public Box(PointF origin, float radius) {
        mOrigin = origin;
        mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }



    public Box(PointF origin) {
        mOrigin = origin;
    }



    public PointF getOrigin() {
        return mOrigin;
    }
}