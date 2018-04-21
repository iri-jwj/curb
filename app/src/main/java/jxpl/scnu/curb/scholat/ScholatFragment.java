package jxpl.scnu.curb.scholat;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    @BindView(R.id.scholat_loading_indicator)
    ProgressBar m_ScholatLoadingIndicator;

    private Activity m_activity;
    private ScholatContract.Presenter m_presenter;
    private ScholatAdapter m_scholatAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public ScholatFragment() {
        // Required empty public constructor
    }


    public static ScholatFragment newInstance(String param1, String param2) {
        ScholatFragment fragment = new ScholatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_scholatAdapter =
                new ScholatAdapter(new LinkedList<ScholatHomework>(), checkNotNull(getContext()), m_ScholatRecycler);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scholat, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        Snackbar.make(checkNotNull(getView()), message, 1300);
    }

    @Override
    public void showScholats(List<ScholatHomework> para_homeworkList) {
        m_scholatAdapter.repalceHomeworks(para_homeworkList);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            m_activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            m_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_ScholatLoadingIndicator.setVisibility(View.VISIBLE);
                }
            });
        } else {
            m_activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            m_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_ScholatLoadingIndicator.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void setActivity(Activity para_activity) {
        m_activity = checkNotNull(para_activity);
    }
}
