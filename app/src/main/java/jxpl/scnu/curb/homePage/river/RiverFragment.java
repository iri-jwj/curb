package jxpl.scnu.curb.homePage.river;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jxpl.scnu.curb.R;

/**
 * @author iri-jwj
 * @version 1 init
 */
public class RiverFragment extends Fragment implements RiverContract.View {


    public RiverFragment() {
        // Required empty public constructor
    }


    public static RiverFragment newInstance() {
        RiverFragment fragment = new RiverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(RiverContract.Presenter presenter) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_river, container, false);
    }

}
