package jxpl.scnu.curb.homePage.smallData;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.smallData.result.ResultActivity;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author iri-jwj
 * @version 1
 * 2018-3-23
 */
public class SmallDataFragment extends Fragment implements SmallDataInterface.View, FragmentBackHandler {
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
        setHasOptionsMenu(true);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_small_data, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(SmallDataInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    /**
     * 显示等待界面
     *
     * @param direction 获取信息的方向
     */
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

    /**
     * 内部调用的显示summary的方法，前台单个单个显示
     *
     * @param para_sdSummary 目标 summary
     */
    private void showSummary(final SDSummary para_sdSummary) {
        final String title = para_sdSummary.getTitle();
        final String description = para_sdSummary.getDescription();
        final String creator = para_sdSummary.getCreator();
        if (para_sdSummary.getImg() != null) {
            checkNotNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity()
                            .getApplicationContext())
                            .asBitmap()
                            .load(para_sdSummary.getImg())
                            .apply(centerCropTransform())
                            .into(m_SdContentImageImage);
                }
            });
        }
        checkNotNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_SdContentTextTitle.setText(title);
                m_SdContentTextDescription.setText(description);
                m_SdContentTextAuthor.setText(creator);
                if (indexForSummary == 0) {
                    m_SdContentImagebuttonBefore.setImageResource(R.drawable.ic_sd_refresh_blue);
                } else {
                    m_SdContentImagebuttonBefore.setImageResource(R.drawable.ic_sd_left);
                }
                if (indexForSummary == (m_sdSummaries.size() - 1)) {
                    m_SdContentImagebuttonAfter.setImageResource(R.drawable.ic_refresh_grey);
                } else {
                    m_SdContentImagebuttonAfter.setImageResource(R.drawable.ic_sd_right);
                }
                //当问卷是已完成时，改变背景颜色
                if (para_sdSummary.isHasFinish()) {
                    m_CardView.setCardBackgroundColor(getResources()
                            .getColor(R.color.card_small_data_finished));

                } else {
                    m_CardView.setCardBackgroundColor(getResources()
                            .getColor(R.color.card_small_data_unfinished));
                }
            }
        });
    }

    @Override
    public void showDetails(List<SDDetail> para_sdDetails) {
        indexForDetail = 0;
        m_sdDetailsCache = checkNotNull(para_sdDetails);
        showDetail(para_sdDetails.get(indexForDetail));
    }

    /**
     * 内部调用的显示方法，单个显示
     *
     * @param para_sdDetail 要显示的detail
     */
    private void showDetail(SDDetail para_sdDetail) {
        final String question = para_sdDetail.getQuestion();
        final String optionOne = para_sdDetail.getOption1();
        final String optionTwo = para_sdDetail.getOption2();
        checkNotNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_SmallDataContent.setVisibility(View.GONE);
                m_SmallDataItem.setVisibility(View.VISIBLE);
                if (indexForDetail == 0)
                    m_SdItemImagebuttonLeft.setClickable(false);
                else
                    m_SdItemImagebuttonLeft.setClickable(true);

                if (indexForDetail == (m_sdDetailsCache.size() - 1)) {
                    m_SdItemImagebuttonRight.setClickable(false);
                    if (!m_sdSummaries.get(indexForSummary).isHasFinish())
                        m_SdItemButtonCommit.setVisibility(View.VISIBLE);
                } else {
                    m_SdItemImagebuttonRight.setClickable(true);
                    m_SdItemButtonCommit.setVisibility(View.GONE);
                }
                m_SdItemQuestionTitle.setText(question);
                m_SdItemRadiobuttonUp.setText(optionOne);
                m_SdItemRadiobuttonDown.setText(optionTwo);
                int chosenAnswer = presenter.getChosenAnswer(m_sdDetailsCache.get(indexForDetail).getQuestionNum());
                switch (chosenAnswer) {
                    case 1:
                        m_SdItemRadioGroup.check(m_SdItemRadiobuttonUp.getId());
                        break;
                    case 2:
                        m_SdItemRadioGroup.check(m_SdItemRadiobuttonDown.getId());
                        break;
                    default:
                        m_SdItemRadioGroup.clearCheck();
                        break;

                }
            }
        });
    }

    /**
     * {onBackPressed()}
     * back键点击监听，若当前显示的是details，点击后返回summaries
     * 若当前是summaries，则实现原本的back事件
     *
     * @return boolean
     */
    @Override
    public boolean onBackPressed() {
        if (m_SmallDataContent.getVisibility() == View.GONE) {
            if (presenter.getAnswerState() && !m_sdSummaries.get(indexForSummary).isHasFinish()) {
                Dialog lc_dialog = new AlertDialog.Builder(getContext()).setTitle("嘿，确认返回吗？")
                        .setMessage("返回将丢失您当前已填写的内容哦")
                        .setNegativeButton("不了!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton("返回！", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemToContent();
                            }
                        })
                        .create();
                lc_dialog.show();
            } else {
                itemToContent();
            }
            return true;
        }
        return false;
    }

    /**
     * 从问题的界面返回到概要界面
     */
    private void itemToContent() {
        m_SmallDataItem.setVisibility(View.GONE);
        m_SmallDataContent.setVisibility(View.VISIBLE);
        //假设反回时数据会丢失
        showSummary(m_sdSummaries.get(indexForSummary));
    }

    /**
     * @param para_context 用于实现弹出按钮
     * @deprecated
     */
    @Override
    public void showPopUpMenu(Context para_context) {

    }

    @Override
    public String getCurrentSummaryID() {
        return m_sdSummaries.get(indexForSummary).getId().toString();
    }


    /**
     * {void showError(String)}
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
        if (!m_sdSummaries.isEmpty()) {
            presenter.setSummary(m_sdSummaries.get(indexForSummary));
            presenter.loadDetails(getCurrentSummaryID());
        } else
            showError("no summary");
    }

    @OnClick(R.id.sd_content_imagebutton_favorite)
    public void onM_SdContentImagebuttonFavoriteClicked() {
        //todo 添加加入喜爱（收藏）的逻辑
    }

    /**
     * {onM_SdContentImagebuttonBeforeClicked()}
     * 向左按钮的点击事件，判断indexForSummary的值
     * 若为 0 ,即第一个summary ， 就刷新 {@link #m_sdSummaries}
     * 若不为 0 ，即显示上一条summary ， {@link #indexForSummary} 减一
     */
    @OnClick(R.id.sd_content_imagebutton_before)
    public void onM_SdContentImagebuttonBeforeClicked() {
        if (indexForSummary == 0) {
            setLoadingIndicator(true);
            presenter.loadSummaries(true, 1);
        } else {
            showSummary(m_sdSummaries.get(--indexForSummary));
        }
    }

    /**
     * {void onM_SdContentImagebuttonAfterClicked()}
     * 向右按钮的点击事件，判断{@link #indexForSummary}的值
     * 若到了最后，则向后刷新以前的summaries信息
     * 若不在最后，则显示后一条summary信息
     */
    @OnClick(R.id.sd_content_imagebutton_after)
    public void onM_SdContentImagebuttonAfterClicked() {
        if (indexForSummary == (m_sdSummaries.size() - 1)) {
            setLoadingIndicator(false);
            presenter.loadSummaries(false, 2);
        } else {
            showSummary(m_sdSummaries.get(++indexForSummary));
        }
    }

    @OnClick(R.id.sd_content_imagebutton_result)
    public void onM_SdContentImagebuttonResultClicked() {
        Intent lc_intent = new Intent(presenter.getContextInPresenter(), ResultActivity.class);
        lc_intent.putExtra("summary_id", m_sdSummaries.get(indexForSummary).getId());
        Log.d("sdf", "onM_SdContentImagebuttonResultClicked: ");
        startActivity(lc_intent);
    }

    @OnClick(R.id.sd_item_radiobutton_up)
    public void onM_SdItemRadiobuttonUpClicked() {
        presenter.saveAnswerToMap(m_sdDetailsCache.get(indexForDetail).getQuestionNum(), 1);

        if (indexForDetail != (m_sdDetailsCache.size() - 1)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onM_SdItemImagebuttonRightClicked();
                }
            },500);
        }
    }

    @OnClick(R.id.sd_item_radiobutton_down)
    public void onM_SdItemRadiobuttonDownClicked() {
        presenter.saveAnswerToMap(m_sdDetailsCache.get(indexForDetail).getQuestionNum(), 2);
        if (indexForDetail != (m_sdDetailsCache.size() - 1)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onM_SdItemImagebuttonRightClicked();
                }
            },500);
        }
    }

    @OnClick(R.id.sd_item_imagebutton_left)
    public void onM_SdItemImagebuttonLeftClicked() {
        showDetail(m_sdDetailsCache.get(--indexForDetail));
        /*int chosenAnswer = presenter.getChosenAnswer(m_sdDetailsCache.get(indexForDetail).getQuestionNum());
        switch (chosenAnswer){
            case 1:m_SdItemRadioGroup.check(m_SdItemRadiobuttonUp.getId());break;
            case 2:m_SdItemRadioGroup.check(m_SdItemRadiobuttonDown.getId());break;
            default:m_SdItemRadioGroup.clearCheck();break;

        }*/
    }

    @OnClick(R.id.sd_item_imagebutton_right)
    public void onM_SdItemImagebuttonRightClicked() {
        showDetail(m_sdDetailsCache.get(++indexForDetail));
        /*int chosenAnswer = presenter.getChosenAnswer(m_sdDetailsCache.get(indexForDetail).getQuestionNum());
        switch (chosenAnswer){
            case 1:m_SdItemRadioGroup.check(m_SdItemRadiobuttonUp.getId());break;
            case 2:m_SdItemRadioGroup.check(m_SdItemRadiobuttonDown.getId());break;
            default:m_SdItemRadioGroup.clearCheck();break;
        }*/
    }

    @OnClick(R.id.sd_item_button_commit)
    public void onM_SdItemButtonCommitClicked() {
        presenter.commitAnswer();
        presenter.markAnswered(m_sdSummaries.get(indexForSummary)
                .getId()
                .toString());
        m_SmallDataItem.setVisibility(View.GONE);
        m_SmallDataContent.setVisibility(View.VISIBLE);
        m_sdSummaries.get(indexForSummary).setHasFinish(true);
        //假设反回时数据会丢失
        showSummary(m_sdSummaries.get(indexForSummary));
    }
}
