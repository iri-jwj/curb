package jxpl.scnu.curb.homePage.immediateInformation;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.reminder.ReminderPresenter;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitViewHolder;

class InformationViewHolder extends AutoFitViewHolder {
    @BindView(R.id.info_item_image)
    ImageView m_InfoItemImage;
    @BindView(R.id.info_item_title)
    TextView m_InfoItemTitle;
    @BindView(R.id.info_item_time)
    TextView m_InfoItemTime;
    @BindView(R.id.skeleton_info)
    RelativeLayout m_SkeletonInfo;
    @BindView(R.id.item_info_content)
    TextView m_ItemInfoContent;
    @BindView(R.id.item_info_imagebutton_add_reminder)
    ImageButton m_ItemInfoImagebuttonAddReminder;
    @BindView(R.id.item_info_container)
    LinearLayout m_ItemInfoContainer;
    @BindView(R.id.item_capacity)
    LinearLayout m_ItemCapacity;
    @BindView(R.id.info_item_image_more_less)
    ImageView m_imageView;
    @BindView(R.id.info_item_out_parent)
    LinearLayout m_InfoItemConstraint;

    private List<ImmediateInformation> data = new ArrayList<>();

    public InformationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        m_ItemInfoImagebuttonAddReminder.setOnClickListener(this);
        //标识需要计算高度的几个部分：
        bindExpandView(m_SkeletonInfo, m_ItemInfoContainer, m_imageView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_info_imagebutton_add_reminder) {
            final Context lc_context = v.getContext();
            AlertDialog.Builder lc_builder = new AlertDialog.Builder(v.getContext());
            lc_builder.setTitle("添加日程提醒");
            lc_builder.setMessage(m_InfoItemTitle.getText());
            lc_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ReminderPresenter.addCalendarEvent(lc_context, m_InfoItemTitle.getText().toString(), m_ItemInfoContent.getText().toString(), getTime());
                }
            });
            lc_builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            lc_builder.create().show();
        } else
            super.onClick(v);
    }

    protected void setData(List<ImmediateInformation> para_immediateInformations) {
        if (para_immediateInformations != null && para_immediateInformations.size() > 0)
            data = para_immediateInformations;
    }

    private long getTime() {

        String time = data.get(this.getAdapterPosition()).getTime();
        Log.d("informationViewHolder", "getTime: " + (time==null));
        SimpleDateFormat lc_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Date lc_date = lc_simpleDateFormat.parse(time, new ParsePosition(0));
        return lc_date.getTime();
    }
}
