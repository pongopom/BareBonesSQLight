package com.examlpe.android.barebonessqlight.data;

import android.provider.BaseColumns;

/**
 * Created by peterpomlett on 04/12/2017.
 */

public class ToDoContract {
    public static final class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "toDo";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CHECK_MARK = "checkMark";
    }
}
