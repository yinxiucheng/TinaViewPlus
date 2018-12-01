package tina.com.scaleimageview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

/**
 * @author yxc
 * @date 2018/12/1
 */
public class ScalableImageView extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, Runnable {

    private static final float IMAGE_WIDTH = Utils.dpToPixel(300);
    private static final float OVER_SCALE_FACTOR = 1.5f;

    Bitmap bitmap;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float orignalOffsetX;
    float orignalOffsetY;
    float offsetX;
    float offsetY;
    float smallScale;
    float bigScale;
    boolean big;
    float scaleFraction; //0 ~1f
    ObjectAnimator scaleAnimator;

    GestureDetectorCompat detector;
    OverScroller overScroller;


    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
        detector = new GestureDetectorCompat(context, this);
        overScroller = new OverScroller(context);
//        detector.setOnDoubleTapListener(this);
        //关闭长按
//        detector.setIsLongpressEnabled(false);
    }


    private float getScaleFraction() {
        return scaleFraction;
    }

    private void setScaleFraction(float scaleFraction) {
        this.scaleFraction = scaleFraction;
        invalidate();
    }

    private ObjectAnimator getScaleAnimator() {
        if (scaleAnimator == null) {
            scaleAnimator = ObjectAnimator.ofFloat(this, "scaleFraction", 0, 1);
        }
        return scaleAnimator;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        offsetX = ((float) getWidth() - bitmap.getWidth()) / 2;
        offsetY = ((float) getHeight() - bitmap.getHeight()) / 2;

        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        float scale = big ? bigScale : smallScale;

        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);
        float scale = smallScale + (bigScale - smallScale) * scaleFraction;

        canvas.scale(scale, scale, getWidth() / 2f, getHeight() / 2f);
        canvas.drawBitmap(bitmap, orignalOffsetX, orignalOffsetY, paint);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //ACTION_DOWN事件
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //这里无论父控件是否是 滑动控件，都设定pre时间
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {//单次按下抬起时响应
        return false;
    }

    /**
     * onMove
     *
     * @param down      事件
     * @param event     move 的事件
     * @param distanceX
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent down, MotionEvent event, float distanceX, float distanceY) {
        if (big) {//小图不动
            offsetX -= distanceX;
            offsetY -= distanceY;
            fixOffset();

            invalidate();
        }
        return false;
    }

    private void fixOffset(){
        offsetX = Math.min(offsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetX = Math.max(offsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetY = Math.min(offsetY, (bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetY = Math.max(offsetY, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
    }

    //长按
    @Override
    public void onLongPress(MotionEvent e) {

    }

    //惯性滑动
    @Override
    public boolean onFling(MotionEvent down, MotionEvent event, float velocityX, float velocityY) {
        if (big) {
            overScroller.fling((int) offsetX, (int) offsetX, (int) velocityX, (int) velocityY,
                    -(bitmap.getWidth() - getWidth()) / 2,
                    (bitmap.getWidth() - getWidth()) / 2,
                    -(bitmap.getHeight() - getHeight()) / 2,
                    (bitmap.getHeight() - getHeight()) / 2,
                    20, 20);

//            for (int i = 10; i<100; i += 10){
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh();
//                    }
//                }, i);
//            }
            //负责启动这个循环
            postOnAnimation(this);
        }
        return false;
    }

//    void refresh(){
//        overScroller.computeScrollOffset();
//        offsetX = overScroller.getCurrX();
//        offsetY = overScroller.getCurrY();
//        invalidate();
//    }


    //======DoubleTap
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {//单击确认
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {//300ms 之内算双击， 低于40ms不算做双击
        big = !big;
        if (big) {
            offsetX = (e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2f) * bigScale / smallScale;
            offsetY = (e.getY() - getHeight() / 2f) - (e.getY() - getHeight() / 2f) * bigScale / smallScale;
            fixOffset();
            getScaleAnimator().start();
        } else {
            getScaleAnimator().reverse();
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {//之后的移动 + 抬起都会触发这个方法
        return false;
    }


    @Override
    public void run() {
        if (overScroller.computeScrollOffset()) {//动画没结束
            offsetX = overScroller.getCurrX();
            offsetY = overScroller.getCurrY();
            invalidate();
            postOnAnimation(this);
        }
    }
}
