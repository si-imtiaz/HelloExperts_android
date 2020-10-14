package com.HelloExperts.HelloExpert;

import android.content.Intent;
import android.content.SharedPreferences;
import com.daimajia.androidanimations.library.Techniques;
import com.HelloExperts.HelloExpert.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash extends AwesomeSplash {
    SharedPreferences dashboard_preference;



    @Override
    public void initSplash(ConfigSplash configSplash) {



        this.dashboard_preference =this.getSharedPreferences("Login",0);

        configSplash.setBackgroundColor(R.color.header_color);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagY(Flags.REVEAL_TOP);

        configSplash.setLogoSplash(R.drawable.logo_helloexperts_white);
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.RollIn);

        configSplash.setOriginalHeight(400);
        configSplash.setOriginalWidth(400);
        configSplash.setAnimPathStrokeDrawingDuration(2000);
        configSplash.setPathSplashStrokeSize(3);
        configSplash.setPathSplashStrokeColor(R.color.colorAccent);
        configSplash.setAnimPathFillingDuration(1000);
        configSplash.setPathSplashFillColor(R.color.Wheat2);



            configSplash.setTitleSplash("");

        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(50f);
        configSplash.setAnimTitleDuration(2000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/ostrich-regular.ttf");
    }
    @Override
    public void animationsFinished() {
        startActivity(new Intent(Splash.this,WelcomeActivity.class));
        finish();
    }


}