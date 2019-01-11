    package com.sdsu.cs646.shameetha.draganddraw;

    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Paint;
    import android.graphics.PointF;
    import android.os.Handler;
    import android.support.v4.view.GestureDetectorCompat;
    import android.util.AttributeSet;
    import android.util.SparseArray;
    import android.view.GestureDetector;
    import android.view.MotionEvent;
    import android.view.View;

    import java.util.ArrayList;
    import java.util.Random;

    /**
     * Created by Shameetha on 4/7/15.
     */
    public class CircleDrawingView extends View  {
        public static final String TAG = "CircleDrawingView";
        private ArrayList<Circle> mCircles = new ArrayList<Circle>();
        private ArrayList<Circle> flingCircleArray = new ArrayList<Circle>();
        private SparseArray<Circle> mCirclePointer = new SparseArray<Circle>(CIRCLES_LIMIT);
        private Circle mCurrentCircle, flingCircle;
        private Paint mCirclePaint;
        private Paint mBackgroundPaint;
        private int circleCounter = 0;
        private static final int CIRCLES_LIMIT = 15;
        private static boolean downFlag = false;
        private static PointF curr;
        private static float radius;
        public static int FRAME_RATE = 100; // Time in miliseconds
        private Handler mHandler = new Handler();
        private static final float restitution = 0.8f;
        private GestureDetectorCompat gDetect;
        private static final String DEBUG_TAG = "Gestures";
        private final double dt = 0.01;
        private static boolean newCircle = false;
        private int height;
        private int width;

        public CircleDrawingView(Context context) {
            this(context, null);
        }

        public CircleDrawingView(Context context, AttributeSet attrs) {
            super(context, attrs);
            int[] androidColors = getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//            mCirclePaint = new Paint();
//            mCirclePaint.setColor(randomAndroidColor);
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(0xfff8efe0);
            gDetect = new GestureDetectorCompat(getContext(), new GestureListener());
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // Fill the background
            height = this.getHeight();
            width = this.getWidth();
            canvas.drawPaint(mBackgroundPaint);
            for (Circle circle : mCircles) {
                mCirclePaint = new Paint();
                mCirclePaint.setColor(circle.getCircleColor());
                canvas.drawCircle(circle.getOrigin().x, circle.getOrigin().y, circle.getRadius(), mCirclePaint);
            }

            if(mCurrentCircle !=null) {
                mCirclePaint = new Paint();
                mCirclePaint.setColor(mCurrentCircle.getCircleColor());
                canvas.drawCircle(mCurrentCircle.getOrigin().x, mCurrentCircle.getOrigin().y, radius, mCirclePaint);
            }

            invalidate();
        }

        Runnable longPressed = new Runnable() {
            public void run() {
                if (downFlag) {
                    if(newCircle) {
                        radius++;
                        mCurrentCircle.setRadius(radius);
                        PointF point = new PointF(mCurrentCircle.getOrigin().x, mCurrentCircle.getOrigin().y);
                        if(mCurrentCircle != null) {
                            if ((point.x > getWidth() - (mCurrentCircle.getRadius())) || (point.x - mCurrentCircle.getRadius() < 0)) {
                                mCircles.remove(mCurrentCircle);
                                mCurrentCircle = null;
                                mHandler.removeCallbacks(this);
                                downFlag = false;
                            }
                        }
                        if(mCurrentCircle != null) {
                            if ((point.y > getHeight() - (mCurrentCircle.getRadius())) || (point.y - mCurrentCircle.getRadius() < 0)) {
                                mCircles.remove(mCurrentCircle);
                                mCurrentCircle = null;
                                mHandler.removeCallbacks(this);
                                downFlag = false;
                            }
                        }
                    }
                    if(mCurrentCircle != null) {
                        mCurrentCircle.setOrigin(curr);
                    }
                    invalidate();
                }
                invalidate();
                mHandler.postDelayed(this, 50);
            }
        };

        Runnable drawAgain = new Runnable() {
            public void run() {
                for(Circle circle : mCircles) {
                    PointF point = new PointF(circle.getOrigin().x, circle.getOrigin().y);

                    if(point.x < 0 && point.y < 0) {
                        point.x = getWidth()/2;
                        point.y = getHeight()/2;
                        circle.setOrigin(point);
                    }

                    else {
                        point.x += (dt * circle.getxVelocity());
                        point.y += (dt * circle.getyVelocity());
                        if ((point.x > width - (circle.getRadius())) || (point.x - circle.getRadius() < 0)) {
                            circle.setxVelocity(circle.getxVelocity()*(restitution * -1));
                        }
                        if ((point.y > height - (circle.getRadius())) || (point.y-circle.getRadius() < 0)) {
                            circle.setyVelocity(circle.getyVelocity()*(restitution * -1));
                        }
                        circle.setOrigin(point);
                    }
                }
                for (Circle circle : mCircles) {
                    detectCollisions(circle);
                }
                invalidate();
                mHandler.postDelayed(this, 50);
                }
        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            this.gDetect.onTouchEvent(event);
            int pointerId;
            int actionIndex = event.getActionIndex();
            if (circleCounter < CIRCLES_LIMIT) {
                curr = new PointF(event.getX(), event.getY());
                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        downFlag = true;
                        // it's the first pointer, so clear all existing pointers data
                        clearCirclePointer();
                        // check if we've touched inside some circle
                        mCurrentCircle = obtainTouchedCircle(curr);
                        mCurrentCircle.setOrigin(curr);
                        mCirclePointer.put(event.getPointerId(0), mCurrentCircle);
                        mHandler.removeCallbacks(longPressed);
                        mHandler.postDelayed(longPressed, FRAME_RATE);
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        // It secondary pointers, so obtain their ids and check circles
                        pointerId = event.getPointerId(actionIndex);
                        // check if we've touched inside some circle
                        mCurrentCircle = obtainTouchedCircle(curr);
                        mCirclePointer.put(pointerId, mCurrentCircle);
                        mCurrentCircle.setOrigin(curr);
                        invalidate();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        final int pointerCount = event.getPointerCount();
                        for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                            // Some pointer has moved, search it by pointer id
                            pointerId = event.getPointerId(actionIndex);
                            curr = new PointF(event.getX(), event.getY());
                            mCurrentCircle = mCirclePointer.get(pointerId);
                            if (null != mCurrentCircle) {
                                mCurrentCircle.setOrigin(curr);
                            }
                        }
                        invalidate();
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        clearCirclePointer();
                        invalidate();
                        if (mCurrentCircle != null) {
                            invalidate();
                        }
                        mCurrentCircle = null;
                        mHandler.removeCallbacks(longPressed);
                        downFlag = false;
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
            mCirclePointer.clear();
        }

        /**
         * Search and creates new (if needed) circle based on touch area
         *
         * @param touch PointF of touch
         *
         * @return obtained {@link Circle}
         */
        private Circle obtainTouchedCircle(final PointF touch) {
            Circle touchedCircle = getTouchedCircle(touch);
            newCircle = false;
            if (null == touchedCircle) {
                radius = 25;
                int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                touchedCircle = new Circle(touch, radius, randomAndroidColor);
                newCircle = true;

                if (mCircles.size() == CIRCLES_LIMIT) {
                    // remove first circle
                    mCircles.clear();
                }

                mCircles.add(touchedCircle);
                circleCounter++;
            }

            return touchedCircle;
        }

         // Determines touched circle

        private Circle getTouchedCircle(final PointF touch) {
            Circle touched = null;

            for (Circle circle : mCircles) {
                if ((touch.x - (circle.getOrigin().x )) * (touch.x - (circle.getOrigin().x )) + (touch.y - (circle.getOrigin().y )) * (touch.y - (circle.getOrigin().y )) <= circle.getRadius() * circle.getRadius()) {
                    touched = circle;
                    break;
                }
            }
            return touched;
        }

        private Circle getOverlapCircle(final PointF touch) {
            Circle touched = null;
            for (Circle circle : mCircles) {
                if (circle.getOrigin().x + circle.getRadius() + radius > touch.x
                        && circle.getOrigin().x < touch.x + circle.getRadius() + radius
                        && circle.getOrigin().y + circle.getRadius() + radius > touch.y
                        && circle.getOrigin().y < touch.y + circle.getRadius() + radius)
                {
                    touched = circle;
                    break;
                }
            }
            return touched;
        }

        public class GestureListener extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2,
                                   float velocityX, float velocityY) {
                flingCircle = getTouchedCircle(curr);
                flingCircle.setxVelocity(velocityX);
                flingCircle.setyVelocity(velocityY);
                flingCircleArray.add(flingCircle);
                mHandler.post(drawAgain);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent event) {
            }

        }

        public void detectCollisions(Circle firstCircle) {
            for (Circle secondCircle : mCircles) {
                if(firstCircle != secondCircle) {
//                    collides(firstCircle, secondCircle);
                    if (firstCircle.getOrigin().x + firstCircle.getRadius() + secondCircle.getRadius() > secondCircle.getOrigin().x
                            && firstCircle.getOrigin().x < secondCircle.getOrigin().x + firstCircle.getRadius() + secondCircle.getRadius()
                            && firstCircle.getOrigin().y + firstCircle.getRadius() + secondCircle.getRadius() > secondCircle.getOrigin().y
                            && firstCircle.getOrigin().y < secondCircle.getOrigin().y + firstCircle.getRadius() + secondCircle.getRadius())
                    {
                        double distance = Math.sqrt(
                                ((firstCircle.getOrigin().x - secondCircle.getOrigin().x) * (firstCircle.getOrigin().x - secondCircle.getOrigin().x))
                        + ((firstCircle.getOrigin().y - secondCircle.getOrigin().y) * (firstCircle.getOrigin().y - secondCircle.getOrigin().y))
                        );
                        if (distance < firstCircle.getRadius() + secondCircle.getRadius())
                        {
                            //balls have collided
                            calculateNewVelocities(firstCircle, secondCircle);
                        }
                    }
                }
            }
        }

        public void calculateNewVelocities(Circle firstCircle, Circle secondCircle) {
            float mass1 = firstCircle.getRadius();
            float mass2 = secondCircle.getRadius();
            float velX1 = firstCircle.getxVelocity();
            float velX2 = secondCircle.getxVelocity();
            float velY1 = firstCircle.getyVelocity();
            float velY2 = secondCircle.getyVelocity();

            float newVelX1 = (velX1 * (mass1 - mass2) + (2 * mass2 * velX2)) / (mass1 + mass2);
            float newVelX2 = (velX2 * (mass2 - mass1) + (2 * mass1 * velX1)) / (mass1 + mass2);
            float newVelY1 = (velY1 * (mass1 - mass2) + (2 * mass2 * velY2)) / (mass1 + mass2);
            float newVelY2 = (velY2 * (mass2 - mass1) + (2 * mass1 * velY1)) / (mass1 + mass2);
            firstCircle.setxVelocity(newVelX1);
            secondCircle.setxVelocity(newVelX2);
            firstCircle.setyVelocity(newVelY1);
            secondCircle.setyVelocity(newVelY2);

            PointF newOriginFirstCircle = new PointF(firstCircle.getOrigin().x, firstCircle.getOrigin().y);
            PointF newOriginSecondCircle = new PointF(secondCircle.getOrigin().x, secondCircle.getOrigin().y);
            newOriginFirstCircle.x += (dt*newVelX1);
            newOriginFirstCircle.y += (dt*newVelY1);
            newOriginSecondCircle.x += (dt*newVelX2);
            newOriginSecondCircle.y += (dt*newVelY2);

            firstCircle.setOrigin(newOriginFirstCircle);
            secondCircle.setOrigin(newOriginSecondCircle);

        }

        public void collides(Circle b1, Circle b2) {
            //Find the distance between their mid-points
            float dx = b1.getOrigin().x - b2.getOrigin().x,
                    dy = b1.getOrigin().y - b2.getOrigin().y,
                    dist = Math.round(Math.sqrt(dx*dx + dy*dy));

            //Check if it is a collision
            if(dist <= (b1.getRadius() + b2.getRadius())) {
                //Calculate the angles
                double angle = Math.atan2(dy, dx),
                        sin = Math.sin(angle),
                        cos = Math.cos(angle);

                //Calculate the old velocity components
                double v1x = b1.getxVelocity() * cos,
                        v2x = b2.getxVelocity() * cos,
                        v1y = b1.getyVelocity() * sin,
                        v2y = b2.getyVelocity() * sin;

                //Calculate the new velocity components
                double vel1x = ((b1.getRadius() - b2.getRadius()) / (b1.getRadius() + b2.getRadius())) * v1x + (2 * b2.getRadius() / (b1.getRadius() + b2.getRadius())) * v2x,
                        vel2x = (2 * b1.getRadius() / (b1.getRadius() + b2.getRadius())) * v1x + ((b2.getRadius() - b1.getRadius()) / (b2.getRadius() + b1.getRadius())) * v2x,
                        vel1y = v1y,
                        vel2y = v2y;

                //Set the new velocities
                b1.setxVelocity((float) vel1x);
                b2.setxVelocity((float) vel2x);
                b1.setyVelocity((float) vel1y);
                b2.setyVelocity((float) vel2y);

                PointF newOriginFirstCircle = new PointF(b1.getOrigin().x, b1.getOrigin().y);
                PointF newOriginSecondCircle = new PointF(b2.getOrigin().x, b2.getOrigin().y);
                newOriginFirstCircle.x += (dt*vel1x);
                newOriginFirstCircle.y += (dt*vel1y);
                newOriginSecondCircle.x += (dt*v2y);
                newOriginSecondCircle.y += (dt*vel2y);

                b1.setOrigin(newOriginFirstCircle);
                b2.setOrigin(newOriginSecondCircle);

            }
        }

        private void centerOrigin(Canvas canvas) {
            width = canvas.getWidth();
            height = canvas.getHeight();
            canvas.translate(width/2, height/2);
        }

    }
