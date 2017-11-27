package jxpl.scnu.curb.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by irijw on 2017/10/14.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

public class InformationDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="Information.db";
    InformationDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static final String TEXT_TYPE=" TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PersistenceContract.informationEntry.TABLE_NAME + "(" +
                    PersistenceContract.informationEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.informationEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_DATE+TEXT_TYPE+COMMA_SEP+
                    PersistenceContract.informationEntry.COLUMN_NAME_TIME+TEXT_TYPE+COMMA_SEP+
                    PersistenceContract.informationEntry.COLUMN_NAME_TYPE+TEXT_TYPE+COMMA_SEP+
                    PersistenceContract.informationEntry.COLUMN_NAME_CONTENT_URL+TEXT_TYPE+
                    ")";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {       // Not required as at version 1
    }
}
