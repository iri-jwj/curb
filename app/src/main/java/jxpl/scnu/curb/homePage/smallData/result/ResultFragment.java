package jxpl.scnu.curb.homePage.smallData.result;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.homePage.smallData.SDResult;

import static com.google.common.base.Preconditions.checkNotNull;


public class ResultFragment extends Fragment implements ResultInterface.View {
    private static final String ARG_SD_ID = "summary_id";
    @BindView(R.id.result_recycler)
    RecyclerView m_ResultRecycler;
    Unbinder unbinder;
    private ResultInterface.Presenter m_presenter;
    private ResultAdapter m_resultAdapter;
    private UUID summaryId;

    public ResultFragment() {
        // Required empty public constructor
    }


    public static ResultFragment newInstance(UUID summaryId) {
        Bundle lc_bundle = new Bundle();
        lc_bundle.putString(ARG_SD_ID, summaryId.toString());
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(lc_bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summaryId = UUID.fromString(getArguments().getString(ARG_SD_ID));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        unbinder = ButterKnife.bind(this, view);
        m_resultAdapter = new ResultAdapter(new LinkedList<SDResult>());
        LinearLayoutManager lc_layoutManager = new LinearLayoutManager(getContext());
        m_ResultRecycler.setLayoutManager(lc_layoutManager);
        m_ResultRecycler.setAdapter(m_resultAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_presenter.start();
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


    class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
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
        public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View lc_view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_result, parent, false);
            ResultViewHolder lc_viewHolder = new ResultViewHolder(lc_view);
            return lc_viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
            List<PieEntry> lc_pieEntry = new LinkedList<>();

            lc_pieEntry.add(new PieEntry(m_resultList.get(position).getOption1ans(), m_resultList.get(position).getOption1()));
            lc_pieEntry.add(new PieEntry(m_resultList.get(position).getOption2ans(), m_resultList.get(position).getOption2()));
            PieDataSet lc_pieDataSet = new PieDataSet(lc_pieEntry,
                    "Question:" + m_resultList.get(position).getQuestion());

            List<Integer> lc_colors = new LinkedList<>();
            lc_colors.add(Color.parseColor("#f9d07e"));
            lc_colors.add(Color.parseColor("#92b6dc"));
            lc_pieDataSet.setColors(lc_colors);

            lc_pieDataSet.setValueTextColor(Color.BLACK);
            lc_pieDataSet.setSliceSpace(2);

            PieData lc_pieData = new PieData(lc_pieDataSet);
            lc_pieData.setValueFormatter(new PercentFormatter());
            lc_pieData.setValueTextColor(Color.BLACK);

            holder.m_ItemChart.getDescription().setEnabled(false);
            Legend lc_legend = holder.m_ItemChart.getLegend();
            lc_legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            lc_legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            lc_legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            lc_legend.setXEntrySpace(5.0f);
            lc_legend.setDrawInside(false);
            holder.m_ItemChart.setData(lc_pieData);
            holder.m_ItemChart.highlightValues(null);
            holder.m_ItemChart.setEntryLabelColor(Color.BLACK);
            holder.m_ItemChart.setMaxAngle(360);
            holder.m_ItemChart.setHoleRadius(0);
        }

        @Override
        public int getItemCount() {
            return m_resultList.size();
        }

        class ResultViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_chart)
            PieChart m_ItemChart;

            ResultViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
