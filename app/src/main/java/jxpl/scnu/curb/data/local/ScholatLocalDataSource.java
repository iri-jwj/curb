package jxpl.scnu.curb.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import jxpl.scnu.curb.data.repository.ScholatDataSource;
import jxpl.scnu.curb.homePage.scholat.ScholatHomework;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by iri-jwj on 2018/3/27.
 *
 * @author iri-jwj
 * @version 1
 */

public class ScholatLocalDataSource implements ScholatDataSource {
    private CurbDbHelper m_curbDbHelper;
    private ScholatLocalDataSource m_scholatLocalDataSource = null;

    public ScholatLocalDataSource(Context para_context) {
        m_curbDbHelper = new CurbDbHelper(para_context);
    }

    public ScholatLocalDataSource getInstance(@NonNull Context para_context) {
        if (m_scholatLocalDataSource == null)
            m_scholatLocalDataSource = new ScholatLocalDataSource(checkNotNull(para_context));

        return m_scholatLocalDataSource;
    }

    @Override
    public void saveHomeworkToLocal(List<ScholatHomework> para_homeworkList) {
        SQLiteDatabase lc_database = m_curbDbHelper.getWritableDatabase();
        for (ScholatHomework homework :
                para_homeworkList) {
            ContentValues lc_contentValues = new ContentValues();
            lc_contentValues.put(PersistenceContract.ScholatEntry.COLUMN_NAME_ID,
                    homework.getHomeworkId().toString());
            lc_contentValues.put(PersistenceContract.ScholatEntry.COLUMN_NAME_TITLE,
                    homework.getTitle());
            lc_contentValues.put(PersistenceContract.ScholatEntry.COLUMN_NAME_CONTENT,
                    homework.getContent());
            lc_contentValues.put(PersistenceContract.ScholatEntry.COLMN_NAME_ENDTIME,
                    homework.getEndTime());
            lc_contentValues.put(PersistenceContract.ScholatEntry.COLUMN_NAME_CREATETIME,
                    homework.getCreateTime());
            lc_database.insert(PersistenceContract.ScholatEntry.TABLE_NAME,
                    null, lc_contentValues);
            lc_contentValues.clear();
        }
        lc_database.close();
    }

    @Override
    public void loadHomeworks(LoadHomeworkCallback para_loadHomeworkCallback, String userId, Context para_context) {
        List<ScholatHomework> lc_homeworks = new LinkedList<>();
        SQLiteDatabase lc_liteDatabase = m_curbDbHelper.getReadableDatabase();
        String[] projection = {
                PersistenceContract.ScholatEntry.COLUMN_NAME_ID,
                PersistenceContract.ScholatEntry.COLUMN_NAME_TITLE,
                PersistenceContract.ScholatEntry.COLUMN_NAME_CONTENT,
                PersistenceContract.ScholatEntry.COLMN_NAME_ENDTIME,
                PersistenceContract.ScholatEntry.COLUMN_NAME_CREATETIME
        };
        String orderby = PersistenceContract.ScholatEntry.COLUMN_NAME_CREATETIME + " DESC";
        Cursor lc_cursor = lc_liteDatabase.query(PersistenceContract.ScholatEntry.TABLE_NAME,
                projection, null, null, null, null, orderby);

        if (lc_cursor != null && lc_cursor.getCount() >= 0) {
            while (lc_cursor.moveToNext()) {
                lc_homeworks.add(new ScholatHomework(
                        lc_cursor
                                .getString(lc_cursor
                                        .getColumnIndexOrThrow(PersistenceContract
                                                .ScholatEntry.COLUMN_NAME_ID)),
                        lc_cursor.getString(lc_cursor
                                .getColumnIndexOrThrow(PersistenceContract
                                        .ScholatEntry.COLUMN_NAME_TITLE)),
                        lc_cursor.getString(lc_cursor
                                .getColumnIndexOrThrow(PersistenceContract
                                        .ScholatEntry.COLUMN_NAME_CONTENT)),
                        lc_cursor.getString(lc_cursor
                                .getColumnIndexOrThrow(PersistenceContract
                                        .ScholatEntry.COLUMN_NAME_CREATETIME)),
                        lc_cursor.getString(lc_cursor
                                .getColumnIndexOrThrow(PersistenceContract
                                        .ScholatEntry.COLMN_NAME_ENDTIME))
                ));
            }
            lc_cursor.close();
            para_loadHomeworkCallback.onHomeworkLoaded(lc_homeworks);
        } else {
            para_loadHomeworkCallback.onDataNotAvailable();
        }
        lc_liteDatabase.close();
    }

    @Override
    public void refreshCache() {

    }
}
