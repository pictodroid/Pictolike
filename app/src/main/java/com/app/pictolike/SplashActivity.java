package com.app.pictolike;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.pictolike.Utils.AppConfig;

public class SplashActivity extends AbstractAppActivity {

    // Splash Screen timeout interval
    private static int SPLASH_TIME_OUT = 1700;
    private static final int FRAME_TIME_OUT = 50;
    private ImageView splashImageView;
    private AnimationDrawable anim;

    private ImageView parentLayout;

    private Handler backgroundHandler = new Handler() {

        public void handleMessage(Message msg) {
            Animation mAnim = AnimationUtils.loadAnimation(SplashActivity.this,
                    android.R.anim.fade_out);
            mAnim.setDuration(1700);
            mAnim.setRepeatCount(0);
            mAnim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    parentLayout.setBackgroundColor(Color.WHITE);
                    Intent i = new Intent(SplashActivity.this, LandingScreenActivity.class);

                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                }
            });
            parentLayout.startAnimation(mAnim);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();

        BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw01_high);
        BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw02_high);
        BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw03_high);
        BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw04_high);
        BitmapDrawable frame5 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw05_high);
        BitmapDrawable frame6 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw06_high);
        BitmapDrawable frame7 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw07_high);
        BitmapDrawable frame8 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw08_high);
        BitmapDrawable frame9 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.large_pw09_high);

        anim = new AnimationDrawable();
        anim.addFrame(frame9, FRAME_TIME_OUT);
        anim.addFrame(frame8, FRAME_TIME_OUT);
        anim.addFrame(frame7, FRAME_TIME_OUT);
        anim.addFrame(frame6, FRAME_TIME_OUT);
        anim.addFrame(frame5, FRAME_TIME_OUT);
        anim.addFrame(frame4, FRAME_TIME_OUT);
        anim.addFrame(frame3, FRAME_TIME_OUT);
        anim.addFrame(frame2, FRAME_TIME_OUT);
        anim.addFrame(frame1, FRAME_TIME_OUT);
        anim.setOneShot(false);

        if (AppConfig.DEBUG) {
            Log.d(mTag, "onCreate :: I am Launching first time");
        }
        setContentView(R.layout.activity_splash);
        splashImageView = (ImageView) findViewById(R.id.splash_imageview);

        splashImageView.setImageDrawable(anim);
        parentLayout = (ImageView) findViewById(R.id.splash_bg_imageview);
        anim.start();

        backgroundHandler.sendEmptyMessageDelayed(0, SPLASH_TIME_OUT);
    }
}