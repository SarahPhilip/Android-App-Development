    package com.sdsu.cs646.shameetha.draganddraw;

    import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

    /**
     * Created by Shameetha on 4/7/15.
     */
    public class BoxDrawingView extends View   {
        public static final String TAG = "BoxDrawingView";
        private ArrayList<Box> mBoxes = new ArrayList<Box>();
        private Box mCurrentBox;
        private Paint mBoxPaint;
        private Paint mBackgroundPaint;
        private int circleCounter = 0;
        private static boolean downFlag = false;
        private static PointF curr;
        private static float radius;
        public static int LONG_PRESS_TIME = 200; // Time in miliseconds
        private Handler mHandler = new Handler();

        // Used when creating the view in code
        public BoxDrawingView(Context context) {
            this(context, null);
        }

        // Used when inflating the view from XML
        public BoxDrawingView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // Paint the boxes a nice semitransparent red (ARGB)
            mBoxPaint = new Paint();
            mBoxPaint.setColor(0x22ff0000);

            // Paint the background off-white
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(0xfff8efe0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // Fill the background
            canvas.drawPaint(mBackgroundPaint);

            for (Box box : mBoxes) {

                    canvas.drawCircle(box.getOrigin().x, box.getOrigin().y, box.getRadius(), mBoxPaint);

            }
            if(mCurrentBox!=null) {
                canvas.drawCircle(mCurrentBox.getOrigin().x, mCurrentBox.getOrigin().y, radius, mBoxPaint);
            }
        }

        Runnable longPressed = new Runnable() {
            public void run() {
                Log.i("info","LongPress");
                mHandler.postDelayed(this, LONG_PRESS_TIME);
//                clickListener.onClick(downView);
                if (mCurrentBox != null) {
                    radius ++;
                    Log.i("info","Value: " + Float.toString(radius));
                    mCurrentBox.setRadius(radius);
                    invalidate();
                }
            }
        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            curr = new PointF(event.getX(), event.getY());
            switch(event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    downFlag = true;
                    // Reset drawing state
                    mCurrentBox = new Box(curr);
                    mCurrentBox.setOrigin(curr);
                    mHandler.removeCallbacks(longPressed);
                    mHandler.postDelayed(longPressed, LONG_PRESS_TIME);
                    break;

                case MotionEvent.ACTION_MOVE:
                    mHandler.removeCallbacks(longPressed);
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mCurrentBox != null) {
                        mCurrentBox.setRadius(radius);
                        mBoxes.add(mCurrentBox);
                        invalidate();
                    }
                    circleCounter++;
                    mCurrentBox = null;
                    mHandler.removeCallbacks(longPressed);
                    downFlag = false;
                    radius = 10;
                    break;
            }
                        return true;
        }

        
    }