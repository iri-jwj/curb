package jxpl.scnu.curb.homePage.immediateInformation;

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

    public InformationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        //标识需要计算高度的几个部分：
        bindExpandView(m_SkeletonInfo, m_ItemInfoContainer, m_imageView);
    }
}
