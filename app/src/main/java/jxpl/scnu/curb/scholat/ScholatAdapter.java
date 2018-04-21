package jxpl.scnu.curb.scholat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitAdapter;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitRecyclerView;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;

import static com.google.common.base.Preconditions.checkNotNull;

public class ScholatAdapter extends AutoFitAdapter<ScholatViewHolder> {

    private List<ScholatHomework> m_scholatHomeworks;
    private final AutoFitRecyclerView m_autoFitRecyclerView;
    private Context m_context;
    private Map<UUID, Integer> m_expandedHeight = new HashMap<>();
    private Map<UUID, Integer> m_skeletonHeight = new HashMap<>();
    private Map<UUID, Boolean> itemIsMeasured = new HashMap<>();

    private int mHiddenViewMeasuredHeight = 0;

    ScholatAdapter(List<ScholatHomework> para_scholatHomeworks, Context para_context,
                   AutoFitRecyclerView para_autoFitRecyclerView) {
        m_scholatHomeworks = para_scholatHomeworks;
        for (int i = 0; i < m_scholatHomeworks.size(); i++)
            this.m_isExpand.add(false);

        m_context = para_context;
        m_autoFitRecyclerView = para_autoFitRecyclerView;
    }

    public List<ScholatHomework> getScholatHomeworks() {
        return m_scholatHomeworks;
    }

    public void repalceHomeworks(List<ScholatHomework> para_homeworkList) {
        setScholatHomeworks(para_homeworkList);
    }

    private void setScholatHomeworks(List<ScholatHomework> para_homeworkList) {
        m_scholatHomeworks = para_homeworkList;
        m_isExpand.clear();
        for (int i = 0; i < m_scholatHomeworks.size(); i++) {
            this.m_isExpand.add(false);
            if (!this.itemIsMeasured.containsKey(m_scholatHomeworks.get(i).getHomeworkId())) {
                this.m_expandedHeight.put(m_scholatHomeworks.get(i).getHomeworkId(), 0);
                this.m_skeletonHeight.put(m_scholatHomeworks.get(i).getHomeworkId(), 0);
                this.itemIsMeasured.put(m_scholatHomeworks.get(i).getHomeworkId(), false);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScholatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scholat_homework, parent, false);
        ScholatViewHolder m_holder = new ScholatViewHolder(view);
        m_holder.setListener(this);
        return m_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ScholatViewHolder holder, int position) {
        final ScholatHomework lc_homework = m_scholatHomeworks.get(position);
        holder.m_ScholatItemTitle.setText(lc_homework.getTitle());
        holder.m_ScholatItemTime.setText(lc_homework.getCreateTime());
        holder.m_ScholatItemEndTime.setText(lc_homework.getEndTime());
        final String content = lc_homework.getContent();
        HtmlText.from(content)
                .setImageLoader(new HtmlImageLoader() {
                    @Override
                    public void loadImage(String para_s, final Callback para_callback) {
                        Glide.with(m_context)
                                .asBitmap()
                                .load(para_s)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        para_callback.onLoadComplete(resource);
                                    }
                                });
                    }

                    @Override
                    public Drawable getDefaultDrawable() {
                        return ContextCompat.getDrawable(checkNotNull(m_context)
                                , R.drawable.ic_info_detail_loading_circle);
                    }

                    @Override
                    public Drawable getErrorDrawable() {
                        return ContextCompat.getDrawable(checkNotNull(m_context)
                                , R.drawable.ic_info_image_error);
                    }

                    @Override
                    public int getMaxWidth() {
                        return holder.m_ScholatItemContent.getWidth();
                    }

                    @Override
                    public boolean fitWidth() {
                        return false;
                    }
                })
                .into(holder.m_ScholatItemContent);


        holder.m_ScholatSkeleton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!itemIsMeasured.get(lc_homework.getHomeworkId())) {
                    m_skeletonHeight.put(lc_homework.getHomeworkId(), holder.m_ScholatSkeleton.getHeight());
                }
                holder.m_ScholatSkeleton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        holder.m_imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!itemIsMeasured.get(lc_homework.getHomeworkId())) {
                    int height = m_skeletonHeight.get(lc_homework.getHomeworkId());
                    m_skeletonHeight.remove(lc_homework.getHomeworkId());
                    m_skeletonHeight.put(lc_homework.getHomeworkId(), height + holder.m_imageView.getHeight());
                    itemIsMeasured.remove(lc_homework.getHomeworkId());
                    itemIsMeasured.put(lc_homework.getHomeworkId(), true);
                    holder.m_ScholatItemContainer.setVisibility(View.GONE);
                    View child = holder.itemView;
                    m_autoFitRecyclerView.changeHeight(child, m_skeletonHeight.get(lc_homework.getHomeworkId()));
                }
                holder.m_imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        holder.m_ScholatItemContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!itemIsMeasured.get(lc_homework.getHomeworkId())) {
                    m_expandedHeight.put(lc_homework.getHomeworkId(), holder.m_ScholatItemContainer.getHeight());
                }
                holder.m_ScholatItemContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_scholatHomeworks == null ? 0 : m_scholatHomeworks.size();
    }

    @Override
    public void onExpand(int position) {
        LinearLayoutManager lc_manager = (LinearLayoutManager) m_autoFitRecyclerView.getLayoutManager();
        int firstPosition = lc_manager.findFirstVisibleItemPosition();
        int childPosition = position - firstPosition;
        View child = m_autoFitRecyclerView.getChildAt(childPosition);

        ScholatHomework lc_homework = m_scholatHomeworks.get(position);
        final ScholatViewHolder v = (ScholatViewHolder) m_autoFitRecyclerView.getChildViewHolder(child);
        //m_autoFitRecyclerView.changeHeight(child,m_skeletonHeight.get(i.getId()) + m_expandedHeight.get(i.getId()));
        m_autoFitRecyclerView.changeHeight(child, child.getHeight(),
                m_skeletonHeight.get(lc_homework.getHomeworkId()) + m_expandedHeight.get(lc_homework.getHomeworkId()));
        mHiddenViewMeasuredHeight = m_expandedHeight.get(lc_homework.getHomeworkId());
        v.m_ScholatItemContainer.setVisibility(View.VISIBLE);
        animOpen(v.m_ScholatItemContainer, v.m_imageView);
    }

    @Override
    public void onReplicate(int position) {
        LinearLayoutManager lc_manager = (LinearLayoutManager) m_autoFitRecyclerView.getLayoutManager();
        int firstPosition = lc_manager.findFirstVisibleItemPosition();
        int childPosition = position - firstPosition;
        View child = m_autoFitRecyclerView.getChildAt(childPosition);
        ScholatHomework lc_homework = m_scholatHomeworks.get(position);
        ScholatViewHolder v = (ScholatViewHolder) m_autoFitRecyclerView.getChildViewHolder(child);
        mHiddenViewMeasuredHeight = m_expandedHeight.get(lc_homework.getHomeworkId());
        animClose(v.m_ScholatItemContainer, v.m_imageView);
        //m_autoFitRecyclerView.changeHeight(child,m_skeletonHeight.get(i.getId()));
        m_autoFitRecyclerView.changeHeight(child, child.getHeight(), m_skeletonHeight.get(lc_homework.getHomeworkId()));
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
