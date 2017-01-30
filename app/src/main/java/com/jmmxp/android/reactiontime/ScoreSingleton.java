package com.jmmxp.android.reactiontime;

import android.content.Context;

/**
 * Created by jmmxp on 29/01/17.
 */

public class ScoreSingleton {
    private static ScoreSingleton sScoreLab;

    private Context mContext;
    private long[] mScores;

    public static ScoreSingleton get(Context context) {
        if (sScoreLab == null) {
            sScoreLab = new ScoreSingleton(context);
        }
        return sScoreLab;
    }

    private ScoreSingleton(Context context) {
        mContext = context.getApplicationContext();

    }

}
