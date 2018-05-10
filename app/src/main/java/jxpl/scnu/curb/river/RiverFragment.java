package jxpl.scnu.curb.river;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jxpl.scnu.curb.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RiverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RiverFragment extends Fragment {

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
