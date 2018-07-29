package tina.com.tinaviewplus.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class ScaleableView extends View implements Runnable {

    public static final String LOG_TAG = "ScaleableView";

    Paint paint;
    public static final float PADDING = DeviceUtil.dip2px(20);
    public static final float SCALE_TIMES = 2;
    Bitmap bitmap;
    float imageWidth;
    float imageHeight;
    float offsetX;
    float offsetY;
    float orignalOffsetX;
    float orignalOffsetY;
    float smallScale;
    float bigScale;
    boolean big;
    float currentScale;
    float oldCurrentScale;

    ObjectAnimator animator;
    OverScroller scroller;

    protected boolean mIsAutoScaling;
    protected boolean mDoubleTapEnabled = true;
    protected boolean mScaleEnabled = true;
    protected boolean mScrollEnabled = true;

    protected GestureListener gestureListener;
    protected DoubleTapListener doubleTapListener;
    protected ScaleGestureListener mScaleListener;

    GestureDetector mGestureDetector;
    ScaleGestureDetector mScaleDetector;


    public ScaleableView(Context context) {
        super(context);
        init(context);
    }

    public ScaleableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScaleableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.batman);
        imageWidth = bitmap.getWidth();
        imageHeight = bitmap.getHeight();

        gestureListener = new GestureListener();
        doubleTapListener = new DoubleTapListener();
        mScaleListener = new ScaleGestureListener();
        mGestureDetector = new GestureDetector(context, gestureListener);
        mGestureDetector.setOnDoubleTapListener(doubleTapListener);
        mScaleDetector = new ScaleGestureDetector(context, mScaleListener);
        scroller = new OverScroller(context);
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }

    private ObjectAnimator getAnimator(float beginScale, float endScale) {
        if (null == animator) {
            animator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale);
        }
//        animator.setFloatValues(currentScale, bigScale);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                mIsAutoScaling = false;
                if (isReverse) {
                    offsetX = 0;
                    offsetY = 0;
                }
            }
        });
        return animator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }

//        return mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        orignalOffsetX = (getWidth() - imageWidth) / 2;
        orignalOffsetY = (getHeight() - imageHeight) / 2;

        if (imageWidth / imageHeight > (float) getWidth() / getHeight()) {//胖的图片的缩放比
            smallScale = getWidth() / imageWidth;
            bigScale = getHeight() / imageHeight * SCALE_TIMES;
        } else {
            smallScale = getHeight() / imageHeight;
            bigScale = getWidth() / imageWidth * SCALE_TIMES;
        }
        currentScale = smallScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scalingFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scalingFraction, offsetY * scalingFraction);
        canvas.scale(currentScale, currentScale, getWidth() / 2, getHeight() / 2);
        canvas.translate(orignalOffsetX, orignalOffsetY);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public void run() {
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.getCurrX();
            offsetY = scroller.getCurrY();
            invalidate();
            postOnAnimation(this);
        }
    }

    public final class DoubleTapListener implements GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//300ms之类 没有被调用，单击事件
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mIsAutoScaling) {
                return true;
            }
            big = !big;
            if (big) {
                offsetX = (getWidth() - imageWidth) / 2;
                offsetY = (getHeight() - imageHeight) / 2;
                mIsAutoScaling = true;
                getAnimator(smallScale, bigScale).start();
            } else {
                mIsAutoScaling = true;
                getAnimator(smallScale, bigScale).reverse();
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {//双击会调用它，但是之后的move， up事件都会传进来。
            return false;
        }
    }

    public final class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScale = oldCurrentScale * detector.getScaleFactor();
            currentScale = Math.max(currentScale, smallScale);
            currentScale = Math.min(currentScale, bigScale);
            if (currentScale == bigScale) {
                big = true;
            } else {
                big = false;
            }
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            oldCurrentScale = currentScale;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    public final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//300ms之内没有被调用，单击事件
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!mScrollEnabled) return false;
            if (e1 == null || e2 == null) return false;
            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            if (mScaleDetector.isInProgress()) return false;

            float widthdefualt = (imageWidth * bigScale - getWidth()) / 2;
            float heightdefualt = (imageHeight * bigScale - getHeight()) / 2;
            if (big) {
                offsetX -= distanceX;
                offsetX = Math.min(offsetX, widthdefualt);
                offsetX = Math.max(offsetX, -widthdefualt);
                offsetY -= distanceY;
                offsetY = Math.min(offsetY, heightdefualt);
                offsetY = Math.max(offsetY, -heightdefualt);
                invalidate();
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!mScrollEnabled) return false;
            if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            if (mScaleDetector.isInProgress()) return false;
//            if ( getScale() == 1f ) return false;
            scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                    (int) (getWidth() - imageWidth * bigScale) / 2, (int) (imageWidth * bigScale - getWidth()) / 2,
                    (int) (getHeight() - imageHeight * bigScale) / 2, (int) (imageHeight * bigScale - getHeight()) / 2, 25, 25);

            postOnAnimation(ScaleableView.this);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {//单击被调用,没有设置DoubleClickLisenter时。
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

}
