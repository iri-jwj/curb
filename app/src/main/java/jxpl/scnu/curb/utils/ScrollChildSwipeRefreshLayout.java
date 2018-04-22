package jxpl.scnu.curb.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author iri-jwj
 * @version 1
 */


public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {
    private View mScrollUpChild;

    public ScrollChildSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ScrollChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
            return mScrollUpChild.canScrollVertically(-1);

        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }
}
