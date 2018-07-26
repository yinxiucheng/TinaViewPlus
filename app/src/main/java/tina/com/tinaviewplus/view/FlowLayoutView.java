package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @date 2018/7/18
 */
public class FlowLayoutView extends ViewGroup {

    public FlowLayoutView(Context context) {
        super(context);
        init();
    }

    public FlowLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected LayoutParams generateLayoutParams(
            LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    private List<List<View>> mAllViews = new ArrayList<>();//总共的行数

    private List<Integer> mMaxLineHeight = new ArrayList<>();//每一行中的最大高度

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = childView.getHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth) {//换一行
                width = Math.max(lineWidth, sizeWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == childCount - 1) {//最后一行
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear();
        mMaxLineHeight.clear();

        int sizeWidth = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();
        List<View> lineViews = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth) {//换行
                mMaxLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineWidth = 0;
                lineViews = new ArrayList<>();
            }
            lineHeight = Math.max(lineHeight, childHeight);
            lineWidth += childWidth;
            lineViews.add(childView);
        }

        mMaxLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        int lineNumbers = mAllViews.size();
        int left = 0;
        int top = 0;

        for (int i = 0; i < lineNumbers; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mMaxLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View childView = lineViews.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                int lc = left + lp.leftMargin;
                int rc = lc + childView.getMeasuredWidth();
                int tc = top + lp.topMargin;
                int bc = tc + childView.getMeasuredHeight();
                childView.layout(lc, tc, rc, bc);

                left += childView.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

    private void init() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
