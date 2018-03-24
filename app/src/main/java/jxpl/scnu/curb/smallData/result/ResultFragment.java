package jxpl.scnu.curb.smallData.result;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDResult;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment implements ResultInterface.View {
    private static final String ARG_SD_ID = "summary_id";
    @BindView(R.id.result_recycler)
    RecyclerView m_ResultRecycler;

    private ResultInterface.Presenter m_presenter;
    private ResultAdapter m_resultAdapter;
    private UUID summaryId;
    Unbinder unbinder;

    public ResultFragment() {
        // Required empty public constructor
    }


    public static ResultFragment newInstance(UUID summaryId) {
        Bundle lc_bundle = new Bundle();
        lc_bundle.putSerializable(ARG_SD_ID, summaryId);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(lc_bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summaryId = (UUID) getArguments().getSerializable(ARG_SD_ID);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        m_resultAdapter = new ResultAdapter(new LinkedList<SDResult>());
        LinearLayoutManager lc_layoutManager = new LinearLayoutManager(m_presenter.getPresenterContext());
        m_ResultRecycler.setLayoutManager(lc_layoutManager);
        m_ResultRecycler.setAdapter(m_resultAdapter);
        m_presenter.start();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void setPresenter(ResultInterface.Presenter presenter) {
        m_presenter = presenter;
    }

    @Override
    public void showError(String error) {
        Snackbar.make(checkNotNull(getView()), error, 1500);
    }

    @Override
    public void showResults(List<SDResult> para_sdResults) {
        m_resultAdapter.setResultList(para_sdResults);
    }

    @Override
    public UUID getSummaryID() {
        return summaryId;
    }


    class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
        List<SDResult> m_resultList;

        ResultAdapter(List<SDResult> para_resultList) {
            m_resultList = para_resultList;
        }

        void setResultList(List<SDResult> para_resultList) {
            m_resultList = para_resultList;
            checkNotNull(getActivity()).runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }
            );
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View lc_view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_result, parent, false);
            return new ViewHolder(lc_view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            List<PieEntry> lc_pieEntry = new LinkedList<>();
            List<Integer> lc_colors = new LinkedList<>();
            lc_colors.add(R.color.small_data_result1);
            lc_colors.add(R.color.small_data_result2);
            lc_pieEntry.add(new PieEntry(1, m_resultList.get(position).getOption1ans()));
            lc_pieEntry.add(new PieEntry(2, m_resultList.get(position).getOption2ans()));
            PieDataSet lc_pieDataSet = new PieDataSet(lc_pieEntry,
                    m_resultList.get(position).getQuestion());
            lc_pieDataSet.setColors(lc_colors);
            lc_pieDataSet.setValueTextColors(lc_colors);
            lc_pieDataSet.setSliceSpace(2);
            PieData lc_pieData = new PieData(lc_pieDataSet);
            holder.m_ItemChart.setData(lc_pieData);
            holder.m_ItemChart.setMaxAngle(360);
            holder.m_ItemChart.setHoleRadius(0);
        }

        @Override
        public int getItemCount() {
            return m_resultList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_chart)
            PieChart m_ItemChart;

            ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
