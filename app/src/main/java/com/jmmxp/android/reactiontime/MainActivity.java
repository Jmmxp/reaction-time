package com.jmmxp.android.reactiontime;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
    private boolean mGameOver = false;
    private boolean mFailScreen = false;
    private int mRandomTime;
    private int mTransitionTime = 500;

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

                    animateTextOut();

                    mTransitionDrawable.startTransition(mTransitionTime);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onTimeReached();
                        }
                    }, mRandomTime);

                }

                mMainLinearLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!mFailScreen) {
                            mFailScreen = true;
                            mMainLinearLayout.setOnTouchListener(null);
                            mGameOver = true;
                            Log.d(TAG, "Game over");
//                        mMainLinearLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.startGame));
                            mMainLinearLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.colour_transition3));
                            mTransitionDrawable = (TransitionDrawable) mMainLinearLayout.getBackground();
                            mTransitionDrawable.startTransition(mTransitionTime);

                            animateTextIn("You tapped too early! Try again.");

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setResetOnClickListener();
                                }
                            }, mTransitionTime);
                        }

                        return true;

                    }
                });

            }
        });

    }

    public void onTimeReached() {

        final MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.ding);
        player.start();

        if (!mGameOver) {

            final long startTime = SystemClock.elapsedRealtime();

            mMainLinearLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.promptClick));
            mMainLinearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.colour_transition2));
            mTransitionDrawable = (TransitionDrawable) mMainLinearLayout.getBackground();

            mMainLinearLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (!mGameOver) {
                        mGameOver = true;
                        mMainLinearLayout.setOnTouchListener(null);
                        Log.d(TAG, "Game over");

                        long endTime = SystemClock.elapsedRealtime();
                        long elapsedMilliseconds = endTime - startTime;
                        // double elapsedSeconds = elapsedMilliSeconds / 1000.0;

                        mTransitionDrawable.startTransition(mTransitionTime);

                        animateTextIn(elapsedMilliseconds + " milliseconds");

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResetOnClickListener();
                            }
                        }, mTransitionTime);
                    }

                    return true;

                }
            });

        }

    }

    private void setResetOnClickListener() {
        mMainLinearLayout.setOnTouchListener(null);
        mMainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    private void animateTextIn(CharSequence text) {
        mInstructionTextView.startAnimation(mFadeIn);
        mFadeIn.setDuration(mTransitionTime);
        mFadeIn.setFillAfter(true);
        mInstructionTextView.setText(text);
    }

    private void animateTextOut() {
        mInstructionTextView.startAnimation(mFadeOut);
        mFadeOut.setDuration(mTransitionTime);
        mFadeOut.setFillAfter(true);
    }

}
