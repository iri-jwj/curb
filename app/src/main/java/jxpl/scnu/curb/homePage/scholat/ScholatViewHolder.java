package jxpl.scnu.curb.homePage.scholat;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
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

    public ScholatViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        bindExpandView(m_ScholatSkeleton, m_ScholatItemContainer, m_ScholatItemImageMoreless);
    }
}