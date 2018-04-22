package jxpl.scnu.curb.utils.autoFitRecycler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.HashMap;
import java.util.Map;

public class AutoFitRecyclerView extends RecyclerView {

    private final static String TAG = "AutoFitRecyclerView";
    private Map<View, Integer> defaultHeightMap = new HashMap<>();

    public AutoFitRecyclerView(Context context) {
        super(context);
    }

    public AutoFitRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onChildDetachedFromWindow(View child) {
        super.onChildDetachedFromWindow(child);
    }

    public void changeHeight(View child, int para_targetHeight) {
        ViewGroup.LayoutParams lc_layoutParams = child.getLayoutParams();
        if (para_targetHeight == 0) {
            lc_layoutParams.height = defaultHeightMap.get(child);
        } else {
            if (!defaultHeightMap.containsKey(child))
                defaultHeightMap.put(child, lc_layoutParams.height);
            lc_layoutParams.height = para_targetHeight;
        }
        child.requestLayout();
    }

    public void changeHeight(final View child, int start, int end) {
        final ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                layoutParams.height = value;
                child.setLayoutParams(layoutParams);//设置高度
            }
        });
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.start();
    }

}
