package com.example.quizzy.Splash_Activity;

import android.content.Intent;
import com.daimajia.androidanimations.library.Techniques;
import com.example.quizzy.Create_Quiz.Custom_quiz;
import com.example.quizzy.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash_create_quiz extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.btn_logout_bg);
        configSplash.setAnimCircularRevealDuration(500);
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);
        configSplash.setLogoSplash(0);
        configSplash.setAnimLogoSplashDuration(0);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);
        configSplash.setOriginalHeight(400);
        configSplash.setOriginalWidth(400);
        configSplash.setAnimPathStrokeDrawingDuration(0);
        configSplash.setPathSplashStrokeSize(3);
        configSplash.setPathSplashStrokeColor(R.color.btn_logout_bg);
        configSplash.setAnimPathFillingDuration(50);
        configSplash.setPathSplashFillColor(R.color.strokeColor);
        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.btn_logout_bg);
        configSplash.setTitleTextSize(30f);
        configSplash.setAnimTitleDuration(0);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    }
    @Override
    public void animationsFinished() {
        startActivity(new Intent(Splash_create_quiz.this, Custom_quiz.class));
        finish();

    }
}
