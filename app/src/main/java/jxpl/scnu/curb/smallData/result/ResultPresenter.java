package jxpl.scnu.curb.smallData.result;

import android.content.Context;

import java.util.List;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.repository.SmallDataDataSource;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.smallData.SDResult;


public class ResultPresenter implements ResultInterface.Presenter {
    private final SmallDataRepository m_smallDataRepository;
    private final ResultInterface.View m_view;
    private final Context m_context;

    public ResultPresenter(SmallDataRepository para_smallDataRepository,
                           ResultInterface.View para_view,
                           Context para_context) {
        m_smallDataRepository = para_smallDataRepository;
        m_view = para_view;
        m_context = para_context;
        para_view.setPresenter(this);
    }

    @Override
    public void start() {
        loadResult(m_view.getSummaryID().toString());
    }

    /**
     * 这里获取测试的最终结果
     */
    @Override
    public void loadResult(String summary_id) {
        m_smallDataRepository.loadResult(new SmallDataDataSource.loadResultCallback() {
            @Override
            public void onResultsLoaded(List<SDResult> para_sdResults) {
                m_view.showResults(para_sdResults);
            }

            @Override
            public void onDataNotAvailable() {
                sendErrorToView(R.string.sd_loading_results_error);
            }
        }, summary_id, m_context);
    }

    private void sendErrorToView(int messageId) {
        m_view.showError(m_context
                .getResources()
                .getString(messageId));
    }

    @Override
    public Context getPresenterContext() {
        return m_context;
    }
}
