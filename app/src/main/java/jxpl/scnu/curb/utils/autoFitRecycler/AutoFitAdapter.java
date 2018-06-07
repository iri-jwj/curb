package jxpl.scnu.curb.utils.autoFitRecycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxpl.scnu.curb.R;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AutoFitAdapter<T extends AutoFitViewHolder> extends RecyclerView.Adapter<T>
        implements ExpandReplicateListener, ViewHolderClickListener {

    private final static String TAG = "AutoFitAdapter";
    private final AutoFitRecyclerView m_autoFitRecyclerView;
    protected List<BaseData> dataList = new LinkedList<>();
    private LinkedList<Boolean> m_isExpand = new LinkedList<>();
    private Map<String, Integer> m_expandedHeight = new HashMap<>();
    private Map<String, Integer> m_skeletonHeight = new HashMap<>();
    private Map<String, Boolean> itemIsMeasured = new HashMap<>();
    private int mHiddenViewMeasuredHeight = 0;

    public AutoFitAdapter(AutoFitRecyclerView para_autoFitRecyclerView) {
        m_autoFitRecyclerView = para_autoFitRecyclerView;
    }

    public void replaceData(List<BaseData> para_baseData) {
        checkNotNull(para_baseData);
        setDataList(para_baseData);
    }

    private void setDataList(List<BaseData> para_dataList) {
        dataList = para_dataList;
        m_isExpand.clear();
        for (int i = 0; i < dataList.size(); i++) {
            this.m_isExpand.add(false);
            if (!this.itemIsMeasured.containsKey(dataList.get(i).getStr_id())) {
                this.m_expandedHeight.put(dataList.get(i).getStr_id(), 0);
                this.m_skeletonHeight.put(dataList.get(i).getStr_id(), 0);
                this.itemIsMeasured.put(dataList.get(i).getStr_id(), false);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createMyViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final T holder, final int position) {
        bindMyData(holder, position);
        BaseData i = dataList.get(holder.getAdapterPosition());
        if (!itemIsMeasured.get(i.getStr_id())) {
            holder.getSkeleton().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    BaseData i = dataList.get(holder.getAdapterPosition());
                    if (!itemIsMeasured.get(i.getStr_id())) {
                        int height = m_skeletonHeight.get(i.getStr_id());
                        m_skeletonHeight.remove(i.getStr_id());

                        m_skeletonHeight.put(i.getStr_id(), height + getChildHeight(holder.getSkeleton()));
                        Log.d(TAG, "OnPreDrawListener: position:" + holder.getAdapterPosition());
                        Log.d(TAG, "onPreDraw: id:" + i.getStr_id());
                        Log.d(TAG, "onPreDraw: skeletonHeight:" + height + holder.getSkeleton().getHeight());
                        //View child = holder.itemView;
                        //m_autoFitRecyclerView.changeHeight(child, m_skeletonHeight.get(i.getStr_id()));
                    }
                    holder.getSkeleton().getViewTreeObserver().removeOnPreDrawListener(this);

                    return true;
                }

               /*@Override
                public void onGlobalLayout() {
                   Log.d(TAG, "onGlobalLayout: size:" + dataList.size());
                   BaseData i = dataList.get(holder.getAdapterPosition());
                   if (!itemIsMeasured.get(i.getStr_id())) {
                       m_skeletonHeight.put(i.getStr_id(), holder.getSkeleton().getHeight());
                   }
                   Log.d(TAG, "onGlobalLayout: position:" + holder.getAdapterPosition());
                   holder.getSkeleton().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }*/
            });
            holder.getIndicator().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    BaseData i = dataList.get(holder.getAdapterPosition());
                    if (!itemIsMeasured.get(i.getStr_id())) {
                        int height = m_skeletonHeight.get(i.getStr_id());
                        m_skeletonHeight.remove(i.getStr_id());
                        m_skeletonHeight.put(i.getStr_id(), height + getChildHeight(holder.getIndicator()));
                        itemIsMeasured.remove(i.getStr_id());
                        itemIsMeasured.put(i.getStr_id(), true);
                        Log.d(TAG, "OnPreDrawListener: position:" + holder.getAdapterPosition());
                        Log.d(TAG, "onPreDraw: id:" + i.getStr_id());
                        Log.d(TAG, "onPreDraw: indicatorHeight:" + getChildHeight(holder.getIndicator()));
                        holder.getDetails().setVisibility(View.GONE);
                        View child = holder.itemView;
                        m_autoFitRecyclerView.changeHeight(child, m_skeletonHeight.get(i.getStr_id()));
                    }
                    holder.getIndicator().getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
                /*public void onGlobalLayout() {
                    BaseData i = dataList.get(holder.getAdapterPosition());
                    if (!itemIsMeasured.get(i.getStr_id())) {
                        int height = m_skeletonHeight.get(i.getStr_id());
                        m_skeletonHeight.remove(i.getStr_id());
                        m_skeletonHeight.put(i.getStr_id(), height + holder.getIndicator().getHeight());
                        itemIsMeasured.remove(i.getStr_id());
                        itemIsMeasured.put(i.getStr_id(), true);
                        holder.getDetails().setVisibility(View.GONE);
                        View child = holder.itemView;
                        m_autoFitRecyclerView.changeHeight(child, m_skeletonHeight.get(i.getStr_id()));
                    }
                    Log.d(TAG, "onGlobalLayout: position:" + holder.getAdapterPosition());
                    holder.getIndicator().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }*/
            });

            holder.getDetails().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    BaseData i = dataList.get(holder.getAdapterPosition());
                    if (!itemIsMeasured.get(i.getStr_id())) {
                        m_expandedHeight.put(i.getStr_id(), getChildHeight(holder.getDetails()) + 12);
                        Log.d(TAG, "OnPreDrawListener: position:" + holder.getAdapterPosition());
                        Log.d(TAG, "onPreDraw: id:" + i.getStr_id());
                        Log.d(TAG, "onPreDraw: detailsHeight:" + holder.getDetails().getHeight());
                    }
                    holder.getDetails().getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
                /*@Override
                public void onGlobalLayout() {
                    BaseData i = dataList.get(holder.getAdapterPosition());
                    if (!itemIsMeasured.get(i.getStr_id())) {
                        m_expandedHeight.put(i.getStr_id(), holder.getDetails().getHeight());
                    }
                    Log.d(TAG, "onGlobalLayout: position:" + holder.getAdapterPosition());
                    holder.getDetails().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }*/
            });
        } else {
            if (!m_isExpand.get(position)) {
                holder.getDetails().setVisibility(View.GONE);
                View child = holder.itemView;
                m_autoFitRecyclerView.changeHeight(child, m_skeletonHeight.get(i.getStr_id()));
            }
        }

    }

    private int getChildHeight(View para_view) {
        int height = 0;
        if (para_view instanceof ViewGroup) {
            ViewGroup lc_viewGroup = (ViewGroup) para_view;
            for (int i = 0; i < lc_viewGroup.getChildCount(); i++) {
                View children = lc_viewGroup.getChildAt(i);
                height += getChildHeight(children);
            }
        } else {
            height = para_view.getHeight();
        }
        return height;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public abstract T createMyViewHolder(@NonNull ViewGroup parent, int viewType);

    public abstract void bindMyData(@NonNull final T holder, int position);

    @Override
    public void onExpand(int position) {
        LinearLayoutManager lc_manager = (LinearLayoutManager) m_autoFitRecyclerView.getLayoutManager();
        int firstPosition = lc_manager.findFirstVisibleItemPosition();
        int childPosition = position - firstPosition;
        View child = m_autoFitRecyclerView.getChildAt(childPosition);

        BaseData i = dataList.get(position);
        final T v = (T) m_autoFitRecyclerView.getChildViewHolder(child);
        //m_autoFitRecyclerView.changeHeight(child,m_skeletonHeight.get(i.getId()) + m_expandedHeight.get(i.getId()));
        m_autoFitRecyclerView.changeHeight(child, child.getHeight(),
                m_skeletonHeight.get(i.getStr_id()) + m_expandedHeight.get(i.getStr_id()));
        mHiddenViewMeasuredHeight = m_expandedHeight.get(i.getStr_id());
        v.getDetails().setVisibility(View.VISIBLE);
        animOpen(v.getDetails(), v.getIndicator());
    }

    @Override
    public void onReplicate(int position) {
        LinearLayoutManager lc_manager = (LinearLayoutManager) m_autoFitRecyclerView.getLayoutManager();
        int firstPosition = lc_manager.findFirstVisibleItemPosition();
        int childPosition = position - firstPosition;
        View child = m_autoFitRecyclerView.getChildAt(childPosition);
        BaseData i = dataList.get(position);
        final T v = (T) m_autoFitRecyclerView.getChildViewHolder(child);
        mHiddenViewMeasuredHeight = m_expandedHeight.get(i.getStr_id());
        animClose(v.getDetails(), v.getIndicator());
        //m_autoFitRecyclerView.changeHeight(child,m_skeletonHeight.get(i.getId()));
        m_autoFitRecyclerView.changeHeight(child, child.getHeight(), m_skeletonHeight.get(i.getStr_id()));
    }

    @Override
    public void onViewHolderClick(int position) {
        if (m_isExpand.get(position)) {
            onReplicate(position);
            m_isExpand.set(position, false);
        } else {
            onExpand(position);
            m_isExpand.set(position, true);
        }
    }


    private void animOpen(final View expandView, final ImageView rotateView) {
        expandView.setAlpha(0);
        float from = 0.0f;
        float to = 180.0f;
        RotateAnimation lc_rotateAnimation = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        lc_rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        lc_rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rotateView.setImageResource(R.drawable.ic_info_item_less);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animator mOpenValueAnimator = createDropAnim(expandView, 0, mHiddenViewMeasuredHeight);
        mOpenValueAnimator.start();
        rotateView.startAnimation(lc_rotateAnimation);
    }

    private void animClose(final View view, final ImageView rotateView) {
        int origHeight = view.getHeight();
        view.setAlpha(1);
        float from = 0.0f;
        float to = 180.0f;
        RotateAnimation lc_rotateAnimation = new RotateAnimation(to, from, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        lc_rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        lc_rotateAnimation.setDuration(300);
        lc_rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rotateView.setImageResource(R.drawable.ic_info_item_more);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        Animator mCloseValueAnimator = createDropAnim(view, origHeight, 0);
        mCloseValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        mCloseValueAnimator.start();

        rotateView.startAnimation(lc_rotateAnimation);
    }

    private ValueAnimator createDropAnim(final View view, int start, int end) {
        final ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                float alpha = ((float) value) / mHiddenViewMeasuredHeight;
                view.setAlpha(alpha);
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        return va;
    }
}
