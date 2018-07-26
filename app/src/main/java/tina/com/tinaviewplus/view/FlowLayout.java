package tina.com.tinaviewplus.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    Rect[] childBounds;
    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;// 如果是warp_content情况下，记录宽和高
        int height = 0;
        int lineWidth = 0;//记录每一行的宽度，width不断取最大宽度
        int lineHeight = 0;//每一行的高度，累加至height
        int cCount = getChildCount();

        if (null == childBounds) {
            childBounds = new Rect[cCount];
        } else if (childBounds.length < cCount) {
            childBounds = Arrays.copyOf(childBounds, cCount);
        }
        // 遍历每个子元素
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            Rect rect = childBounds[i];
            // 测量每一个child的宽和高, lineWidth 用掉的高度， height用掉的宽度
            measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            /**
             * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
             */
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, sizeWidth);// 取最大的
                // 叠加当前高度，
                height += lineHeight;
                measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);
                lineWidth = 0; // 重新开启新行，开始记录
                lineHeight = 0;
            }
            if (null == rect){
                rect = childBounds[i] = new Rect();
            }
            rect.set(lineWidth, height, lineWidth + child.getMeasuredWidth(), height + child.getMeasuredHeight());
            // 否则累加值lineWidth,lineHeight取最大高度
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
        }

        width = Math.max(lineWidth, width);
        height += lineHeight;

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < childBounds.length; i++) {
            View childView = getChildAt(i);
            Rect childBound = childBounds[i];
            childView.layout(childBound.left, childBound.top, childBound.right, childBound.bottom);
        }
    }
}
