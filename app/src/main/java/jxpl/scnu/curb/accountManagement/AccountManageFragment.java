package jxpl.scnu.curb.accountManagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jxpl.scnu.curb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountManageFragment extends Fragment {


    public AccountManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_manage, container, false);
    }

}
