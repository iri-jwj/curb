package jxpl.scnu.curb.immediateInformation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.immediateInformation.informationCreate.InformationCreateActivity;
import jxpl.scnu.curb.utils.ScrollChildSwipeRefreshLayout;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;

import static com.google.common.base.Preconditions.checkNotNull;

public class InformationFragment extends Fragment implements InformationContract.View {

    @BindView(R.id.info_recycler)
    RecyclerView infoRecycler;
    @BindView(R.id.refresh_info_layout)
    ScrollChildSwipeRefreshLayout refreshInfoLayout;
    Unbinder unbinder;

    private InformationContract.Presenter presenter;
    private InfoAdapter m_minfoAdapter;

    public InformationFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Override
    public void setPresenter(@NonNull InformationContract.Presenter mPresenter) {
        presenter = checkNotNull(mPresenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_minfoAdapter = new InfoAdapter(new ArrayList<ImmediateInformation>(0));
        Log.d("CreateInfoView", "CreateAdapter");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View info = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, info);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        infoRecycler.setLayoutManager(layoutManager);
        infoRecycler.setAdapter(m_minfoAdapter);

        //设置一些动画，但是没有显示出来
        infoRecycler.addItemDecoration(new DividerItemDecoration(
                checkNotNull(getActivity()), DividerItemDecoration.HORIZONTAL));

        //设置一些颜色，但是我并不知道在哪里用的
        refreshInfoLayout.setColorSchemeColors(
                ContextCompat.getColor(checkNotNull(getActivity()), R.color.colorPrimary),
                ContextCompat.getColor(checkNotNull(getActivity()), R.color.colorAccent),
                ContextCompat.getColor(checkNotNull(getActivity()), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the custom SwipeRefreshLayout.
        refreshInfoLayout.setScrollUpChild(infoRecycler);
        refreshInfoLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadInformation(true);
            }
        });
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, info);
        return info;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragement_info_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //todo 添加点击事件
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        Log.d("InfoFrag", "onViewCreated: ");
        super.onViewCreated(view, bundle);
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void showInfo(List<ImmediateInformation> immediateInformations) {
        Log.d("informationFragment", "showInfo: " + immediateInformations.isEmpty());
        m_minfoAdapter.replaceInfo(immediateInformations);
    }


    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null)
            return;
        refreshInfoLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshInfoLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public int getImageIdByType(String type) {
        int imageId;
        switch (type) {
            case "EDU":
                imageId = R.drawable.ic_item_edu_info;
                break;
            case "PRA":
                imageId = R.drawable.ic_filter_practice;
                break;
            case "SCHOLAR":
                imageId = R.drawable.ic_filter_scholar;
                break;
            default:
                imageId = 0;
        }
        return imageId;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showInformationDetailsUi(UUID id, Context context) {
        Intent intent = new Intent(context, InformationCreateActivity.class);
        intent.putExtra(InformationCreateActivity.INFO_ID, id);
        startActivity(intent);
    }


    @Override
    public void showLoadingError() {
        showLoadingErrorMessage(getString(R.string.info_loading_error));
    }


    @Override
    public void showNoInfo() {
        showLoadingErrorMessage(getString(R.string.info_nothing));
    }


    @Override
    public void showNoNewInfo() {
        showLoadingErrorMessage(getString(R.string.info_no_new_info_error));
    }

    @Override
    public boolean isListShowing() {
        return m_minfoAdapter.getItemCount() == 0;
    }

    /**
     * 用于显示各种错误信息
     * @param message 错误信息的内容
     */
    private void showLoadingErrorMessage(String message) {
        View view = checkNotNull(getView());
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public List<ImmediateInformation> getCurrentList() {
        return m_minfoAdapter.getInformation();
    }

    /**
     * 内部类，RecyclerView的Adapter
     * last update:3.24
     */
    class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
        Unbinder unBinderAdapter;
        private List<ImmediateInformation> immediateInformations;
        private ViewHolder m_holder;

        InfoAdapter(List<ImmediateInformation> immediateInformations) {
            this.immediateInformations = immediateInformations;
        }

        void replaceInfo(List<ImmediateInformation> immediateInformations) {
            setImmediateInformations(immediateInformations);
            checkNotNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        private void setImmediateInformations(List<ImmediateInformation> immediateInformations) {
            this.immediateInformations = checkNotNull(immediateInformations);
        }

        List<ImmediateInformation> getInformation() {
            return immediateInformations;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_information, viewGroup, false);
            m_holder = new ViewHolder(view);
            /*holder.infoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    ImmediateInformation immediateInformation = immediateInformations.get(position);
                    presenter.openInformationDetails(immediateInformation, getActivity());
                }
            });*/
            return m_holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ImmediateInformation ImmediateInformation = immediateInformations.get(position);
            //holder.infoImage.setImageResource(getImageIdByType(ImmediateInformation.getType()));
            holder.m_InfoItemImage.setImageResource(R.drawable.ic_item_edu_info);
            holder.m_InfoItemTitle.setText(ImmediateInformation.getTitle());
        }

        @Override
        public int getItemCount() {
            return immediateInformations.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.info_item_imagebutton_more)
            ImageButton m_InfoItemImagebuttonMore;
            @BindView(R.id.info_item_image)
            ImageView m_InfoItemImage;
            @BindView(R.id.info_item_time)
            TextView m_InfoItemTime;
            @BindView(R.id.info_item_title)
            TextView m_InfoItemTitle;
            @BindView(R.id.item_info_content)
            TextView m_ItemInfoContent;
            @BindView(R.id.item_info_imagebutton_less)
            ImageButton m_ItemInfoImagebuttonLess;
            @BindView(R.id.item_info_imagebutton_add_reminder)
            ImageButton m_ItemInfoImagebuttonAddReminder;
            @BindView(R.id.item_info_container)
            ConstraintLayout m_ItemInfoContainer;
            @BindView(R.id.info_item_constraint)
            LinearLayout m_InfoItemConstraint;
            View infoView;

            ViewHolder(View itemView) {
                super(itemView);
                infoView = itemView;
                unBinderAdapter = ButterKnife.bind(this, itemView);
            }

        }

        @OnClick({R.id.info_item_imagebutton_more, R.id.item_info_imagebutton_less, R.id.item_info_imagebutton_add_reminder})
        public void onViewClicked(View view) {

            int position = m_holder.getAdapterPosition();
            final String content = immediateInformations.get(position).getContent();
            switch (view.getId()) {
                case R.id.info_item_imagebutton_more:
                    checkNotNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_holder.m_InfoItemImagebuttonMore.setVisibility(View.GONE);
                            m_holder.m_ItemInfoContainer.setVisibility(View.VISIBLE);
                            HtmlText.from(content)
                                    .setImageLoader(new HtmlImageLoader() {
                                        @Override
                                        public void loadImage(String para_s, final Callback para_callback) {
                                            Glide.with(InformationFragment.this)
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
                                            return ContextCompat.getDrawable(checkNotNull(getContext())
                                                    , R.drawable.ic_info_detail_loading_circle);
                                        }

                                        @Override
                                        public Drawable getErrorDrawable() {
                                            return ContextCompat.getDrawable(checkNotNull(getContext())
                                                    , R.drawable.ic_info_image_error);
                                        }

                                        @Override
                                        public int getMaxWidth() {
                                            return m_holder.m_ItemInfoContent.getWidth();
                                        }

                                        @Override
                                        public boolean fitWidth() {
                                            return false;
                                        }
                                    })
                                    .into(m_holder.m_ItemInfoContent);
                        }
                    });
                    break;
                case R.id.item_info_imagebutton_less:
                    checkNotNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_holder.m_ItemInfoContainer.setVisibility(View.GONE);
                            m_holder.m_InfoItemImagebuttonMore.setVisibility(View.VISIBLE);
                        }
                    });
                    break;
                case R.id.item_info_imagebutton_add_reminder:
                    //todo 添加到提醒
                    break;
            }
        }

    }
}
