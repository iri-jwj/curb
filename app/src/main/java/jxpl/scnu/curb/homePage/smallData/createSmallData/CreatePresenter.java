package jxpl.scnu.curb.homePage.smallData.createSmallData;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import jxpl.scnu.curb.R;
import jxpl.scnu.curb.data.repository.SmallDataDataSource;
import jxpl.scnu.curb.data.repository.SmallDataRepository;
import jxpl.scnu.curb.homePage.smallData.SDDetail;
import jxpl.scnu.curb.homePage.smallData.SDSummary;
import jxpl.scnu.curb.homePage.smallData.SDSummaryCreate;


public class CreatePresenter implements CreateInterface.Presenter {
    private final CreateInterface.View m_createView;
    private final SmallDataRepository m_smallDataRepository;
    private final Context m_createContext;
    private List<SDDetail> m_sdCreatedDetails = new LinkedList<>();
    private SDSummary m_sdCreatedSummary;
    private File m_imageFile;

    public CreatePresenter(SmallDataRepository para_smallDataRepository,
                           CreateInterface.View para_view,
                           Context para_context) {
        m_smallDataRepository = para_smallDataRepository;
        m_createView = para_view;
        m_createContext = para_context;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCreatedSummaries() {

        m_smallDataRepository.loadCreatedSummaries(new SmallDataDataSource
                .loadCreatedSummariesCallback() {
            @Override
            public void onCreatedSummariesLoaded(List<SDSummaryCreate> sdSummaries) {
                //todo 添加逻辑
            }

            @Override
            public void onDataNotAvailable() {
                sendErrorToView(R.string.sd_loading_created_summaries_error);
            }
        }, m_createContext);
    }

    @Override
    public void loadCreatedDetails(String summaryId) {

        m_smallDataRepository.loadCreatedDetails(new SmallDataDataSource.loadCreatedDetailsCallback() {
            @Override
            public void onCreatedDetailsLoaded(List<SDDetail> para_sdDetails) {
                //todo 添加逻辑
            }

            @Override
            public void onDataNotAvailable() {
                sendErrorToView(R.string.sd_loading_created_details_error);
            }
        }, summaryId, m_createContext);
    }

    @Override
    public void saveCreatedSummary(SDSummaryCreate para_sdSummaryCreate, final File para_imageFile) {
        m_sdCreatedSummary = new SDSummary(para_sdSummaryCreate.getId(),
                para_sdSummaryCreate.getTitle(),
                ">", para_sdSummaryCreate.getCreate_time(),
                para_sdSummaryCreate.getImg_url(),
                para_sdSummaryCreate.getDescription(),
                false);
        m_imageFile = para_imageFile;
    }

    @Override
    public void saveCreatedDetail(SDDetail para_sdDetail) {
        m_sdCreatedDetails.add(para_sdDetail);
    }

    @Override
    public void postCreatedSummary() {
        Gson lc_gson = new Gson();
        String lc_s = lc_gson.toJson(new CreatePresenter.CreatedSD(m_sdCreatedSummary, m_sdCreatedDetails));
        m_smallDataRepository.saveCreatedSDToRemote(lc_s, m_imageFile, m_createContext);
    }

    private void sendErrorToView(int messageId) {
        m_createView.showError(m_createContext
                .getResources()
                .getString(messageId));
    }

    private class CreatedSD {
        private SDSummary m_sdSummaryCreate;
        private List<SDDetail> m_sdDetails;

        CreatedSD(SDSummary para_sdSummaryCreate, List<SDDetail> para_sdDetails) {
            m_sdSummaryCreate = para_sdSummaryCreate;
            m_sdDetails = para_sdDetails;
        }

        public SDSummary getSdSummaryCreate() {
            return m_sdSummaryCreate;
        }

        public List<SDDetail> getSdDetails() {
            return m_sdDetails;
        }
    }
}
 