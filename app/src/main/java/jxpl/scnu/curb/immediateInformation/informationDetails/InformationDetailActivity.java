package jxpl.scnu.curb.immediateInformation.informationDetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.utils.ActivityUtil;

public class InformationDetailActivity extends AppCompatActivity {

    public static String INFO_ID = "info_id";

    @BindView(R.id.detail_info_toolbar)
    Toolbar detailInfoToolbar;
    Unbinder unbinder;
    @BindView(R.id.info_detail_title)
    TextView infoDetailTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_detail);
        unbinder = ButterKnife.bind(this);

        String infoId = getIntent().getStringExtra(INFO_ID);
        ActivityUtil.setFragmentManagerNotHome(getSupportFragmentManager());
        ActivityUtil.setContainerViewNotHome(R.id.detail_info_frame);

        InformationDetailFragment informationDetailFragment =
                InformationDetailFragment.newInstance(infoId);
        ActivityUtil.addFragmentNotInHomePage(informationDetailFragment);

        InformationDetailPresenter informationDetailPresenter = new InformationDetailPresenter(infoId,
                InformationRepository.getInstance(InformationLocalDataSource.getInstace(this),
                        InformationRemoteDataSource.getInstance())
                , informationDetailFragment);
        ActivityUtil.setCurrentFragmentNotInHome();
        if (informationDetailPresenter.getInfoTitle().isEmpty()) {
            infoDetailTitle.setText("");
        } else
            infoDetailTitle.setText(informationDetailPresenter.getInfoTitle());
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.removeFragmentNotHome();
        unbinder.unbind();
        super.onDestroy();
    }

}
