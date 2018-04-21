package jxpl.scnu.curb.data.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.List;

import jxpl.scnu.curb.data.repository.SmallDataDataSource;
import jxpl.scnu.curb.data.retrofit.RetrofitGetData;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDResult;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;


public class SDRemoteDataSource implements SmallDataDataSource {

    private static SDRemoteDataSource stc_sdRemoteDataSource;

    private SDRemoteDataSource() {
    }

    public static SDRemoteDataSource getInstance() {
        if (stc_sdRemoteDataSource == null)
            stc_sdRemoteDataSource = new SDRemoteDataSource();
        return stc_sdRemoteDataSource;
    }

    @Override
    public void loadSummaries(@NonNull loadSummaryCallback callback, String time, int direction) {
        List<SDSummary> lc_sdSummaries = RetrofitGetData.getSmallDataSummary(time, direction);

        if (lc_sdSummaries == null || lc_sdSummaries.isEmpty())
            callback.onDataNotAvailable();
        else
            callback.onSummaryLoaded(lc_sdSummaries);
    }

    @Override
    public void saveSummariesToLocal(List<SDSummary> sdSummaries) {

    }

    @Override
    public void getSummary(@NonNull getSummaryCallback callback, String id) {

    }

    @Override
    public void loadDetails(@NonNull loadDetailCallback callback, String summaryId) {
        List<SDDetail> lc_sdDetails = RetrofitGetData.postSmallDataDetail(summaryId);
        if (lc_sdDetails == null || lc_sdDetails.isEmpty()) {
            callback.onDataNotAvailable();
        } else
            callback.onDetailLoaded(lc_sdDetails);
    }

    @Override
    public void getCreatedSummary(@NonNull getCreatedSummaryCallback para_getCreatedSummaryCallback,
                                  String id) {

    }

    @Override
    public void loadCreatedSummaries(@NonNull loadCreatedSummariesCallback para_loadCreatedSummariesCallback) {
        List<SDSummaryCreate> lc_sdSummaryCreates = RetrofitGetData.getCreatedSummaries();
        if (lc_sdSummaryCreates != null && !lc_sdSummaryCreates.isEmpty())
            para_loadCreatedSummariesCallback.onCreatedSummariesLoaded(lc_sdSummaryCreates);
        else
            para_loadCreatedSummariesCallback.onDataNotAvailable();
    }

    @Override
    public void loadCreatedDetails(@NonNull loadCreatedDetailsCallback para_loadCreatedDetailsCallback,
                                   String para_summaryId) {
        List<SDDetail> lc_sdDetails = RetrofitGetData.getCreatedDetails(para_summaryId);
        if (lc_sdDetails != null && !lc_sdDetails.isEmpty())
            para_loadCreatedDetailsCallback.onCreatedDetailsLoaded(lc_sdDetails);
        else
            para_loadCreatedDetailsCallback.onDataNotAvailable();
    }


    @Override
    public void loadAnswers(@NonNull loadAnswersCallback para_loadAnswersCallback, String summaryId) {
        List<SDAnswer> lc_sdAnswers = RetrofitGetData.getAnswers(summaryId);
        if (lc_sdAnswers != null && !lc_sdAnswers.isEmpty())
            para_loadAnswersCallback.onAnswerLoaded(lc_sdAnswers);
        else
            para_loadAnswersCallback.onDataNotAvailable();
    }

    @Override
    public void saveCreatedSDToRemote(String para_s, File image) {
        String result = RetrofitGetData.postCreatedSD(para_s, image);
    }

    @Override
    public void loadResult(loadResultCallback para_loadResultCallback,
                           String summaryId) {
        List<SDResult> lc_resultList = RetrofitGetData.getSDResult(summaryId);
        if (lc_resultList == null || lc_resultList.isEmpty())
            para_loadResultCallback.onDataNotAvailable();
        else
            para_loadResultCallback.onResultsLoaded(lc_resultList);
    }

    @Override
    public void commitAnswer(String strEntity) {
        RetrofitGetData.postAnswer(strEntity);
    }

    @Override
    public void markAnswered(String summaryId) {

    }

    @Override
    public void refreshSummaries() {

    }

    @Override
    public void refreshCreatedSummaries() {

    }

    @Override
    public void saveAnswersToLocal(List<SDAnswer> para_sdAnswers) {

    }

    @Override
    public void saveDetailsToLocal(List<SDDetail> para_sdDetails) {

    }

    @Override
    public void saveCreatedSDToLocal(SDSummaryCreate para_sdSummaryCreate, List<SDDetail> para_sdDetails) {

    }
}
