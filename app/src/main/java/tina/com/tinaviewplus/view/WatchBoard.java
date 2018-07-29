package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class WatchBoard extends View {

    public final float PADDING = DeviceUtil.dip2px(40);
    public final float PADDING_BOARD = DeviceUtil.dip2px(5);
    public final float SCALE_LENGTH_LONG = DeviceUtil.dip2px(14);
    public final float SCALE_LENGTH_SHORT = DeviceUtil.dip2px(10);
    public final float NUMBER_HEIGHT = DeviceUtil.dip2px(5);
    Paint paint;

    float radius;
    float scaleLength;

    public WatchBoard(Context context) {
        super(context);
    }

    public WatchBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DeviceUtil.dip2px(1));
        radius = getWidth() / 2 - PADDING;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawRedScale(canvas);
        drawPointer(canvas);
    }


    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DeviceUtil.dip2px(1));
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawCircle(0, 0, radius, paint);
    }

    private void drawRedScale(Canvas canvas) {
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                paint.reset();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(DeviceUtil.dip2px(3));
                scaleLength = SCALE_LENGTH_LONG;
                canvas.drawLine(0, -radius + PADDING_BOARD, 0, -radius + PADDING_BOARD + scaleLength, paint);
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                paint.reset();
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(DeviceUtil.dip2px(1));
                paint.setTextSize(DeviceUtil.dip2px(15));
                paint.setTextAlign(Paint.Align.CENTER);
                Rect textBound = new Rect();
                paint.getTextBounds(text, 0, text.length(), textBound);
                int textHeight = textBound.bottom - textBound.top; //获得文字高度
                canvas.save();
                canvas.rotate(-6 * i);
                canvas.translate(0, -radius + NUMBER_HEIGHT + scaleLength + PADDING_BOARD + (textBound.bottom - textBound.top) / 2);
                canvas.drawText(text, 0, -radius + PADDING_BOARD + scaleLength + textHeight + NUMBER_HEIGHT, paint);
                canvas.restore();
            } else {
                paint.reset();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(DeviceUtil.dip2px(1));
                scaleLength = SCALE_LENGTH_SHORT;
                canvas.drawLine(0, -radius + PADDING_BOARD, 0, -radius + PADDING_BOARD + scaleLength, paint);
            }
            canvas.rotate(6);
        }

    }


    private void drawPointer(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(DeviceUtil.dip2px(15));
        canvas.drawPoint(0, 0, paint);
    }

}
