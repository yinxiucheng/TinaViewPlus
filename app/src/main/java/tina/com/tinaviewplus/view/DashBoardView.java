package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class DashBoardView extends View {

    public static final int PADDING = DeviceUtil.dip2px(40);
    public static final int ANGLE = 120;
    public static final int DASH_WIDTH = DeviceUtil.dip2px(2);
    public static final int DASH_HEIGHT = DeviceUtil.dip2px(5);

    public static final int DASH_WIDTH_RED = DeviceUtil.dip2px(5);
    public static final int DASH_HEIGHT_RED = DeviceUtil.dip2px(10);

    public static final int TOTAL = 20;
    public static final int TOTALRED = 5;

    public static final int POINT_PADDING = DeviceUtil.dip2px(3);

    private Paint circlePaint;
    private Path dash;
    private Path dashred;
    private PathEffect dashEffect;
    private PathMeasure pathMeasure;
    private Path path;
    private int value;//角度值
    private float pointLength;//指针长度
    private float radius;

    private Paint dashPaint;
    private Paint pointPaint;//指针笔

    public DashBoardView(Context context) {
        super(context);
        init();
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        dash = new Path();
        path = new Path();
        dashred = new Path();
        pathMeasure = new PathMeasure();
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(DeviceUtil.dip2px(2));
        circlePaint.setColor(Color.BLACK);

        dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashPaint.setStyle(Paint.Style.FILL);
        dashPaint.setStrokeWidth(DeviceUtil.dip2px(5));
        dashPaint.setColor(Color.RED);

        dash.addRect(0, 0, DASH_WIDTH, DASH_HEIGHT, Path.Direction.CCW);
        dashred.addRect(0, 0, DASH_WIDTH_RED, DASH_HEIGHT_RED, Path.Direction.CCW);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(DeviceUtil.dip2px(5));
        pointPaint.setColor(Color.BLACK);
        setValue(12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = getWidth()/2 - PADDING;
        drawCircle(canvas);
        drawRedDash(canvas);
        drawPoint(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawCircle(Canvas canvas){
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius,
                getHeight() / 2 + radius, 90 + ANGLE / 2, 360 - ANGLE, false, circlePaint);
        path.addArc(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius,
                getHeight() / 2 + radius, 90 + ANGLE / 2, 360 - ANGLE);
        pathMeasure.setPath(path, false);

        dashEffect = new PathDashPathEffect(dash, (pathMeasure.getLength() - DASH_WIDTH) / TOTAL, 0, PathDashPathEffect.Style.ROTATE);

        circlePaint.setPathEffect(dashEffect);

        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius,
                getHeight() / 2 + radius, 90 + ANGLE / 2, 360 - ANGLE, false, circlePaint);
        circlePaint.setPathEffect(null);
    }

    private void drawRedDash(Canvas canvas){
        canvas.save();
        dashEffect = new PathDashPathEffect(dashred, (pathMeasure.getLength() - DASH_WIDTH) / TOTALRED, 0, PathDashPathEffect.Style.ROTATE);
        dashPaint.setPathEffect(dashEffect);
        canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius,
                getHeight() / 2 + radius, 90 + ANGLE / 2, 360 - ANGLE, false, dashPaint);
        canvas.restore();
    }

    /**
     * 绘制指针
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas){
        canvas.save();
        //绘制指针
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, DeviceUtil.dip2px(8), pointPaint);
        pointLength = radius * 3/4;
        int angle = getAngle(value);
        float stopX = (float) (getWidth()/2 + pointLength * Math.cos(angle * Math.PI/180) );
        float stopY = (float) (getHeight()/2 + pointLength * Math.sin(angle * Math.PI/180));
        canvas.drawLine(getWidth() / 2, getHeight() / 2, stopX, stopY, pointPaint);

        canvas.restore();
    }

    public void setValue(int value) {
        this.value = value;
        invalidate();
    }

    public int getAngle(int value) {
        return 90 + ANGLE / 2 + (360 - ANGLE) / TOTAL * value;
    }

}
