package jxpl.scnu.curb.immediateInformation.informationDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.utils.ActivityUtil;

public class InformationDetailActivity extends AppCompatActivity {

    public static String INFO_ID="info_id";

    @BindView(R.id.detail_info_toolbar)
    Toolbar detailInfoToolbar;
    @BindView(R.id.detail_info_frame)
    FrameLayout detailInfoFrame;
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);
        unbinder=ButterKnife.bind(this);

        String infoId= getIntent().getStringExtra(INFO_ID);

        InformationDetailFragment informationDetailFragment=(InformationDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.detail_info_frame);

        if (informationDetailFragment==null){
            informationDetailFragment=InformationDetailFragment.newInstance(infoId);
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),informationDetailFragment,R.id.detail_info_frame);
        }
        InformationDetailPresenter informationDetailPresenter=new InformationDetailPresenter(infoId,
                InformationRepository.getInstance(InformationLocalDataSource.getInstace(this), InformationRemoteDataSource.getInstance())
                ,informationDetailFragment);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbinder.unbind();
    }
    
}
