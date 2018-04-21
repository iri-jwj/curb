package jxpl.scnu.curb.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_ADDRESS;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_CONTENT;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_ID;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_TIME;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_TITLE;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_BELONG;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.TABLE_NAME;

/**
 * @author iri-jwj
 * @version 2
 * 2018 3 23
 */

public class InformationLocalDataSource implements InformationDataSource {
    private static InformationLocalDataSource INSTANCE;
    private CurbDbHelper curbDbHelper;
    private InformationLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        curbDbHelper = new CurbDbHelper(context);
    }

    public static InformationLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new InformationLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getInformation(@NonNull GetInformationCallback callback, UUID id) {
        SQLiteDatabase sqLiteDatabase = curbDbHelper.getReadableDatabase();
        String[] projection = {
                PersistenceContract.informationEntry.COLUMN_NAME_ID,
                PersistenceContract.informationEntry.COLUMN_NAME_TITLE,
                PersistenceContract.informationEntry.COLUMN_NAME_CONTENT,
                PersistenceContract.informationEntry.COLUMN_NAME_BELONG,
                PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME,
                PersistenceContract.informationEntry.COLUMN_NAME_TIME,
                PersistenceContract.informationEntry.COLUMN_NAME_ADDRESS
        };
        String selection = PersistenceContract.informationEntry.COLUMN_NAME_ID + " like ?";
        String[] selectionArgs = {id.toString()};
        Cursor cursor = sqLiteDatabase.query(PersistenceContract.informationEntry.TABLE_NAME, projection,
                selection, selectionArgs, null, null, null);

        ImmediateInformation immediateInformation = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor
                    .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TITLE));
            String content = cursor.getString(cursor
                    .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT));
            String belong = cursor.getString(cursor
                    .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_BELONG));
            String createTime = cursor.getString(cursor
                    .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME));
            String time = cursor.getString(cursor
                    .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TIME));
            String address = cursor.getString(cursor
                    .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_ADDRESS));
            immediateInformation = new ImmediateInformation(id, title,
                    content, belong, createTime, time, address);
        }
        if (cursor != null) {
            cursor.close();
        }
        sqLiteDatabase.close();

        if (immediateInformation != null) {
            Log.d("localDataSource", "getInformation: " + immediateInformation.getTitle());
            callback.onInformationLoaded(immediateInformation);
        } else
            callback.onDataNotAvailable();
    }

    @Override
    public void getInformations(@NonNull LoadInformationCallback callback,
                                String userId, String timestamp) {
        SQLiteDatabase sqLiteDatabase = curbDbHelper.getWritableDatabase();
        String[] projection = {
                PersistenceContract.informationEntry.COLUMN_NAME_ID,
                PersistenceContract.informationEntry.COLUMN_NAME_TITLE,
                PersistenceContract.informationEntry.COLUMN_NAME_CONTENT,
                PersistenceContract.informationEntry.COLUMN_NAME_BELONG,
                PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME,
                PersistenceContract.informationEntry.COLUMN_NAME_TIME,
                PersistenceContract.informationEntry.COLUMN_NAME_ADDRESS
        };
        String orderBy = PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME + " DESC";
        Cursor cursor = sqLiteDatabase.query(PersistenceContract.informationEntry.TABLE_NAME, projection,
                null, null, null, null, orderBy);

        ImmediateInformation immediateInformation;
        List<ImmediateInformation> immediateInformations = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                UUID id = UUID.fromString(cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_ID)));
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TITLE));
                String content = cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT));
                //String content_url = cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL));
                String belong = cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_BELONG));
                //String date = cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_DATE));
                String createTime = cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME));
                String time = cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TIME));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_ADDRESS));
                immediateInformation = new ImmediateInformation(id, title,
                        content, belong, createTime, time, address);

                immediateInformations.add(immediateInformation);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        sqLiteDatabase.close();

        if (!immediateInformations.isEmpty())
            callback.getInformationsLoaded(immediateInformations);
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void addToReminder(ImmediateInformation immediateInformation) {
    }

    @Override
    public void saveInfoFromWeb(List<ImmediateInformation> immediateInformations) {
        SQLiteDatabase sqLiteDatabase = curbDbHelper.getWritableDatabase();
        for (ImmediateInformation i :
                immediateInformations) {
            Log.d("localDataSource", "saveInfoFromWeb:" + i.toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_ID, i.getId().toString());
            contentValues.put(COLUMN_NAME_TITLE, i.getTitle());
            contentValues.put(COLUMN_NAME_CONTENT, i.getContent());
            contentValues.put(COLUMN_NAME_BELONG, i.getBelong());
            contentValues.put(COLUMN_NAME_CREATETIME, i.getCreateTime());
            contentValues.put(COLUMN_NAME_TIME, i.getTime());
            contentValues.put(COLUMN_NAME_ADDRESS, i.getAddress());
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            contentValues.clear();
        }
        sqLiteDatabase.close();
    }

    @Override
    public void refreshInformation() {

    }

    @Override
    public void postInformation(PostInformationCallback para_callback, String information, String userId) {

    }
}
