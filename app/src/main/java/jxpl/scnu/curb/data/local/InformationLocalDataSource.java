package jxpl.scnu.curb.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import jxpl.scnu.curb.data.repository.InformationDataSource;
import jxpl.scnu.curb.immediateInformation.ImmediateInformation;

import static com.google.common.base.Preconditions.checkNotNull;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_CONTENT;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_DATE;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_ID;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_TIME;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_TITLE;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.COLUMN_NAME_TYPE;
import static jxpl.scnu.curb.data.local.PersistenceContract.informationEntry.TABLE_NAME;

/**
 * Created by irijw on 2017/10/15.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationLocalDataSource implements InformationDataSource{
    private InformationDbHelper informationDbHelper;

    private static InformationLocalDataSource INSTANCE;

    private InformationLocalDataSource (@NonNull Context context){
        checkNotNull(context);
        informationDbHelper=new InformationDbHelper(context);
    }

    public static InformationLocalDataSource getInstace(@NonNull Context context){
        if (INSTANCE==null){
            INSTANCE=new InformationLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getInformation(@NonNull getInformationCallback callback,@NonNull String id) {
        SQLiteDatabase sqLiteDatabase=informationDbHelper.getWritableDatabase();
        String[] projection={
                PersistenceContract.informationEntry.COLUMN_NAME_ID,
                PersistenceContract.informationEntry.COLUMN_NAME_TITLE,
                PersistenceContract.informationEntry.COLUMN_NAME_TIME,
                PersistenceContract.informationEntry.COLUMN_NAME_CONTENT,
                PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL,
                PersistenceContract.informationEntry.COLUMN_NAME_DATE,
                PersistenceContract.informationEntry.COLUMN_NAME_TYPE
        };
        String selection=PersistenceContract.informationEntry.COLUMN_NAME_ID+"like ?";
        String[] selectionArgs={id};
        Cursor cursor=sqLiteDatabase.query(PersistenceContract.informationEntry.TABLE_NAME,projection,
                selection,selectionArgs,null,null,null);

        ImmediateInformation immediateInformation=null;
        if (cursor!=null&&cursor.getCount()>0){
           cursor.moveToFirst();
           String title=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TITLE));
           String time=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TIME));
           String content=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT));
           String content_url=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL));
           String type=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TYPE));
           String date=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_DATE));
           immediateInformation=new ImmediateInformation(id,title,date,time,content,type,content_url);
        }
        if (cursor!=null){
            cursor.close();
        }
        sqLiteDatabase.close();

        if (immediateInformation!=null)
            callback.onInformationLoaded(immediateInformation);
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void getInformations(@NonNull loadInformationCallback callback) {
        SQLiteDatabase sqLiteDatabase=informationDbHelper.getWritableDatabase();
        String[] projection={
                PersistenceContract.informationEntry.COLUMN_NAME_ID,
                PersistenceContract.informationEntry.COLUMN_NAME_TITLE,
                PersistenceContract.informationEntry.COLUMN_NAME_TIME,
                PersistenceContract.informationEntry.COLUMN_NAME_CONTENT,
                PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL,
                PersistenceContract.informationEntry.COLUMN_NAME_DATE,
                PersistenceContract.informationEntry.COLUMN_NAME_TYPE
        };
        Cursor cursor=sqLiteDatabase.query(PersistenceContract.informationEntry.TABLE_NAME,projection,
                null,null,null,null,null);

        ImmediateInformation immediateInformation=null;
        List<ImmediateInformation> immediateInformations=new ArrayList<>();
        if (cursor!=null&&cursor.getCount()>0){
            while (cursor.moveToNext()){
                String id=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_ID));
                String title=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TITLE));
                String time=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TIME));
                String content=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT));
                String content_url=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL));
                String type=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_TYPE));
                String date=cursor.getString(cursor.getColumnIndexOrThrow(PersistenceContract.informationEntry.COLUMN_NAME_DATE));
                immediateInformation=new ImmediateInformation(id,title,date,time,content,type,content_url);
                immediateInformations.add(immediateInformation);
            }
        }
        if (cursor!=null){
            cursor.close();
        }
        sqLiteDatabase.close();

        if (immediateInformation!=null)
            callback.getInformationsLoaded(immediateInformations);
        else
            callback.onDataNotAvailable();
    }

    @Override
    public void addToArrangement(ImmediateInformation immediateInformation) {
    }

    @Override
    public void saveInfoFromWeb(List<ImmediateInformation> immediateInformations){
        SQLiteDatabase sqLiteDatabase= informationDbHelper.getWritableDatabase();
        for (ImmediateInformation i:
             immediateInformations) {
            ContentValues contentValues=new ContentValues();
            contentValues.put(COLUMN_NAME_CONTENT_URL,i.getContent_url());
            contentValues.put(COLUMN_NAME_CONTENT,i.getContent());
            contentValues.put(COLUMN_NAME_DATE,i.getDate());
            contentValues.put(COLUMN_NAME_TIME,i.getTime());
            contentValues.put(COLUMN_NAME_TYPE,i.getType());
            contentValues.put(COLUMN_NAME_TITLE,i.getTitle());
            contentValues.put(COLUMN_NAME_ID,i.getId());
            sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
            contentValues.clear();
        }
        sqLiteDatabase.close();
    }

    @Override
    public void refreshInformation() {

    }
}
