package tina.com.tinaviewplus.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.util.DeviceUtil;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class MatrialEditView extends android.support.v7.widget.AppCompatEditText {

    Paint paint;
    public static final float PADDING = DeviceUtil.dip2px(80);
    public static final float PADDING_TOP = DeviceUtil.dip2px(20);

    public static final float LABEL_SIZE = DeviceUtil.dip2px(12);
    public static final float LABEL_PADDING = DeviceUtil.dip2px(5);
    public static final float LABEL_OFFSET = DeviceUtil.dip2px(4);

    public static final float TOTAL_OFFSET = DeviceUtil.dip2px(8);

    boolean labelShow;
    ObjectAnimator animator;
    ObjectAnimator animator2;
    float labFraction;
    boolean useFloatingLabel;

    public float getLabFraction() {
        return labFraction;
    }

    public void setLabFraction(float labFraction) {
        this.labFraction = labFraction;
        invalidate();
    }


    public MatrialEditView(Context context) {
        super(context);
    }

    public MatrialEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MatrialEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(DeviceUtil.dip2px(13));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MatrialEditView);
        useFloatingLabel = typedArray.getBoolean(R.styleable.MatrialEditView_use_floating_label, true);

        if (useFloatingLabel) {
            setPadding(getPaddingLeft(), (int) (getPaddingTop() + LABEL_SIZE + LABEL_PADDING),
                    getPaddingRight(), getPaddingBottom());
        } else {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && !labelShow) {
                    labelShow = true;
                    getAnimator().start();
                } else if (s.length() == 0 && labelShow) {
                    labelShow = false;
                    getAnimator().reverse();
                }
            }
        });
    }

    private ObjectAnimator getAnimator() {
        if (null == animator) {
            animator = ObjectAnimator.ofFloat(this, "labFraction", 0, 1);
            animator.setDuration(800);
        }
        return animator;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getText().length() > 0 && useFloatingLabel) {
            CharSequence lable = getHint();
            paint.setAlpha((int) (labFraction * 0xFF));
            float offset = (1 - labFraction) * TOTAL_OFFSET;
            canvas.drawText((String) lable, 0, lable.length(), LABEL_OFFSET, LABEL_SIZE + LABEL_PADDING + offset, paint);
            paint.setAlpha(1);
        }
    }

}
