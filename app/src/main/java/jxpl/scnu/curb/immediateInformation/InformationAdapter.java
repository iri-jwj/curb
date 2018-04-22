package jxpl.scnu.curb.immediateInformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitAdapter;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitRecyclerView;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;

import static com.google.common.base.Preconditions.checkNotNull;


public class InformationAdapter extends AutoFitAdapter<InformationViewHolder> {
    private static final String TAG = "InformationAdapter";
    private final AutoFitRecyclerView m_autoFitRecyclerView;
    private List<ImmediateInformation> immediateInformations;
    private Context m_context;
    private Map<UUID, Integer> m_expandedHeight = new HashMap<>();
    private Map<UUID, Integer> m_skeletonHeight = new HashMap<>();
    private Map<UUID, Boolean> itemIsMeasured = new HashMap<>();
    private Animator mOpenValueAnimator = null;
    private Animator mCloseValueAnimator = null;
    private int mHiddenViewMeasuredHeight = 0;

    public InformationAdapter(List<ImmediateInformation> para_immediateInformations,
                              Context para_context,
                              AutoFitRecyclerView para_autoFitRecyclerView) {
        immediateInformations = para_immediateInformations;
        for (int i = 0; i < immediateInformations.size(); i++)
            this.m_isExpand.add(false);

        m_context = para_context;
        m_autoFitRecyclerView = para_autoFitRecyclerView;

    }

    public List<ImmediateInformation> getImmediateInformations() {
        return immediateInformations;
    }

    private void setImmediateInformations(List<ImmediateInformation> para_immediateInformations) {
        immediateInformations = para_immediateInformations;
        m_isExpand.clear();
        for (int i = 0; i < immediateInformations.size(); i++) {
            this.m_isExpand.add(false);
            if (!this.itemIsMeasured.containsKey(immediateInformations.get(i).getId())) {
                this.m_expandedHeight.put(immediateInformations.get(i).getId(), 0);
                this.m_skeletonHeight.put(immediateInformations.get(i).getId(), 0);
                this.itemIsMeasured.put(immediateInformations.get(i).getId(), false);
            }
        }
        notifyDataSetChanged();
    }


    public void replaceInfo(List<ImmediateInformation> immediateInformations) {
        setImmediateInformations(immediateInformations);
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_information, parent, false);
        InformationViewHolder m_holder = new InformationViewHolder(view);
        m_holder.setListener(this);
        return m_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final InformationViewHolder holder, int position) {
        ImmediateInformation lc_information = immediateInformations.get(position);
        //holder.infoImage.setImageResource(getImageIdByType(ImmediateInformation.getType()));
        holder.m_InfoItemImage.setImageResource(R.drawable.ic_item_edu_info);
        holder.m_InfoItemTitle.setText(lc_information.getTitle());
        final String content = lc_information.getContent();
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
                        return holder.m_ItemInfoContent.getWidth();
                    }

                    @Override
                    public boolean fitWidth() {
                        return false;
                    }
                })
                .into(holder.m_ItemInfoContent);

        holder.m_SkeletonInfo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImmediateInformation i = immediateInformations.get(holder.getAdapterPosition());
                if (!itemIsMeasured.get(i.getId())) {
                    m_skeletonHeight.put(i.getId(), holder.m_SkeletonInfo.getHeight());
                }
                holder.m_SkeletonInfo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        holder.m_imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImmediateInformation i = immediateInformations.get(holder.getAdapterPosition());
                if (!itemIsMeasured.get(i.getId())) {
                    int height = m_skeletonHeight.get(i.getId());
                    m_skeletonHeight.remove(i.getId());
                    m_skeletonHeight.put(i.getId(), height + holder.m_imageView.getHeight());
                    itemIsMeasured.remove(i.getId());
                    itemIsMeasured.put(i.getId(), true);
                    holder.m_ItemInfoContainer.setVisibility(View.GONE);
                    View child = holder.itemView;
                    m_autoFitRecyclerView.changeHeight(child, m_skeletonHeight.get(i.getId()));
                }
                holder.m_imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        holder.m_ItemInfoContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImmediateInformation i = immediateInformations.get(holder.getAdapterPosition());

                if (!itemIsMeasured.get(i.getId())) {
                    m_expandedHeight.put(i.getId(), holder.m_ItemInfoContainer.getHeight());
                }
                holder.m_ItemInfoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        Log.d(TAG, "onBindViewHolder: time:" + lc_information.getTime());
        SimpleDateFormat lc_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Date lc_createTime = null;
        long currentTime = System.currentTimeMillis();
        try {
            lc_createTime = lc_simpleDateFormat.parse(lc_information.getCreateTime());
        } catch (ParseException para_e) {
            para_e.printStackTrace();
        }
        String timeToShow;
        if (lc_createTime != null) {
            long deviation = currentTime - lc_createTime.getTime();
            double days = deviation / (1000 * 60 * 60 * 24);
            if (days < 1) {
                if (deviation / (1000 * 60 * 60) >= 1)
                    timeToShow = Math.round(deviation / (1000 * 60 * 60)) + "小时前";
                else
                    timeToShow = Math.round(deviation / (1000 * 60)) + "分钟前";
            } else {
                timeToShow = Math.round(days) + "天前";
            }
            holder.m_InfoItemTime.setText(timeToShow);
        }
    }

    @Override
    public int getItemCount() {
        return immediateInformations == null ? 0 : immediateInformations.size();
    }

    @Override
    public void onExpand(int position) {
        LinearLayoutManager lc_manager = (LinearLayoutManager) m_autoFitRecyclerView.getLayoutManager();
        int firstPosition = lc_manager.findFirstVisibleItemPosition();
        int childPosition = position - firstPosition;
        View child = m_autoFitRecyclerView.getChildAt(childPosition);

        ImmediateInformation i = immediateInformations.get(position);
        final InformationViewHolder v = (InformationViewHolder) m_autoFitRecyclerView.getChildViewHolder(child);
        //m_autoFitRecyclerView.changeHeight(child,m_skeletonHeight.get(i.getId()) + m_expandedHeight.get(i.getId()));
        m_autoFitRecyclerView.changeHeight(child, child.getHeight(),
                m_skeletonHeight.get(i.getId()) + m_expandedHeight.get(i.getId()));
        mHiddenViewMeasuredHeight = m_expandedHeight.get(i.getId());
        v.m_ItemInfoContainer.setVisibility(View.VISIBLE);
        animOpen(v.m_ItemInfoContainer, v.m_imageView);
        Log.d(TAG, "onExpand: v.m_ItemInfoContainer:" + (v.m_ItemInfoContainer.getVisibility() == View.GONE));
    }

    @Override
    public void onReplicate(int position) {
        LinearLayoutManager lc_manager = (LinearLayoutManager) m_autoFitRecyclerView.getLayoutManager();
        int firstPosition = lc_manager.findFirstVisibleItemPosition();
        int childPosition = position - firstPosition;
        View child = m_autoFitRecyclerView.getChildAt(childPosition);
        ImmediateInformation i = immediateInformations.get(position);
        InformationViewHolder v = (InformationViewHolder) m_autoFitRecyclerView.getChildViewHolder(child);
        mHiddenViewMeasuredHeight = m_expandedHeight.get(i.getId());
        animClose(v.m_ItemInfoContainer, v.m_imageView);
        Log.d(TAG, "onReplicate: v.m_ItemInfoContainer:" + (v.m_ItemInfoContainer.getVisibility() == View.GONE));
        //m_autoFitRecyclerView.changeHeight(child,m_skeletonHeight.get(i.getId()));
        m_autoFitRecyclerView.changeHeight(child, child.getHeight(), m_skeletonHeight.get(i.getId()));
    }

    @Override
    public void onViewHolderClick(int position) {
        if (m_isExpand.get(position)) {
            Log.d(TAG, "onViewHolderClick: 收起");
            onReplicate(position);
            m_isExpand.set(position, false);
        } else {
            onExpand(position);
            Log.d(TAG, "onViewHolderClick: 展开");
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

        mOpenValueAnimator = createDropAnim(expandView, 0, mHiddenViewMeasuredHeight);
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
        mCloseValueAnimator = createDropAnim(view, origHeight, 0);
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
