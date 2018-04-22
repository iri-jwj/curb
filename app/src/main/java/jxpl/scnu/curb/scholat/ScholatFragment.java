package jxpl.scnu.curb.scholat;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.ScrollChildSwipeRefreshLayout;
import jxpl.scnu.curb.utils.autoFitRecycler.AutoFitRecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author iri-jwj
 * @version 1
 */
public class ScholatFragment extends Fragment implements ScholatContract.View {
    @BindView(R.id.scholat_recycler)
    AutoFitRecyclerView m_ScholatRecycler;
    @BindView(R.id.scholat_refresh)
    ScrollChildSwipeRefreshLayout m_ScholatRefresh;
    @BindView(R.id.frameLayout)
    ConstraintLayout m_FrameLayout;
    Unbinder unbinder;
    private Activity m_activity;
    private ScholatContract.Presenter m_presenter;
    private ScholatAdapter m_scholatAdapter;


    public ScholatFragment() {
        // Required empty public constructor
    }


    public static ScholatFragment newInstance() {
        ScholatFragment fragment = new ScholatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scholat, container, false);
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager lc_linearLayoutManager = new LinearLayoutManager(m_activity);
        m_ScholatRecycler.setLayoutManager(lc_linearLayoutManager);
        m_scholatAdapter =
                new ScholatAdapter(new LinkedList<ScholatHomework>(), checkNotNull(getContext()), m_ScholatRecycler);
        m_ScholatRecycler.setAdapter(m_scholatAdapter);
        m_ScholatRecycler.addItemDecoration(new DividerItemDecoration(
                checkNotNull(getActivity()), DividerItemDecoration.HORIZONTAL));

        m_ScholatRefresh.setScrollUpChild(m_ScholatRecycler);
        m_ScholatRefresh.canChildScrollUp();
        m_ScholatRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                m_presenter.loadScholats(true);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_presenter.start();
    }

    @Override
    public void setPresenter(ScholatContract.Presenter presenter) {
        m_presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(checkNotNull(getView()), message, 1300).show();
    }

    @Override
    public void showScholats(List<ScholatHomework> para_homeworkList) {
        m_scholatAdapter.repalceHomeworks(para_homeworkList);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null)
            return;
        m_ScholatRefresh.post(new Runnable() {
            @Override
            public void run() {
                m_ScholatRefresh.setRefreshing(active);
            }
        });
    }

    @Override
    public void setActivity(Activity para_activity) {
        m_activity = checkNotNull(para_activity);
    }
}
