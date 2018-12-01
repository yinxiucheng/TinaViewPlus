package tina.com.mutiltouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author yxc
 * @date 2018/12/2
 */
public class MutiTouchView extends View {

    private static final float image_width = Utils.dpToPixel(200);

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    private float offsetX;
    private float offsetY;
    private float downX;
    private float downY;
    private float orignalOffsetX;
    private float orignalOffsetY;

    public MutiTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                orignalOffsetX = offsetX;
                orignalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = orignalOffsetX + event.getX() - downX;
                offsetY = orignalOffsetY + event.getY() - downY;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }



}
