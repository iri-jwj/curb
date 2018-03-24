package jxpl.scnu.curb.smallData.result;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.local.SmallDataLocalDataSource;
import jxpl.scnu.curb.data.remote.SDRemoteDataSource;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.utils.ActivityUtil;

public class ResultActivity extends AppCompatActivity {
    private static final String ARG_SD_ID = "summary_id";
    private ResultFragment m_resultFragment = null;
    private ResultPresenter m_resultPresenter = null;
    private UUID m_uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent lc_intent = new Intent();
        m_uuid = (UUID) lc_intent.getSerializableExtra(ARG_SD_ID);

        ActivityUtil.setContainerViewNotHome(R.id.content_frame);
        ActivityUtil.setFragmentManagerNotHome(getSupportFragmentManager());

        m_resultFragment = ResultFragment.newInstance(m_uuid);
        ActivityUtil.addFragmentNotInHomePage(m_resultFragment);
        m_resultPresenter = new ResultPresenter(SmallDataRepository
                .getInstance(SDRemoteDataSource.getInstance(),
                        SmallDataLocalDataSource.getInstance(ResultActivity.this)),
                m_resultFragment,
                this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_SD_ID, m_resultFragment.getSummaryID());
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
        m_resultPresenter.loadResult(m_uuid.toString());
    }
}
