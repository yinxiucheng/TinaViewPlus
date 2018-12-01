package tina.com.scaleimageview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;


/**
 * @author yxc
 * @date 2018/7/18
 */
public class Utils {

    public static float dpToPixel(float dp) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    public static Bitmap getAvatar(Resources res, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, R.drawable.avatar_rengwuxian, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(res, R.drawable.avatar_rengwuxian, options);
    }




}
