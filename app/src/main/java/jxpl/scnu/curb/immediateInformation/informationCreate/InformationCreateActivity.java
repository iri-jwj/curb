package jxpl.scnu.curb.immediateInformation.informationCreate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.InformationLocalDataSource;
import jxpl.scnu.curb.data.remote.InformationRemoteDataSource;
import jxpl.scnu.curb.data.repository.InformationRepository;
import jxpl.scnu.curb.utils.ActivityUtil;

/**
 * @author iri-jwj
 * @version 1
 */
public class InformationCreateActivity extends AppCompatActivity {

    public static String INFO_ID = "info_id";
    @BindView(R.id.toolbar_title)
    TextView m_ToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar m_Toolbar;
    @BindView(R.id.content_frame)
    FrameLayout m_ContentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_create);
        ButterKnife.bind(this);
        m_ToolbarTitle.setText("新建资讯");

        ActivityUtil.setFragmentManagerNotHome(getSupportFragmentManager());
        ActivityUtil.setContainerViewNotHome(R.id.content_frame);

        InformationCreateFragment lc_informationCreateFragment =
                InformationCreateFragment.newInstance();
        ActivityUtil.addFragmentNotInHomePage(lc_informationCreateFragment);

        new InformationCreatePresenter(
                InformationRepository.getInstance(InformationLocalDataSource.getInstance(this),
                        InformationRemoteDataSource.getInstance(), this)
                , lc_informationCreateFragment, this);
        ActivityUtil.setCurrentFragmentNotInHome();

    }

    @Override
    protected void onDestroy() {
        ActivityUtil.removeFragmentNotHome();
        super.onDestroy();
    }

}
