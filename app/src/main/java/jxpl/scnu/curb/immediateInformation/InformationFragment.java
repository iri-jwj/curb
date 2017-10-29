package jxpl.scnu.curb.immediateInformation;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    public void showInfo(List<ImmediateInformation> immediateInformations) {
        minfoAdapter.replaceInfo(immediateInformations);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View info = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this,info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(info.getContext());//不明代码= =
        infoRecycler.setLayoutManager(layoutManager);
        infoRecycler.setAdapter(minfoAdapter);

        //SET UP floatingActionBar
        infoFloatingAb.setImageResource(R.drawable.fliter_arrow);
        infoFloatingAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilteringPopUpMenu(getActivity());
            }
        });

        refreshInfoLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        refreshInfoLayout.setScrollUpChild(infoRecycler);

        refreshInfoLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadInformation(false);
            }
        });
        setHasOptionsMenu(true);

        unbinder = ButterKnife.bind(this, info);
        return info;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        minfoAdapter.unBinderAdapter.unbind();
    }

    class infoAdapter extends RecyclerView.Adapter<infoAdapter.ViewHolder> {
        private List<ImmediateInformation> immediateInformations;
        Unbinder unBinderAdapter;
        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.info_item_image) ImageView infoImage;
            @BindView(R.id.info_item_title) TextView infoTitle;
            @BindView(R.id.info_item_time) TextView infoTime;
            View infoView;
            ViewHolder(View itemView) {
                super(itemView);
                infoView=itemView;
                unBinderAdapter= ButterKnife.bind(this,itemView);
            }
        }

        infoAdapter(List<ImmediateInformation> ImmediateInformations) {
            this.immediateInformations = ImmediateInformations;
        }

        void replaceInfo(List<ImmediateInformation> immediateInformations){
            setImmediateInformations(immediateInformations);
            notifyDataSetChanged();
        }

        private void setImmediateInformations(List<ImmediateInformation> immediateInformations){
            this.immediateInformations=checkNotNull(immediateInformations);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.informations_item, viewGroup, false);

            final ViewHolder holder=new ViewHolder(view);
            holder.infoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=holder.getAdapterPosition();
                    ImmediateInformation immediateInformation=immediateInformations.get(position);
                    presenter.openInformationDetails(immediateInformation);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImmediateInformation ImmediateInformation = immediateInformations.get(position);
            holder.infoImage.setImageResource(getImageIdByType(ImmediateInformation.getType()));
            holder.infoTitle.setText(ImmediateInformation.getTitle());
            holder.infoTime.setText(ImmediateInformation.getTime());
        }

        @Override
        public int getItemCount() {
            return immediateInformations.size();
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
        showLoadingErrorMessage(getString(R.string.loading_info_error));
    }
    private void showLoadingErrorMessage(String message){
        View view=checkNotNull(getView());
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
}
