package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class AvatarView extends View {

    Paint paint;
    Paint textPaint;


    private float radius;
    public static final float PADDING = DeviceUtil.dip2px(40);
    private RectF rectF;
    Bitmap bitmap;

    public AvatarView(Context context) {
        super(context);
        init();
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian);

        rectF = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.set(getWidth() / 2 - PADDING, getHeight() / 2 - PADDING, getWidth() / 2 + PADDING, getHeight() / 2 + PADDING);
        float radius = getWidth() / 2 - PADDING;

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, null, rectF, paint);

    }

}
