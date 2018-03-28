package jxpl.scnu.curb.scholat;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.utils.ScrollChildSwipeRefreshLayout;

/**
 * @author iri-jwj
 * @version 1
 */
public class ScholatFragment extends Fragment implements ScholatContract.View {
    @BindView(R.id.scholat_recycler)
    RecyclerView m_ScholatRecycler;
    @BindView(R.id.scholat_refresh)
    ScrollChildSwipeRefreshLayout m_ScholatRefresh;
    @BindView(R.id.frameLayout)
    ConstraintLayout m_FrameLayout;
    Unbinder unbinder;
    private ScholatContract.Presenter m_presenter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    }

    @Override
    public void showScholats(List<ScholatHomework> para_homeworkList) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setActivity(Activity para_activity) {

    }
}
