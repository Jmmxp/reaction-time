package com.jmmxp.android.reactiontime;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmmxp.android.reactiontime.data.ScoreDbHelper;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String EXTRA_SCORES = "com.jmmxp.android.reactiontime.scores";

    private TextView mInstructionTextView;
    private TextView mScoreboardTextView;
    private RelativeLayout mMainLayout;
    private ImageView mImageView;

    private TransitionDrawable mTransitionDrawable;
    private AlphaAnimation mFadeOut = new AlphaAnimation(1.0f, 0.0f);
    private AlphaAnimation mFadeIn = new AlphaAnimation(0.0f, 1.0f);
    private boolean mGameStart = false;
    private boolean mGameOver = false;
    private boolean mFailScreen = false;
    private int mRandomTime;
    private int mTransitionTime = 500;

    private long[] mScores;

    private ScoreDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mImageView = (ImageView) findViewById(R.id.scoreboard_image_view);
        mImageView.setVisibility(View.VISIBLE);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "IMGE VIEW WAS CLICKED!!");
                showScoreboard();
            }
        });

        Random random = new Random();
        mRandomTime = random.nextInt(10000 - 1000) + 1000;

        mInstructionTextView = (TextView) findViewById(R.id.instruction_text_view);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mScoreboardTextView = (TextView) findViewById(R.id.scoreboard_text_view);

        mTransitionDrawable = (TransitionDrawable) mMainLayout.getBackground();

        mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGameStart) {
                    Log.d(TAG, "Animation should start.");
                    mImageView.animate()
                            .alpha(0.0f)
                            .setDuration(500);
                    mImageView.setClickable(false);
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

                mMainLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!mFailScreen) {
                            mFailScreen = true;
                            mMainLayout.setOnTouchListener(null);

                            animateButtonIn();

                            mGameOver = true;
                            Log.d(TAG, "Game over");
//                        mMainLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.startGame));
                            mMainLayout.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.colour_transition3));
                            mTransitionDrawable = (TransitionDrawable) mMainLayout.getBackground();
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

        if (savedInstanceState != null) {
            mScores = (long[]) savedInstanceState.getSerializable(EXTRA_SCORES);
        } else {
            mScores = new long[]{0, 0, 0, 0, 0};
        }

        checkScore();

        mDbHelper = new ScoreDbHelper(getApplicationContext());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_SCORES, mScores);
    }

    private void insertData() {

    }

    public void onTimeReached() {

        if (!mGameOver) {

            final long startTime = SystemClock.elapsedRealtime();

            mMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.promptClick));
            mMainLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.colour_transition2));
            mTransitionDrawable = (TransitionDrawable) mMainLayout.getBackground();

            mMainLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (!mGameOver) {
                        mGameOver = true;
                        mMainLayout.setOnTouchListener(null);

                        animateButtonIn();
                        Log.d(TAG, "Game over");

                        long endTime = SystemClock.elapsedRealtime();
                        long elapsedMilliseconds = endTime - startTime;
                        addScore(elapsedMilliseconds);
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

    private void addScore(long score) {
        for (int i = 0; i < mScores.length; i++) {
            if (mScores[i] == 0) {
                mScores[i] = score;
                break;
            } else if (score < mScores[i]) {
                for (int j = mScores.length; j > i; j--) {
                    long secondScore = mScores[j - 1];
                    mScores[j] = secondScore;
                }
                mScores[i] = score;
            }
        }

        checkScore();

    }

    private void checkScore() {
        boolean scoreExists = false;
        mScoreboardTextView.setText("");

        for (int i = 0; i < mScores.length; i++) {
            if (mScores[i] != 0) {
                mScoreboardTextView.setText(mScoreboardTextView.getText().toString() + (i + 1) + ". " + mScores[i] + "\n");
                scoreExists = true;
            }
        }

        if (!scoreExists) {
            mScoreboardTextView.setText(R.string.empty_scoreboard);
        }
    }

    private void showScoreboard() {
        mScoreboardTextView.setVisibility(View.VISIBLE);
        mScoreboardTextView.startAnimation(mFadeIn);
        mFadeIn.setDuration(mTransitionTime);
        mFadeIn.setFillAfter(true);

        mInstructionTextView.startAnimation(mFadeOut);
        mFadeOut.setDuration(mTransitionTime);
        mFadeOut.setFillAfter(true);
    }

    private void hideScoreboard() {

    }

    private void animateButtonIn() {
        mImageView.animate()
                .alpha(1.0f)
                .setDuration(500);
        mImageView.setClickable(true);
    }

    private void setResetOnClickListener() {
        mMainLayout.setOnTouchListener(null);
        mMainLayout.setOnClickListener(new View.OnClickListener() {
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
