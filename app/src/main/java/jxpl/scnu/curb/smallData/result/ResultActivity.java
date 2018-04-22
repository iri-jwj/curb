package jxpl.scnu.curb.smallData.result;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.SmallDataLocalDataSource;
import jxpl.scnu.curb.data.remote.SDRemoteDataSource;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.utils.ActivityUtil;

public class ResultActivity extends AppCompatActivity {
    private static final String ARG_SD_ID = "summary_id";
    @BindView(R.id.toolbar_title)
    TextView m_ToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar m_Toolbar;
    @BindView(R.id.content_frame)
    FrameLayout m_ContentFrame;

    private ResultFragment m_resultFragment = null;
    private ResultPresenter m_resultPresenter = null;
    private UUID m_uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        m_uuid = (UUID) getIntent().getSerializableExtra(ARG_SD_ID);

        setSupportActionBar(m_Toolbar);
        m_ToolbarTitle.setText("问卷结果");
        ActivityUtil.setContainerViewNotHome(m_ContentFrame.getId());
        ActivityUtil.setFragmentManagerNotHome(getSupportFragmentManager());

        m_resultFragment = ResultFragment.newInstance(m_uuid);
        ActivityUtil.addFragmentNotInHomePage(m_resultFragment);
        m_resultPresenter = new ResultPresenter(SmallDataRepository
                .getInstance(SDRemoteDataSource.getInstance(),
                        SmallDataLocalDataSource.getInstance(ResultActivity.this), this),
                m_resultFragment,
                this);
        ActivityUtil.setCurrentFragmentNotInHome();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_SD_ID, m_resultFragment.getSummaryID().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        m_uuid = (UUID) savedInstanceState.getSerializable(ARG_SD_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //m_resultPresenter.loadResult(m_uuid.toString());
    }
}
