package jxpl.scnu.curb.homePage.immediateInformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitAdapter;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitRecyclerView;
import jxpl.scnu.curb.utils.autoFitRecycler.BaseData;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;

import static com.google.common.base.Preconditions.checkNotNull;


public class InformationAdapter extends AutoFitAdapter<InformationViewHolder> {
    private Context m_context;

    InformationAdapter(Context para_context, AutoFitRecyclerView para_autoFitRecyclerView) {
        super(para_autoFitRecyclerView);
        m_context = para_context;
    }

    public List<ImmediateInformation> getImmediateInformations() {
        List<ImmediateInformation> lc_informations = new LinkedList<>();
        for (BaseData base :
                dataList) {
            lc_informations.add((ImmediateInformation) base);
        }
        return lc_informations;
    }

    @Override
    public InformationViewHolder createMyViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_information, parent, false);
        InformationViewHolder m_holder = new InformationViewHolder(view);
        m_holder.setListener(this);
        m_holder.setData(getImmediateInformations());
        return m_holder;
    }

    @Override
    public void bindMyData(@NonNull final InformationViewHolder holder, int position) {
        //ImmediateInformation lc_information = immediateInformations.get(position);
        ImmediateInformation lc_information = (ImmediateInformation) dataList.get(position);
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
}
