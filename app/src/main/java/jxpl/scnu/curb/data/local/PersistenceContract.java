package jxpl.scnu.curb.data.local;

import android.provider.BaseColumns;

/**
 * Created by irijw on 2017/10/14.
 * all great things are simple,
 * and many can be expressed in single words:
 * freedom, justice, honor, duty, mercy, hope.
 * --Winsdon Churchill
 */

final class PersistenceContract {
    private PersistenceContract(){
    }
    static abstract class informationEntry implements BaseColumns {
        static final String COLUMN_NAME_ID="id";
         static final String TABLE_NAME = "information";
         static final String COLUMN_NAME_TITLE = "title";
         static final String COLUMN_NAME_DATE="date";
         static final String COLUMN_NAME_TIME="time";
         static final String COLUMN_NAME_CONTENT="content";
         static final String COLUMN_NAME_TYPE="type";
         static final String COLUMN_NAME_CONTENT_URL="contentUrl";
    }
}
