package com.jmmxp.android.reactiontime;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mInstructionTextView;
    private LinearLayout mMainLinearLayout;
    private TransitionDrawable mTransitionDrawable;
    private AlphaAnimation mFadeOut = new AlphaAnimation(1.0f, 0.0f);
    private AlphaAnimation mFadeIn = new AlphaAnimation(0.0f, 1.0f);
    private boolean mGameStart = false;
    private int mRandomTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Random random = new Random();
        mRandomTime = random.nextInt(10000 - 1000) + 1000;

        mInstructionTextView = (TextView) findViewById(R.id.instruction_text_view);
        mMainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);

        mTransitionDrawable = (TransitionDrawable) mMainLinearLayout.getBackground();

        mMainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGameStart) {
                    Log.d(TAG, "Animation should start.");

                    mGameStart = true;

                    mInstructionTextView.startAnimation(mFadeOut);
                    mFadeOut.setDuration(1000);
                    mFadeOut.setFillAfter(true);

                    mTransitionDrawable.startTransition(1000);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onTimeReached();
                        }
                    }, mRandomTime);
                } else {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }

            }
        });

    }

    public void onTimeReached() {

        final long startTime = SystemClock.elapsedRealtime();

        mMainLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.promptClick));
        mMainLinearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.colour_transition2));
        mTransitionDrawable = (TransitionDrawable) mMainLinearLayout.getBackground();

        mMainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Game over");

                long endTime = SystemClock.elapsedRealtime();
                long elapsedMilliSeconds = endTime - startTime;
                double elapsedSeconds = elapsedMilliSeconds / 1000.0;

                mTransitionDrawable.startTransition(1000);

                mInstructionTextView.startAnimation(mFadeIn);
                mFadeIn.setDuration(1000);
                mFadeIn.setFillAfter(true);

                mInstructionTextView.setText(elapsedSeconds + " seconds");


            }
        });

    }

}
