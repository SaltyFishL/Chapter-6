package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量

    public static final String SQL_CREATE_NOTES =
            "CREATE TABLE " + TodoNotes.TABLE_NAME
                    + "(" + TodoNotes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TodoNotes.COLUMN_DATE + " INTEGER, "
                    + TodoNotes.COLUMN_STATE + " INTEGER, "
                    + TodoNotes.COLUMN_CONTENT + " TEXT, "
                    + TodoNotes.COLUMN_PRIORITY + " INTEGER)";

    public static final String SQL_DELETE_NOTES =
            "DROP TABLE IF EXISTS " + TodoNotes.TABLE_NAME;
    private TodoContract() {
    }

    public static class TodoNotes implements BaseColumns {
        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_PRIORITY = "priority";
    }

}
