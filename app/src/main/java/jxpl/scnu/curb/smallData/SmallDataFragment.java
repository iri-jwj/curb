package jxpl.scnu.curb.smallData;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;
import static com.google.common.base.Preconditions.checkNotNull;

public class SmallDataFragment extends Fragment implements SmallDataInterface.View {
    @BindView(R.id.sd_content_image_image)
    ImageView m_SdContentImageImage;
    @BindView(R.id.sd_content_text_title)
    TextView m_SdContentTextTitle;
    @BindView(R.id.sd_content_text_description)
    TextView m_SdContentTextDescription;
    @BindView(R.id.sd_content_text_author)
    TextView m_SdContentTextAuthor;
    @BindView(R.id.cardView)
    CardView m_CardView;
    @BindView(R.id.sd_content_imagebutton_favorite)
    ImageButton m_SdContentImagebuttonFavorite;
    @BindView(R.id.sd_content_imagebutton_before)
    ImageButton m_SdContentImagebuttonBefore;
    @BindView(R.id.sd_content_imagebutton_after)
    ImageButton m_SdContentImagebuttonAfter;
    @BindView(R.id.sd_content_imagebutton_result)
    ImageButton m_SdContentImagebuttonResult;
    @BindView(R.id.sd_item_question_title)
    TextView m_SdItemQuestionTitle;
    @BindView(R.id.sd_item_radiobutton_up)
    RadioButton m_SdItemRadiobuttonUp;
    @BindView(R.id.sd_item_radiobutton_down)
    RadioButton m_SdItemRadiobuttonDown;
    @BindView(R.id.sd_item_radioGroup)
    RadioGroup m_SdItemRadioGroup;
    @BindView(R.id.sd_item_imagebutton_left)
    ImageButton m_SdItemImagebuttonLeft;
    @BindView(R.id.sd_item_imagebutton_right)
    ImageButton m_SdItemImagebuttonRight;
    @BindView(R.id.sd_item_button_commit)
    Button m_SdItemButtonCommit;
    Unbinder unbinder;
    @BindView(R.id.small_data_content)
    ConstraintLayout m_SmallDataContent;
    @BindView(R.id.small_data_item)
    ConstraintLayout m_SmallDataItem;

    private List<SDSummary> m_sdSummaries = new LinkedList<>();
    private List<SDDetail> m_sdDetailsCache = new LinkedList<>();
    private int indexForSummary = 0;
    private int indexForDetail = 0;
    private SmallDataInterface.Presenter presenter;

    public SmallDataFragment() {
        // Required empty public constructor
    }


    @NonNull
    public static SmallDataFragment newInstance() {
        return new SmallDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_small_data, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(SmallDataInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    private void setLoadingIndicator(boolean direction) {
        if (direction) {
            Snackbar.make(checkNotNull(getView()), R.string.sd_loading_new_SD, 1500);
        } else {
            Snackbar.make(checkNotNull(getView()), R.string.sd_loading_old_SD, 1500);
        }
    }

    @Override
    public void showSummaries(List<SDSummary> para_sdSummaries) {
        indexForSummary = 0;
        m_sdSummaries = checkNotNull(para_sdSummaries);
        showSummary(m_sdSummaries.get(indexForSummary));
    }

    private void showSummary(SDSummary para_sdSummary) {
        Glide.with(this)
                .load(para_sdSummary.getImg())
                .apply(fitCenterTransform())
                .into(m_SdContentImageImage);

        m_SdContentTextTitle.setText(para_sdSummary.getTitle());
        m_SdContentTextDescription.setText(para_sdSummary.getDescription());
        m_SdContentTextAuthor.setText(para_sdSummary.getCreator());
    }

    @Override
    public void showDetails(List<SDDetail> para_sdDetails) {
        m_sdDetailsCache = checkNotNull(para_sdDetails);
        showDetail(para_sdDetails.get(indexForDetail));
    }

    private void showDetail(SDDetail para_sdDetail) {
        m_SmallDataContent.setVisibility(View.GONE);
        m_SmallDataItem.setVisibility(View.VISIBLE);
        m_SdItemQuestionTitle.setText(para_sdDetail.getQuestion());
        m_SdItemRadiobuttonUp.setText(para_sdDetail.getOptionTwo());
        m_SdItemRadiobuttonDown.setText(para_sdDetail.getOptionTwo());
    }

    @Override
    public void showPopUpMenu(Context para_context) {

    }

    @Override
    public String getCurrentSummaryID() {
        return m_sdSummaries.get(indexForSummary).getId().toString();
    }
/*    *//**
     * 当用户点击了一个问卷，将当前显示的内容替换为问卷的详细内容
     *//*
    @Override
    public void changeToQuestionnaire() {

    }

    *//**
     * 当用户点击了提交或返回键时，将当前的内容替换回问卷列表
     *//*
    @Override
    public void changeBack() {

    }*/

    /**
     * 将已填写的问卷更换一个颜色
     */
    @Override
    public void markAnswered() {

    }

    /**
     * 当出现了某些错误，如问卷未完成时调用这个方法
     *
     * @param error 错误信息
     */
    @Override
    public void showError(String error) {
        View view = checkNotNull(getView());
        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.cardView)
    public void onM_CardViewClicked() {
        presenter.loadDetails(getCurrentSummaryID());
    }

    @OnClick(R.id.sd_content_imagebutton_favorite)
    public void onM_SdContentImagebuttonFavoriteClicked() {

    }

    @OnClick(R.id.sd_content_imagebutton_before)
    public void onM_SdContentImagebuttonBeforeClicked() {
        if (indexForSummary == 0) {
            setLoadingIndicator(true);
            presenter.loadSummaries(true);
        } else {

        }
    }

    @OnClick(R.id.sd_content_imagebutton_after)
    public void onM_SdContentImagebuttonAfterClicked() {
    }

    @OnClick(R.id.sd_content_imagebutton_result)
    public void onM_SdContentImagebuttonResultClicked() {
    }

    @OnClick(R.id.sd_item_radiobutton_up)
    public void onM_SdItemRadiobuttonUpClicked() {
    }

    @OnClick(R.id.sd_item_radiobutton_down)
    public void onM_SdItemRadiobuttonDownClicked() {
    }

    @OnClick(R.id.sd_item_imagebutton_left)
    public void onM_SdItemImagebuttonLeftClicked() {
    }

    @OnClick(R.id.sd_item_imagebutton_right)
    public void onM_SdItemImagebuttonRightClicked() {
    }

    @OnClick(R.id.sd_item_button_commit)
    public void onM_SdItemButtonCommitClicked() {
    }
}
