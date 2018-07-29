package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class AvatarView extends View {

    Paint paint;
    private float radius;
    public static final float PADDING = DeviceUtil.dip2px(40);
    public static final float RADIUS_PADDING = DeviceUtil.dip2px(10);
    Xfermode xfermode;

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
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float radius = Math.min(getWidth(), getHeight()) / 2 - PADDING;
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        int save = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint);
        canvas.drawCircle(getWidth()/2, getHeight()/2, radius - RADIUS_PADDING, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(getAvatar((int) radius * 2), getWidth()/2 - radius, getHeight()/2 - radius, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(save);
    }


    private Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
    }


}
