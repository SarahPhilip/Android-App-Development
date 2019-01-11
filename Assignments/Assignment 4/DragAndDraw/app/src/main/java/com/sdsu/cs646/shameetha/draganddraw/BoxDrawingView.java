    package com.sdsu.cs646.shameetha.draganddraw;

    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Paint;
    import android.graphics.PointF;
    import android.os.Handler;
    import android.support.v4.view.GestureDetectorCompat;
    import android.util.AttributeSet;
    import android.util.Log;
    import android.util.SparseArray;
    import android.view.GestureDetector;
    import android.view.MotionEvent;
    import android.view.View;

    import java.util.ArrayList;

    /**
     * Created by Shameetha on 4/7/15.
     */
    public class BoxDrawingView extends View  {
        public static final String TAG = "BoxDrawingView";
        private ArrayList<Box> mBoxes = new ArrayList<Box>();
        private SparseArray<Box> mCirclePointer = new SparseArray<Box>(CIRCLES_LIMIT);
        private Box mCurrentBox;
        private Paint mBoxPaint;
        private Paint mBackgroundPaint;
        private int circleCounter = 0;
        private static final int CIRCLES_LIMIT = 15;
        private static boolean downFlag = false;
        private static PointF curr;
        private static float radius;
        public static int LONG_PRESS_TIME = 200; // Time in miliseconds
        private Handler mHandler = new Handler();
        //gesture detector
        private GestureDetectorCompat gDetect;
        private static final String DEBUG_TAG = "Gestures";

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

            gDetect = new GestureDetectorCompat(getContext(), new GestureListener());
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
                if (downFlag) {
                    radius ++;
                    Log.i("info","Value: " + Float.toString(radius));
                    mCurrentBox.setRadius(radius);
                    mCurrentBox.setOrigin(curr);
                    invalidate();
                    Box checkOverlap = getOverlapCircle(curr);
                    Log.w("overlap", "overlap" + checkOverlap);
                    if(checkOverlap != null)
                        downFlag = false;
                    mHandler.postDelayed(this, LONG_PRESS_TIME);
                }
            }
        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.w("PPP", "Size is " + mBoxes.size());
            this.gDetect.onTouchEvent(event);
            for (Box circle : mBoxes) {
                Log.w("PPP", "Circle is " + circle);
            }
            int pointerId;
            int actionIndex = event.getActionIndex();
            if (circleCounter < CIRCLES_LIMIT) {
                Log.i("COUNTER", String.valueOf(circleCounter));
                curr = new PointF(event.getX(), event.getY());
                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        downFlag = true;
                        // it's the first pointer, so clear all existing pointers data
                        clearCirclePointer();
                        // check if we've touched inside some circle
                        mCurrentBox = obtainTouchedCircle(curr);
                        mCurrentBox.setOrigin(curr);
                        mCirclePointer.put(event.getPointerId(0), mCurrentBox);
                        mHandler.removeCallbacks(longPressed);
                        mHandler.postDelayed(longPressed, LONG_PRESS_TIME);
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.w(TAG, "Pointer down");
                        // It secondary pointers, so obtain their ids and check circles
                        pointerId = event.getPointerId(actionIndex);
                        // check if we've touched inside some circle
                        mCurrentBox = obtainTouchedCircle(curr);

                        mCirclePointer.put(pointerId, mCurrentBox);
                        mCurrentBox.setOrigin(curr);
                        invalidate();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        final int pointerCount = event.getPointerCount();

                        Log.w(TAG, "Move");

                        for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                            // Some pointer has moved, search it by pointer id
                            pointerId = event.getPointerId(actionIndex);

                            curr = new PointF(event.getX(), event.getY());

                            mCurrentBox = mCirclePointer.get(pointerId);

                            if (null != mCurrentBox) {
                                mCurrentBox.setOrigin(curr);
                            }
                        }
                        invalidate();
                        mHandler.removeCallbacks(longPressed);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        clearCirclePointer();
                        invalidate();
                        if (mCurrentBox != null) {
                            mCurrentBox.setRadius(radius);
//                            mBoxes.add(mCurrentBox);
                            invalidate();
                        }
                        mCurrentBox = null;
                        mHandler.removeCallbacks(longPressed);
                        downFlag = false;
//                        radius = 10;
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        // not general pointer was up
                        pointerId = event.getPointerId(actionIndex);

                        mCirclePointer.remove(pointerId);
                        invalidate();

                        break;
                }
            }
            return true;
        }

        /**
         * Clears all CircleArea - pointer id relations
         */
        private void clearCirclePointer() {
            Log.w(TAG, "clearCirclePointer");

            mCirclePointer.clear();
        }

        /**
         * Search and creates new (if needed) circle based on touch area
         *
         * @param touch PointF of touch
         *
         * @return obtained {@link Box}
         */
        private Box obtainTouchedCircle(final PointF touch) {
            Box touchedCircle = getTouchedCircle(touch);
            if (null == touchedCircle) {
                touchedCircle = new Box(touch);
                radius = 10;
                if (mBoxes.size() == CIRCLES_LIMIT) {
                    Log.w(TAG, "Clear all circles, size is " + mBoxes.size());
                    // remove first circle
                    mBoxes.clear();
                }

                Log.w(TAG, "Added circle " + touchedCircle);
                mBoxes.add(touchedCircle);
                circleCounter++;
            }

            return touchedCircle;
        }

        /**
         * Determines touched circle
         *
         * @param touch PointF touch coordinate
         *
         * @return {@link Box} touched circle or null if no circle has been touched
         */
        private Box getTouchedCircle(final PointF touch) {
            Box touched = null;

            for (Box circle : mBoxes) {
                if ((touch.x - (circle.getOrigin().x )) * (touch.x - (circle.getOrigin().x )) + (touch.y - (circle.getOrigin().y )) * (touch.y - (circle.getOrigin().y )) <= circle.getRadius() * circle.getRadius()) {
                    touched = circle;
                    break;
                }
            }
            return touched;
        }

        private Box getOverlapCircle(final PointF touch) {
            Box touched = null;

            for (Box circle : mBoxes) {
                Log.w("PPP", "PPP" + circle);
                if (circle.getOrigin().x + circle.getRadius() + radius > touch.x
                        && circle.getOrigin().x < touch.x + circle.getRadius() + radius
                        && circle.getOrigin().y + circle.getRadius() + radius > touch.y
                        && circle.getOrigin().y < touch.y + circle.getRadius() + radius)
                {
                    //AABBs are overlapping
                    touched = circle;
                    break;
                }
//                if ((touch.x - (circle.getOrigin().x + circle.getRadius())) * (touch.x - (circle.getOrigin().x + circle.getRadius())) + (touch.y - (circle.getOrigin().y + circle.getRadius())) * (touch.y - (circle.getOrigin().y + circle.getRadius())) < circle.getRadius() * circle.getRadius()) {
//                    touched = circle;
//                    break;
//                }

            }

            return touched;
        }

        public class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private float flingMin = 100;
            private float velocityMin = 100;
            //user will move forward through messages on fling up or left
            boolean forward = false;
            //user will move backward through messages on fling down or right
            boolean backward = false;


            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2,
                                   float velocityX, float velocityY) {
                Log.i("AAA","BBB");

                return true;
            }

            @Override
            public void onLongPress(MotionEvent event) {
                Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
            }

        }

    }
