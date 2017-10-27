package jxpl.scnu.curb.immediateInformation;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.ScrollChildSwipeRefreshLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class InformationFragment extends Fragment implements InformationContract.View {

    @BindView(R.id.info_recycler)
    RecyclerView infoRecycler;
    @BindView(R.id.info_floatingAb)
    FloatingActionButton infoFloatingAb;
    @BindView(R.id.refresh_info_layout)
    ScrollChildSwipeRefreshLayout refreshInfoLayout;


    Unbinder unbinder;
    private InformationContract.Presenter presenter;
    private infoAdapter minfoAdapter;
    private List<ImmediateInformation> immediateInformations;

    public InformationFragment() {
        // Required empty public constructor
    }

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

        minfoAdapter = new infoAdapter(immediateInformations);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View info = inflater.inflate(R.layout.fragment_information, container, false);
        RecyclerView recyclerView = info.findViewById(R.id.info_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(info.getContext());//不明代码= =
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(minfoAdapter);

        //SET UP floatingActionBar
        FloatingActionButton fab = getActivity().findViewById(R.id.info_floatingAb);
        fab.setImageResource(R.drawable.fliter_arrow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                info.findViewById(R.id.refresh_info_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadInformation(false);
            }
        });

        unbinder = ButterKnife.bind(this, info);
        return info;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class infoAdapter extends RecyclerView.Adapter<infoAdapter.ViewHolder> {
        private List<ImmediateInformation> ImmediateInformations;

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.info_item_image)
            ImageView infoImage;
            TextView infoTitle;
            TextView infoTime;

            ViewHolder(View itemView) {
                super(itemView);
                infoImage = itemView.findViewById(R.id.info_item_image);
                infoTitle = itemView.findViewById(R.id.info_item_title);
                infoTime = itemView.findViewById(R.id.info_item_time);
            }
        }

        public infoAdapter(List<ImmediateInformation> ImmediateInformations) {
            this.ImmediateInformations = ImmediateInformations;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.informations_item, viewGroup, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImmediateInformation ImmediateInformation = ImmediateInformations.get(position);
            holder.infoImage.setImageResource(getImageIdByType(ImmediateInformation.getType()));
            holder.infoTitle.setText(ImmediateInformation.getTitle());
            holder.infoTime.setText(ImmediateInformation.getTime());
        }

        @Override
        public int getItemCount() {
            return ImmediateInformations.size();
        }
    }

    @Override
    public void showFilteringPopUpMenu(Context context) {
        PopupMenu popup = new PopupMenu(context, getActivity().findViewById(R.id.info_floatingAb));
        popup.getMenuInflater().inflate(R.menu.info_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.info_education:
                        presenter.setFiltering(InformationFilter.EDU_INFORMATIONS);
                        break;
                    case R.id.info_scholar:
                        presenter.setFiltering(InformationFilter.SCHOLAR_INFORMATIONS);
                        break;
                    case R.id.info_work_study:
                        presenter.setFiltering(InformationFilter.WORK_STUDY_INFORMATIONS);
                        break;
                    case R.id.info_practice:
                        presenter.setFiltering(InformationFilter.PRA_INFORMATIONS);
                        break;
                    default:
                        presenter.setFiltering(InformationFilter.ALL_INFORMATIONS);
                        break;
                }
                presenter.loadInformation(false);
                return true;
            }
        });
        popup.show();
    }


    @Override
    public void setLoadingIndicator(final boolean active) {
        if(getView()==null)
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
                imageId = R.drawable.item_edu_info;
                break;
            case "PRA":
                imageId = R.drawable.pra;
                break;
            case "SCHOLAR":
                imageId = R.drawable.scholar;
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
    public void showInformationDetailsUi() {

    }

    @Override
    public void showLoadingError() {

    }
}
