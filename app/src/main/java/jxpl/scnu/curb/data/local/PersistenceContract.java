package jxpl.scnu.curb.data.local;

import android.provider.BaseColumns;


final class PersistenceContract {
    private PersistenceContract(){
    }
    static abstract class informationEntry implements BaseColumns {
        static final String COLUMN_NAME_ID="id";
         static final String TABLE_NAME = "information";
         static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_DATE = "datetime";
         static final String COLUMN_NAME_CONTENT="content";
         static final String COLUMN_NAME_TYPE="type";
         static final String COLUMN_NAME_CONTENT_URL="contentUrl";
    }

    static abstract class AccountEntry implements BaseColumns {
        static final String COLUMN_NAME_ID = "id";
        static final String TABLE_NAME = "accountManage";
        static final String COLUMN_NAME_ACCOUNT = "account";
        static final String COLUMN_NAME_PASSWORD = "password";
        static final String COLUMN_NAME_LEVEL = "level";
    }

    static abstract class SDSummary implements BaseColumns {
        static final String TABLE_NAME = "SmallDataSummary";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_DESCRIPTION = "description";
        static final String COLUMN_NAME_IMG = "img_url";
        static final String COLUMN_NAME_CREATE_TIME = "create_time";
        static final String COLUMN_NAME_CREATOR = "creator";
        static final String COLUMN_NAME_HAVEFINISHED = "have_finished";
    }

    static abstract class SDDetail implements BaseColumns {
        static final String TABLE_NAME = "SmallDataDetail";
        static final String COLUMN_NAME_SUMMARYID = "summary_id";
        static final String COLUMN_NAME_NUM = "question_num";
        static final String COLUMN_NAME_QUESTION = "question";
        static final String COLUMN_NAME_OPTION1 = "option1";
        static final String COLUMN_NAME_OPTION2 = "option2";
    }

    static abstract class SDAnswer implements BaseColumns {
        static final String TABLE_NAME = "SmallDataAnswer";
        static final String COLUMN_NAME_NUM = "question_num";
        static final String COLUMN_NAME_SUMMARYID = "summary_id";
        static final String COLUMN_NAME_ANSWER = "answer";
    }

    static abstract class SDSummaryCreate implements BaseColumns {
        static final String TABLE_NAME = "SmallDataSummaryCreated";
        static final String COLUMN_NAME_ID = "id";
        static final String COLUMN_NAME_TITLE = "title";
        static final String COLUMN_NAME_IMGURL = "img_url";
        static final String COLUMN_NAME_DESCRIPTION = "description";
        static final String COLUMN_NAME_CREATE_TIME = "create_time";
    }

    static abstract class SDDetailCreate implements BaseColumns {
        static final String TABLE_NAME = "SmallDataDetailCreated";
        static final String COLUMN_NAME_SUMMARYID = "summary_id";
        static final String COLUMN_NAME_NUM = "question_num";
        static final String COLUMN_NAME_QUESTION = "question";
        static final String COLUMN_NAME_OPTION1 = "option1";
        static final String COLUMN_NAME_OPTION2 = "option2";
    }
}
