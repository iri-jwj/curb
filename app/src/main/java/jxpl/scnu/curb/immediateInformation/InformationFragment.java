package jxpl.scnu.curb.immediateInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.immediateInformation.informationDetails.InformationDetailActivity;
import jxpl.scnu.curb.utils.ScrollChildSwipeRefreshLayout;

import static com.google.common.base.Preconditions.checkNotNull;

public class InformationFragment extends Fragment implements InformationContract.View {

    @BindView(R.id.info_recycler)
    RecyclerView infoRecycler;
    @BindView(R.id.info_floatingAb)
    FloatingActionButton infoFloatingAb;
    @BindView(R.id.refresh_info_layout)
    ScrollChildSwipeRefreshLayout refreshInfoLayout;
    @BindView(R.id.Label)
    TextView Label;
    @BindView(R.id.info_list)
    LinearLayout infoList;
    Unbinder unbinder;

    private InformationContract.Presenter presenter;
    private infoAdapter minfoAdapter;



    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance() {
        return  new InformationFragment();
    }

    @Override
    public void setPresenter(@NonNull InformationContract.Presenter mPresenter) {
        presenter = checkNotNull(mPresenter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        minfoAdapter = new infoAdapter(new ArrayList<ImmediateInformation>(0));
        Log.d("CreateInfoView","CreateAdapter");
    }


    @Override
    public void showInfo(List<ImmediateInformation> immediateInformations) {
        Log.d("informationFragment", "showInfo: " + immediateInformations.isEmpty());
        minfoAdapter.replaceInfo(immediateInformations);
        Log.d("GetList","SetAdapter");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View info = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, info);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        infoRecycler.setLayoutManager(layoutManager);
        infoRecycler.setAdapter(minfoAdapter);

        infoRecycler.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));

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

        //SET UP floatingActionBar
        infoFloatingAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilteringPopUpMenu(getActivity());
            }
        });
        unbinder = ButterKnife.bind(this, info);
        return info;
    }

    @Override
    public void showNoInfo() {
        Log.d("informationFragment", "showNoInfo: ");
        infoList.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view,Bundle bundle){
        presenter.loadInformation(true);
        Log.d("InfoFrag", "onViewCreated: ");
        super.onViewCreated(view,bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class infoAdapter extends RecyclerView.Adapter<infoAdapter.ViewHolder> {
        private List<ImmediateInformation> immediateInformations;
        Unbinder unBinderAdapter;

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.info_item_image)
            ImageView infoImage;
            @BindView(R.id.info_item_title)
            TextView infoTitle;
            @BindView(R.id.info_item_time)
            TextView infoTime;
            View infoView;

            ViewHolder(View itemView) {
                super(itemView);
                infoView = itemView;
                unBinderAdapter = ButterKnife.bind(this, itemView);
            }

        }

        infoAdapter(List<ImmediateInformation> immediateInformations) {
            this.immediateInformations=immediateInformations;
        }

        void replaceInfo(List<ImmediateInformation> immediateInformations) {
            for (ImmediateInformation i :
                    immediateInformations) {
                if (this.immediateInformations.contains(i))
                    immediateInformations.remove(i);
            }
            if (!immediateInformations.isEmpty()) {
                immediateInformations.addAll(this.immediateInformations);
                setImmediateInformations(immediateInformations);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            } else
                Snackbar.make(checkNotNull(getView()), "没有新的信息", Snackbar.LENGTH_LONG).show();
        }

        private void setImmediateInformations(List<ImmediateInformation> immediateInformations) {
            this.immediateInformations = checkNotNull(immediateInformations);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_information, viewGroup, false);

            final ViewHolder holder = new ViewHolder(view);
            holder.infoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    ImmediateInformation immediateInformation = immediateInformations.get(position);
                    presenter.openInformationDetails(immediateInformation, getActivity());
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImmediateInformation ImmediateInformation = immediateInformations.get(position);
            //holder.infoImage.setImageResource(getImageIdByType(ImmediateInformation.getType()));
            holder.infoImage.setImageResource(R.drawable.ic_item_edu_info);
            holder.infoTitle.setText(ImmediateInformation.getTitle());
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
                        presenter.setFiltering(InformationFilter.EDU_INFORMATIONS.toString());
                        break;
                    case R.id.info_scholar:
                        presenter.setFiltering(InformationFilter.SCHOLAR_INFORMATIONS.toString());
                        break;
                    case R.id.info_work_study:
                        presenter.setFiltering(InformationFilter.WORK_STUDY_INFORMATIONS.toString());
                        break;
                    case R.id.info_practice:
                        presenter.setFiltering(InformationFilter.PRA_INFORMATIONS.toString());
                        break;
                    default:
                        presenter.setFiltering(InformationFilter.ALL_INFORMATIONS.toString());
                        break;
                }
                return true;
            }
        });
        popup.show();
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
    public void showInformationDetailsUi(String id, Context context) {
        Intent intent = new Intent(context, InformationDetailActivity.class);
        intent.putExtra(InformationDetailActivity.INFO_ID, id);
        startActivity(intent);
    }

    @Override
    public void showLoadingError() {
        showLoadingErrorMessage(getString(R.string.loading_info_error));
    }

    private void showLoadingErrorMessage(String message) {
        View view = checkNotNull(getView());
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
