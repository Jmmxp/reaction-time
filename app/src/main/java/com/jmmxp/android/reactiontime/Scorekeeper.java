package com.jmmxp.android.reactiontime;

/**
 * Created by jmmxp on 22/01/17.
 */

public class Scorekeeper {
    private int[] mScores;

    public Scorekeeper(int[] scores) {
        mScores = scores;
    }

    public int[] getScores() {
        return mScores;
    }

    public void setScores(int[] scores) {
        mScores = scores;
    }
}
