package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class MatrialEditView extends EditText {

    Paint paint;
    public static final float PADDING = DeviceUtil.dip2px(80);
    public static final float PADDING_TOP = DeviceUtil.dip2px(20);
    RectF rectF;

    public MatrialEditView(Context context) {
        super(context);
        init();
    }

    public MatrialEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MatrialEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        rectF.set(0, getMeasuredHeight()- PADDING, getWidth(), getMeasuredHeight());
        paint.setColor(Color.parseColor("#26000000"));
        canvas.drawRect(rectF, paint);
        paint.setColor(Color.parseColor("#FF4081"));
        canvas.drawLine(0, getMeasuredHeight(), getWidth(), getMeasuredHeight(), paint);
    }

}
