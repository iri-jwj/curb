package jxpl.scnu.curb.data.repository;


import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDResult;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;

public interface SmallDataDataSource {
    void getSummary(@NonNull getSummaryCallback callback, String id);

    void loadSummaries(@NonNull loadSummaryCallback callback, String time, int direction, Context para_context);

    void loadDetails(@NonNull loadDetailCallback callback, String summaryId, Context para_context);

    void loadAnswers(@NonNull loadAnswersCallback para_loadAnswersCallback, String summaryId, Context para_context);

    void getCreatedSummary(@NonNull getCreatedSummaryCallback para_getCreatedSummaryCallback
            , String id);

    void loadCreatedSummaries(@NonNull loadCreatedSummariesCallback para_loadCreatedSummariesCallback, Context para_context);

    void loadCreatedDetails(@NonNull loadCreatedDetailsCallback para_loadCreatedDetailsCallback,
                            String para_summaryId, Context para_context);

    void refreshSummaries();

    void refreshCreatedSummaries();

    void saveSummariesToLocal(List<SDSummary> sdSummaries);

    void saveDetailsToLocal(List<SDDetail> para_sdDetails);

    void saveCreatedSDToLocal(SDSummaryCreate para_sdSummaryCreate
            , List<SDDetail> para_sdDetails) throws Exception;

    void saveCreatedSDToRemote(String para_s, final File image, Context para_context);

    void saveAnswersToLocal(List<SDAnswer> para_sdAnswers);

    void markAnswered(String summaryId);

    void commitAnswer(String strEntity, Context para_context);

    void loadResult(loadResultCallback para_loadResultCallback,
                    String summaryId, Context para_context);

    interface getSummaryCallback {
        void onSummaryGot(SDSummary para_sdSummary);

        void onDataNotAvailable();
    }

    interface loadSummaryCallback {
        void onSummaryLoaded(List<SDSummary> sdSummaries);

        void onDataNotAvailable();
    }

    interface loadDetailCallback {
        void onDetailLoaded(List<SDDetail> para_sdDetails);

        void onDataNotAvailable();
    }

    interface getCreatedSummaryCallback {
        void onCreatedSummaryGot(SDSummaryCreate para_sdSummaryCreates);

        void onDataNotAvailable();
    }

    interface loadCreatedSummariesCallback {
        void onCreatedSummariesLoaded(List<SDSummaryCreate> sdSummaries);

        void onDataNotAvailable();
    }

    interface loadCreatedDetailsCallback {
        void onCreatedDetailsLoaded(List<SDDetail> para_sdDetails);

        void onDataNotAvailable();
    }

    interface loadAnswersCallback {
        void onAnswerLoaded(List<SDAnswer> para_sdAnswers);

        void onDataNotAvailable();
    }

    interface loadResultCallback {
        void onResultsLoaded(List<SDResult> para_sdResults);

        void onDataNotAvailable();
    }
}
