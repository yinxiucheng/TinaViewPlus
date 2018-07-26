package tina.com.tinaviewplus.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author yxc
 * @date 2018/7/24
 */
public class SquarelImageView extends android.support.v7.widget.AppCompatImageView {

    public SquarelImageView(Context context) {
        super(context);
    }

    public SquarelImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquarelImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widethMeasure = getMeasuredWidth();
        int heightMeasure = getMeasuredHeight();

        if (widethMeasure > heightMeasure){
            widethMeasure = heightMeasure;
        }else {
            heightMeasure = widethMeasure;
        }
        setMeasuredDimension(widethMeasure, heightMeasure);
    }
}
