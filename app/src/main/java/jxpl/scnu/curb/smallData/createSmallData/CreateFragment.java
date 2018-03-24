package jxpl.scnu.curb.smallData.createSmallData;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDSummaryCreate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment implements CreateInterface.View {
    @BindView(R.id.recycler_create)
    RecyclerView m_RecyclerCreate;
    @BindView(R.id.fragment_create)
    ConstraintLayout m_FragmentCreate;
    Unbinder unbinder;
    private CreateInterface.Presenter m_presenter;
    private SummariesAdapter m_summariesAdapter;
    private DetailsAdapter m_detailsAdapter;

    public CreateFragment() {
        // Required empty public constructor
    }


    @NonNull
    public static CreateFragment newInstance() {

        return new CreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_summariesAdapter = new SummariesAdapter(new LinkedList<SDSummaryCreate>());
        m_detailsAdapter = new DetailsAdapter();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        unbinder = ButterKnife.bind(this, view);

        //设置recyclerView
        LinearLayoutManager lc_layoutManager = new LinearLayoutManager(this.getContext());
        m_RecyclerCreate.setLayoutManager(lc_layoutManager);

        m_RecyclerCreate.setAdapter(m_summariesAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        Log.d("InfoFrag", "onViewCreated: ");
        super.onViewCreated(view, bundle);
        m_presenter.loadCreatedSummaries();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_sd, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(Object presenter) {
        m_presenter = (CreateInterface.Presenter) presenter;
    }

    @Override
    public void showCreateSummaries(List<SDSummaryCreate> para_createList) {
        m_summariesAdapter.setSdSummaryCreates(checkNotNull(para_createList));
    }

    @Override
    public void showCreateDetails(List<SDDetail> para_details) {
        m_detailsAdapter.setDetailList(checkNotNull(para_details));
    }

    @Override
    public void showError(String message) {
        View view = checkNotNull(getView());
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //内部类 recyclerView 的 SummariesAdapter
    protected class SummariesAdapter extends RecyclerView.Adapter<SummariesAdapter.ViewHolder> {

        private List<SDSummaryCreate> m_sdSummaryCreates = new ArrayList<>();

        SummariesAdapter(List<SDSummaryCreate> para_createList) {
            m_sdSummaryCreates = para_createList;
        }

        void setSdSummaryCreates(List<SDSummaryCreate> para_createList) {
            m_sdSummaryCreates = para_createList;
            checkNotNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View lc_view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_create_small_date, parent, false);

            final ViewHolder lc_viewHolder = new ViewHolder(lc_view);
            lc_viewHolder.m_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 添加点击事件
                }
            });
            return lc_viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(CreateFragment.this)
                    .asBitmap()
                    .load(m_sdSummaryCreates.get(position).getImg_url())
                    .into(holder.m_ImageView);
            holder.m_ListTitle.setText(m_sdSummaryCreates.get(position).getTitle());
            holder.m_ListCreateTime.setText(m_sdSummaryCreates.get(position).getCreate_time());

        }

        @Override
        public int getItemCount() {
            return m_sdSummaryCreates.size();
        }

        //ViewHolder 内部类
        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.imageView)
            ImageView m_ImageView;
            @BindView(R.id.list_title)
            TextView m_ListTitle;
            @BindView(R.id.list_create_time)
            TextView m_ListCreateTime;

            View m_view;

            ViewHolder(View itemView) {
                super(itemView);
                m_view = itemView;
                ButterKnife.bind(this, itemView);
            }
        }
    }

    protected class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

        private List<SDDetail> m_detailList = new LinkedList<>();

        void setDetailList(List<SDDetail> para_details) {
            m_detailList = para_details;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View lc_view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_create_details, parent, false);
            return new ViewHolder(lc_view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.m_QuestionTitle.setText(m_detailList.get(position).getQuestion());
            holder.m_QuestionOne.setText(m_detailList.get(position).getOptionOne());
            holder.m_QuestionTwo.setText(m_detailList.get(position).getOptionTwo());
        }

        @Override
        public int getItemCount() {
            return m_detailList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.question_title)
            TextView m_QuestionTitle;
            @BindView(R.id.question_one)
            EditText m_QuestionOne;
            @BindView(R.id.question_two)
            EditText m_QuestionTwo;

            View m_view;

            ViewHolder(View itemView) {
                super(itemView);
                m_view = itemView;
            }
        }
    }
}
