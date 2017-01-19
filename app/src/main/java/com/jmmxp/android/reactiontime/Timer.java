package com.jmmxp.android.reactiontime;

import android.os.SystemClock;

/**
 * Created by jmmxp on 18/01/17.
 * http://stackoverflow.com/questions/15248891/how-to-measure-elapsed-time
 */

public class Timer implements Runnable {
    Thread thread;
    int stopTime;

    public Timer(int stopTime) {
        this.stopTime = stopTime;
        thread = new Thread(this);
    }

    @Override
    public void run() {
        long startTime = SystemClock.elapsedRealtime();
        long endTime = SystemClock.elapsedRealtime();
        while ((endTime - startTime) <= stopTime) {
            endTime = SystemClock.elapsedRealtime();
        }

    }
}
