package com.jmmxp.android.reactiontime.data;

import android.provider.BaseColumns;

/**
 * Created by jmmxp on 28/01/17.
 */

public final class ScoreContract {

    private ScoreContract () {

    }

    public static final class ScoreEntry implements BaseColumns {

        public static final String TABLE_NAME = "scores";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_TIME = "time";

    }

}
