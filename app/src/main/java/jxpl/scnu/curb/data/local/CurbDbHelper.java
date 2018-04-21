package jxpl.scnu.curb.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author iri-jwj
 * @version 2
 *          update 3/27
 *          新增 scholat 数据表的创建方法
 */
public class CurbDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Information.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String TIMESTAMP_TYPE = " TIMESTAMP";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_INFORMATION_ENTRIES =
            "CREATE TABLE " + PersistenceContract.informationEntry.TABLE_NAME + "(" +
                    PersistenceContract.informationEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY, " +
                    PersistenceContract.informationEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_BELONG + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_CREATETIME + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.informationEntry.COLUMN_NAME_ADDRESS + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_ACCOUNT_ENTRIES =
            "CREATE TABLE " + PersistenceContract.AccountEntry.TABLE_NAME + "(" +
                    PersistenceContract.AccountEntry.COLUMN_NAME_ID + INT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.AccountEntry.COLUMN_NAME_ACCOUNT + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.AccountEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.AccountEntry.COLUMN_NAME_LEVEL + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_SDSUMMARY_ENTRIES =
            "CREATE TABLE " + PersistenceContract.SDSummary.TABLE_NAME + "(" +
                    PersistenceContract.SDSummary.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.SDSummary.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummary.COLUMN_NAME_CREATOR + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummary.COLUMN_NAME_CREATE_TIME + TIMESTAMP_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummary.COLUMN_NAME_IMG + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummary.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummary.COLUMN_NAME_HAVEFINISHED + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_SDDETIAL_ENTRIES =
            "CREATE TABLE " + PersistenceContract.SDDetail.TABLE_NAME + "(" +
                    PersistenceContract.SDDetail.COLUMN_NAME_SUMMARYID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.SDDetail.COLUMN_NAME_NUM + INT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetail.COLUMN_NAME_QUESTION + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetail.COLUMN_NAME_OPTION1 + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetail.COLUMN_NAME_OPTION2 + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_SDANSWER_ENTRIES =
            "CREATE TABLE " + PersistenceContract.SDAnswer.TABLE_NAME + "(" +
                    PersistenceContract.SDAnswer.COLUMN_NAME_SUMMARYID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDAnswer.COLUMN_NAME_NUM + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDAnswer.COLUMN_NAME_ANSWER + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_SDSUMMARYCREATE_ENTRIES =
            "CREATE TABLE " + PersistenceContract.SDSummaryCreate.TABLE_NAME + "(" +
                    PersistenceContract.SDSummaryCreate.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY," +
                    PersistenceContract.SDSummaryCreate.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummaryCreate.COLUMN_NAME_IMGURL + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummaryCreate.COLUMN_NAME_CREATE_TIME + TIMESTAMP_TYPE + COMMA_SEP +
                    PersistenceContract.SDSummaryCreate.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_SDDETIALCREATE_ENTRIES =
            "CREATE TABLE  " + PersistenceContract.SDDetailCreate.TABLE_NAME + "(" +
                    PersistenceContract.SDDetailCreate.COLUMN_NAME_SUMMARYID + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetailCreate.COLUMN_NAME_NUM + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetailCreate.COLUMN_NAME_QUESTION + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetailCreate.COLUMN_NAME_OPTION1 + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.SDDetailCreate.COLUMN_NAME_OPTION2 + TEXT_TYPE +
                    ")";
    private static final String SQL_CREATE_SCHOLAT_ENTRIES =
            "CREATE TABLE " + PersistenceContract.ScholatEntry.TABLE_NAME + "(" +
                    PersistenceContract.ScholatEntry.COLUMN_NAME_ID + TEXT_TYPE + " PRIMARY KEY, " +
                    PersistenceContract.ScholatEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.ScholatEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.ScholatEntry.COLMN_NAME_ENDTIME + TEXT_TYPE + COMMA_SEP +
                    PersistenceContract.ScholatEntry.COLUMN_NAME_CREATETIME + TIMESTAMP_TYPE + ")" ;

    CurbDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_INFORMATION_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SDSUMMARY_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SDDETIAL_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SDANSWER_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SDSUMMARYCREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SDDETIALCREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_SCHOLAT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {       // Not required as at version 1
    }
}
