package com.sdsu.cs646.shameetha.draganddraw;

import android.graphics.PointF;

/**
 * Created by Shameetha on 4/7/15.
 */
public class Circle {
    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    private PointF mOrigin;
    private float mRadius;
    private float xVelocity;
    private float yVelocity;
    private int circleColor;



    public Circle(PointF origin, float radius, int randomAndroidColor) {
        mOrigin = origin;
        mRadius = radius;
        xVelocity = 0;
        yVelocity = 0;
        circleColor = randomAndroidColor;
    }

    public Circle(PointF origin) {
        mOrigin = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    @Override
    public String toString() {
        return "Circle[" + mOrigin.x + ", " + mOrigin.y + ", " + mRadius + "]";
    }
}