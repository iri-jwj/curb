package jxpl.scnu.curb.homePage.scholat;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

public class ScholatViewHolder extends AutoFitViewHolder {
    @BindView(R.id.scholat_item_title)
    TextView m_ScholatItemTitle;
    @BindView(R.id.scholat_item_time)
    TextView m_ScholatItemTime;
    @BindView(R.id.scholat_item_end_time)
    TextView m_ScholatItemEndTime;
    @BindView(R.id.scholat_skeleton)
    RelativeLayout m_ScholatSkeleton;
    @BindView(R.id.scholat_item_content)
    TextView m_ScholatItemContent;
    @BindView(R.id.scholat_item_imagebutton_add_reminder)
    ImageButton m_ScholatImagebuttonAddReminder;
    @BindView(R.id.scholat_item_container)
    LinearLayout m_ScholatItemContainer;
    @BindView(R.id.scholat_capacity)
    LinearLayout m_ScholatCapacity;
    @BindView(R.id.scholat_item_image_moreless)
    ImageView m_ScholatItemImageMoreless;
    @BindView(R.id.item_scholat)
    LinearLayout m_ItemScholat;

    private List<ScholatHomework> data = new ArrayList<>();

    public ScholatViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        m_ScholatImagebuttonAddReminder.setOnClickListener(this);
        bindExpandView(m_ScholatSkeleton, m_ScholatItemContainer, m_ScholatItemImageMoreless);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_info_imagebutton_add_reminder) {
            final Context lc_context = v.getContext();
            AlertDialog.Builder lc_builder = new AlertDialog.Builder(v.getContext());
            lc_builder.setTitle("添加日程提醒");
            lc_builder.setMessage(m_ScholatItemTitle.getText());
            lc_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ReminderPresenter.addCalendarEvent(lc_context, m_ScholatItemTitle.getText().toString(), m_ScholatItemContent.getText().toString(), getTime());
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

    public void setData(List<ScholatHomework> para_data) {
        data = para_data;
    }

    private long getTime() {
        String time = data.get(this.getAdapterPosition()).getEndTime();
        SimpleDateFormat lc_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Date lc_date = lc_simpleDateFormat.parse(time, new ParsePosition(0));
        return lc_date.getTime();
    }
}
