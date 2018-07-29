package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class PieChartView extends View {
    private Paint mPaint;
    private float[] angleArray = {60, 100, 80, 120};
    private int[] colorArray = {Color.parseColor("#9df979"), Color.parseColor("#2b1cf9"),
            Color.parseColor("#f91b35"), Color.parseColor("#e443f9")};

    private final static int PADDING = DeviceUtil.dip2px(50);
    private final static int PADDING_OFFSET = DeviceUtil.dip2px(10);
    private int radius;

    private RectF rectF;

    public int getPullOutofIndex() {
        return pullOutofIndex;
    }

    public void setPullOutofIndex(int pullOutofIndex) {
        this.pullOutofIndex = pullOutofIndex;
        invalidate();
    }

    private int pullOutofIndex;


    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        setPullOutofIndex(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = Math.min(getWidth(), getHeight()) / 2 - PADDING;
        rectF.set(getWidth() / 2 - radius, getHeight() / 2 - radius,
                getWidth() / 2 + radius, getHeight() / 2 + radius);

        float usedAngle = 0;
        for (int i = 0; i < angleArray.length; i++) {
            float angle = angleArray[i];
            int color = colorArray[i];
            mPaint.setColor(color);
            if (i == pullOutofIndex) {
                float angleOffset = usedAngle + angle/2;
                float offsetDx = PADDING_OFFSET * (float)Math.cos(angleOffset * Math.PI/180);
                float offsetDy = PADDING_OFFSET * (float)Math.sin(angleOffset * Math.PI/180);
                rectF.offset(offsetDx, offsetDy);
                canvas.drawArc(rectF, usedAngle, angle, true, mPaint);
                rectF.offset(-offsetDx, -offsetDy);
            } else {
                canvas.drawArc(rectF, usedAngle, angle, true, mPaint);
            }
            usedAngle += angleArray[i];
        }
    }

}
