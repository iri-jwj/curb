package jxpl.scnu.curb.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.data.repository.SmallDataDataSource;
import jxpl.scnu.curb.smallData.SDAnswer;
import jxpl.scnu.curb.smallData.SDDetail;
import jxpl.scnu.curb.smallData.SDSummary;
import jxpl.scnu.curb.smallData.SDSummaryCreate;

import static com.google.common.base.Preconditions.checkNotNull;


public class SmallDataLocalDataSource implements SmallDataDataSource {
    private CurbDbHelper m_curbDbHelper;
    private static SmallDataLocalDataSource stc_smallDataLocalDataSource;

    private SmallDataLocalDataSource(@NonNull Context para_context) {
        checkNotNull(para_context);
        m_curbDbHelper = new CurbDbHelper(para_context);
    }

    public static SmallDataLocalDataSource getInstance(@NonNull Context para_context) {
        if (stc_smallDataLocalDataSource == null) {
            stc_smallDataLocalDataSource = new SmallDataLocalDataSource(para_context);
        }
        return stc_smallDataLocalDataSource;
    }

    @Override
    public void getSummary(@NonNull getSummaryCallback callback, String id) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        SDSummary lc_sdSummary = null;
        String[] projection = {
                PersistenceContract.SDSummary.COLUMN_NAME_ID,
                PersistenceContract.SDSummary.COLUMN_NAME_TITLE,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATOR,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME,
                PersistenceContract.SDSummary.COLUMN_NAME_IMG,
                PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION,
                PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED
        };
        String selection = PersistenceContract.SDSummary.COLUMN_NAME_ID + " like ?";
        String[] selectionArgs = {id};
        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDSummary.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null);
        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_cursor.moveToNext();
            lc_sdSummary = new SDSummary(UUID.fromString(id),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummary.COLUMN_NAME_TITLE)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummary.COLUMN_NAME_CREATOR)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummary.COLUMN_NAME_IMG)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION)),
                    Boolean.parseBoolean(lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED)))
            );
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdSummary != null)
            callback.onSummaryGot(lc_sdSummary);
        else
            callback.onDataNotAvailable();

    }

    @Override
    public void loadDetails(@NonNull loadDetailCallback callback, String summaryId) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        List<SDDetail> lc_sdDetails = null;
        String[] projection = {
                PersistenceContract.SDDetail.COLUMN_NAME_SUMMARYID,
                PersistenceContract.SDDetail.COLUMN_NAME_NUM,
                PersistenceContract.SDDetail.COLUMN_NAME_QUESTION,
                PersistenceContract.SDDetail.COLUMN_NAME_OPTION1,
                PersistenceContract.SDDetail.COLUMN_NAME_OPTION2
        };
        String selection = PersistenceContract.SDDetail.COLUMN_NAME_SUMMARYID + " like ?";
        String[] selectionArgs = {summaryId};
        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDDetail.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null);
        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_sdDetails = new ArrayList<>();
            while (lc_cursor.moveToNext()) {
                lc_sdDetails.add(new SDDetail(UUID.fromString(summaryId),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDDetail.COLUMN_NAME_NUM)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDDetail.COLUMN_NAME_QUESTION)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDDetail.COLUMN_NAME_OPTION1)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDDetail.COLUMN_NAME_OPTION2))
                ));
            }
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdDetails != null)
            callback.onDetailLoaded(lc_sdDetails);
        else
            callback.onDataNotAvailable();
    }

   /* @Override
    public void loadSummaries(@NonNull loadSummaryCallback callback) {
        SQLiteDatabase lc_sqLiteDatabase=m_curbDbHelper.getReadableDatabase();
        List<SDSummary> lc_sdSummaries=null;
        String[] projection={
                PersistenceContract.SDSummary.COLUMN_NAME_ID,
                PersistenceContract.SDSummary.COLUMN_NAME_TITLE,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATOR,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME,
                PersistenceContract.SDSummary.COLUMN_NAME_IMG,
                PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION,
                PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED
        };
        String orderBy=PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME+" DESC";
        Cursor lc_cursor=lc_sqLiteDatabase.query(PersistenceContract.SDSummary.TABLE_NAME,
                projection,null,null,null,null,orderBy);
        if (lc_cursor!=null&&lc_cursor.getCount()>0){
            lc_sdSummaries=new ArrayList<>();
            int i=0;
            while (lc_cursor.moveToNext() && i < 5){
                lc_sdSummaries.add(new SDSummary(UUID.fromString(
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_ID))),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_TITLE)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_CREATOR)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_IMG)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION)),
                        Boolean.parseBoolean(lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED)))
                ));
                i++;
            }
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdSummaries!=null)
            callback.onSummaryLoaded(lc_sdSummaries);
        else
            callback.onDataNotAvailable();

    }*/

    @Override
    public void loadSummaries(@NonNull loadSummaryCallback callback, String time, int direction) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        List<SDSummary> lc_sdSummaries = null;
        String[] projection = {
                PersistenceContract.SDSummary.COLUMN_NAME_ID,
                PersistenceContract.SDSummary.COLUMN_NAME_TITLE,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATOR,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME,
                PersistenceContract.SDSummary.COLUMN_NAME_IMG,
                PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION,
                PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED
        };
        String orderBy = PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME + " DESC";
        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDSummary.TABLE_NAME,
                projection, null, null, null, null, orderBy);
        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_sdSummaries = new ArrayList<>();
            int i = 0;
            while (lc_cursor.moveToNext() && i < 5) {
                lc_sdSummaries.add(new SDSummary(UUID.fromString(
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_ID))),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_TITLE)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_CREATOR)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_IMG)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION)),
                        Boolean.parseBoolean(lc_cursor
                                .getString(lc_cursor.
                                        getColumnIndexOrThrow(PersistenceContract.SDSummary
                                                .COLUMN_NAME_HAVEFINISHED)))
                ));
                i++;
            }
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdSummaries != null)
            callback.onSummaryLoaded(lc_sdSummaries);
        else
            callback.onDataNotAvailable();
    }

    /**
     * 注意，此处的获取的图片链接很可能失效，即被用户删除
     * 因而将创建的问卷发送给服务器之后，需要将url改为服务器上的url
     *
     * @param para_getCreatedSummaryCallback 回调参数
     */
    @Override
    public void getCreatedSummary(@NonNull getCreatedSummaryCallback para_getCreatedSummaryCallback
            , String id) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        SDSummaryCreate lc_sdSummaryCreate = null;
        String[] projection = {
                PersistenceContract.SDSummaryCreate.COLUMN_NAME_ID,
                PersistenceContract.SDSummaryCreate.COLUMN_NAME_TITLE,
                PersistenceContract.SDSummaryCreate.COLUMN_NAME_CREATE_TIME,
                PersistenceContract.SDSummaryCreate.COLUMN_NAME_IMGURL,
                PersistenceContract.SDSummaryCreate.COLUMN_NAME_DESCRIPTION,
        };
        String orderBy = PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME + " DESC";
        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDSummaryCreate.TABLE_NAME,
                projection, null, null, null, null, orderBy);
        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_sdSummaryCreate = new SDSummaryCreate(UUID.fromString(
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummaryCreate.COLUMN_NAME_ID))),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummaryCreate.COLUMN_NAME_TITLE)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummaryCreate.COLUMN_NAME_CREATE_TIME)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummaryCreate.COLUMN_NAME_IMGURL)),
                    lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                            PersistenceContract.SDSummaryCreate.COLUMN_NAME_DESCRIPTION))
            );
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdSummaryCreate != null)
            para_getCreatedSummaryCallback.onCreatedSummaryGot(lc_sdSummaryCreate);
        else
            para_getCreatedSummaryCallback.onDataNotAvailable();
    }

    @Override
    public void loadCreatedSummaries(@NonNull loadCreatedSummariesCallback
                                             para_loadCreatedSummariesCallback) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        List<SDSummaryCreate> lc_sdSummaryCreates = null;
        String[] projection = {
                PersistenceContract.SDSummary.COLUMN_NAME_ID,
                PersistenceContract.SDSummary.COLUMN_NAME_TITLE,
                PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME,
                PersistenceContract.SDSummary.COLUMN_NAME_IMG,
                PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION,
        };
        String orderBy = PersistenceContract.SDSummaryCreate.COLUMN_NAME_CREATE_TIME + " DESC";
        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDSummaryCreate.TABLE_NAME,
                projection, null, null, null, null, orderBy);
        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_sdSummaryCreates = new ArrayList<>();
            while (lc_cursor.moveToNext()) {
                lc_sdSummaryCreates.add(new SDSummaryCreate(UUID.fromString(
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_ID))),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_TITLE)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_IMG)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION))
                ));
            }
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdSummaryCreates != null)
            para_loadCreatedSummariesCallback.onCreatedSummariesLoaded(lc_sdSummaryCreates);
        else
            para_loadCreatedSummariesCallback.onDataNotAvailable();
    }

    @Override
    public void loadCreatedDetails(@NonNull loadCreatedDetailsCallback para_loadCreatedDetailsCallback
            , String para_summaryId) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        List<SDDetail> lc_sdDetails = null;
        String[] projection = {
                PersistenceContract.SDDetailCreate.COLUMN_NAME_SUMMARYID,
                PersistenceContract.SDDetailCreate.COLUMN_NAME_NUM,
                PersistenceContract.SDDetailCreate.COLUMN_NAME_QUESTION,
                PersistenceContract.SDDetailCreate.COLUMN_NAME_OPTION1,
                PersistenceContract.SDDetailCreate.COLUMN_NAME_OPTION2
        };
        String selection = PersistenceContract.SDDetailCreate.COLUMN_NAME_SUMMARYID + " like ?";
        String[] selectionArgs = {para_summaryId};
        String orderBy = PersistenceContract.SDDetailCreate.COLUMN_NAME_NUM + " ASC";
        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDDetailCreate.TABLE_NAME,
                projection, selection, selectionArgs, null, null, orderBy);
        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_sdDetails = new ArrayList<>();
            while (lc_cursor.moveToNext()) {
                lc_sdDetails.add(new SDDetail(UUID.fromString(para_summaryId),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                                PersistenceContract.SDDetail.COLUMN_NAME_NUM)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                                PersistenceContract.SDDetail.COLUMN_NAME_QUESTION)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                                PersistenceContract.SDDetail.COLUMN_NAME_OPTION1)),
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow(
                                PersistenceContract.SDDetail.COLUMN_NAME_OPTION2))
                ));
            }
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdDetails != null)
            para_loadCreatedDetailsCallback.onCreatedDetailsLoaded(lc_sdDetails);
        else
            para_loadCreatedDetailsCallback.onDataNotAvailable();
    }

    @Override
    public void saveSummariesToLocal(List<SDSummary> sdSummaries) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getWritableDatabase();
        for (SDSummary sd :
                sdSummaries) {
            ContentValues lc_contentValues = new ContentValues();
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_ID,
                    sd.getId().toString());
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_TITLE,
                    sd.getTitle());
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_CREATOR,
                    sd.getCreator());
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME,
                    sd.getCreate_time());
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_IMG,
                    sd.getImg());
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION,
                    sd.getDescription());
            lc_contentValues.put(PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED,
                    sd.isHasFinish());
            lc_sqLiteDatabase.insert(PersistenceContract.SDSummary.TABLE_NAME,
                    null, lc_contentValues);
            lc_contentValues.clear();
        }
        lc_sqLiteDatabase.close();
    }

    @Override
    public void saveDetailsToLocal(List<SDDetail> para_sdDetails) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getWritableDatabase();
        for (SDDetail sd :
                para_sdDetails) {
            ContentValues lc_contentValues = new ContentValues();
            lc_contentValues.put(PersistenceContract.SDDetail.COLUMN_NAME_SUMMARYID,
                    sd.getSd_id().toString());
            lc_contentValues.put(PersistenceContract.SDDetail.COLUMN_NAME_NUM,
                    sd.getQuestion_num());
            lc_contentValues.put(PersistenceContract.SDDetail.COLUMN_NAME_QUESTION,
                    sd.getQuestion());
            lc_contentValues.put(PersistenceContract.SDDetail.COLUMN_NAME_OPTION1,
                    sd.getOptionOne());
            lc_contentValues.put(PersistenceContract.SDDetail.COLUMN_NAME_OPTION2,
                    sd.getOptionTwo());
            lc_sqLiteDatabase.insert(PersistenceContract.SDDetail.TABLE_NAME, null,
                    lc_contentValues);
            lc_contentValues.clear();
        }
        lc_sqLiteDatabase.close();
    }

    @Override
    public void refreshSummaries() {

    }

    @Override
    public void refreshCreatedSummaries() {

    }

    @Override
    public void loadAnswers(@NonNull loadAnswersCallback para_loadAnswersCallback, String summaryId) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getReadableDatabase();
        List<SDAnswer> lc_sdAnswers = null;
        String[] projection = {
                PersistenceContract.SDAnswer.COLUMN_NAME_SUMMARYID,
                PersistenceContract.SDAnswer.COLUMN_NAME_NUM,
                PersistenceContract.SDAnswer.COLUMN_NAME_ANSWER,
        };
        String orderBy = PersistenceContract.SDAnswer.COLUMN_NAME_NUM + " ASC";
        String selection = PersistenceContract.SDAnswer.COLUMN_NAME_SUMMARYID + " like ?";
        String selectionArgs[] = {summaryId};

        Cursor lc_cursor = lc_sqLiteDatabase.query(PersistenceContract.SDSummary.TABLE_NAME,
                projection, selection, selectionArgs, null, null, orderBy);

        if (lc_cursor != null && lc_cursor.getCount() > 0) {
            lc_sdAnswers = new ArrayList<>();
            while (lc_cursor.moveToNext()) {
                lc_sdAnswers.add(new SDAnswer(UUID.fromString(
                        lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDAnswer.COLUMN_NAME_SUMMARYID))),
                        Integer.parseInt(lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDAnswer.COLUMN_NAME_NUM))),
                        Integer.parseInt(lc_cursor.getString(lc_cursor.getColumnIndexOrThrow
                                (PersistenceContract.SDAnswer.COLUMN_NAME_ANSWER)))
                ));
            }
            lc_cursor.close();
        }
        lc_sqLiteDatabase.close();
        if (lc_sdAnswers != null)
            para_loadAnswersCallback.onAnswerLoaded(lc_sdAnswers);
        else
            para_loadAnswersCallback.onDataNotAvailable();
    }

    @Override
    public void saveAnswersToLocal(List<SDAnswer> para_sdAnswers) {
        SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getWritableDatabase();
        for (SDAnswer lc_sdAnswer :
                para_sdAnswers) {
            ContentValues lc_contentValuesDetail = new ContentValues();
            lc_contentValuesDetail.put(PersistenceContract.SDAnswer.COLUMN_NAME_SUMMARYID,
                    lc_sdAnswer.getSummaryId().toString());
            lc_contentValuesDetail.put(PersistenceContract.SDAnswer.COLUMN_NAME_NUM,
                    lc_sdAnswer.getQuestionNum());
            lc_contentValuesDetail.put(PersistenceContract.SDAnswer.COLUMN_NAME_ANSWER,
                    lc_sdAnswer.getAnswer());
            lc_sqLiteDatabase.insert(PersistenceContract.SDAnswer.TABLE_NAME, null,
                    lc_contentValuesDetail);
            lc_contentValuesDetail.clear();
        }
        lc_sqLiteDatabase.close();
    }

    @Override
    public void saveCreatedSDToLocal(SDSummaryCreate para_sdSummaryCreate,
                                     List<SDDetail> para_sdDetails) throws Exception {
        if (para_sdSummaryCreate.getId().equals(para_sdDetails.get(1).getSd_id())) {
            SQLiteDatabase lc_sqLiteDatabase = m_curbDbHelper.getWritableDatabase();
            ContentValues lc_contentValues = new ContentValues();
            lc_contentValues.put(PersistenceContract.SDSummaryCreate.COLUMN_NAME_ID,
                    para_sdSummaryCreate.getId().toString());
            lc_contentValues.put(PersistenceContract.SDSummaryCreate.COLUMN_NAME_TITLE,
                    para_sdSummaryCreate.getTitle());
            lc_contentValues.put(PersistenceContract.SDSummaryCreate.COLUMN_NAME_CREATE_TIME,
                    para_sdSummaryCreate.getCreate_time());
            lc_contentValues.put(PersistenceContract.SDSummaryCreate.COLUMN_NAME_IMGURL,
                    para_sdSummaryCreate.getImg_url());
            lc_contentValues.put(PersistenceContract.SDSummaryCreate.COLUMN_NAME_DESCRIPTION,
                    para_sdSummaryCreate.getDescription());
            lc_sqLiteDatabase.insert(PersistenceContract.SDSummaryCreate.TABLE_NAME,
                    null, lc_contentValues);
            lc_contentValues.clear();

            for (SDDetail sd :
                    para_sdDetails) {
                ContentValues lc_contentValuesDetail = new ContentValues();
                lc_contentValuesDetail.put(PersistenceContract.SDDetail.COLUMN_NAME_SUMMARYID,
                        sd.getSd_id().toString());
                lc_contentValuesDetail.put(PersistenceContract.SDDetail.COLUMN_NAME_NUM,
                        sd.getQuestion_num());
                lc_contentValuesDetail.put(PersistenceContract.SDDetail.COLUMN_NAME_QUESTION,
                        sd.getQuestion());
                lc_contentValuesDetail.put(PersistenceContract.SDDetail.COLUMN_NAME_OPTION1,
                        sd.getOptionOne());
                lc_contentValuesDetail.put(PersistenceContract.SDDetail.COLUMN_NAME_OPTION2,
                        sd.getOptionTwo());
                lc_sqLiteDatabase.insert(PersistenceContract.SDDetail.TABLE_NAME,
                        null, lc_contentValuesDetail);
                lc_contentValuesDetail.clear();
            }
            lc_sqLiteDatabase.close();
        } else {
            throw new Exception("summary中的id与detail中的id不同");
        }
    }
}
