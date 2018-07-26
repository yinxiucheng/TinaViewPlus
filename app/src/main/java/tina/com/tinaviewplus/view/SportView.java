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
public class SportView extends View {

    Paint paint;
    Paint textPaint;


    private float radius;
    public static final float PADDING = DeviceUtil.dip2px(40);
    private RectF rectF;

    public SportView(Context context) {
        super(context);
        init();
    }

    public SportView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DeviceUtil.dip2px(20));
        rectF = new RectF();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(DeviceUtil.dip2px(40));
        textPaint.setColor(Color.GREEN);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        radius = getWidth() / 2 - PADDING;
        rectF.set(PADDING, getHeight() / 2 - radius, getWidth() - PADDING, getHeight() / 2 + radius);

        paint.setColor(Color.GRAY);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        paint.setColor(Color.RED);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, -90, 270, false, paint);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.descent + fontMetrics.ascent;
        float baseTextLine = canvas.getHeight()/2 - textHeight/2;


        paint.setStrokeWidth(DeviceUtil.dip2px(1));
        canvas.drawText("75%", getWidth() / 2, baseTextLine, textPaint);
        canvas.drawLine(0, canvas.getHeight() / 2, getWidth(), canvas.getHeight() / 2, paint);
    }


}
