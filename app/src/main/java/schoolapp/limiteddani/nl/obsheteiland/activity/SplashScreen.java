package schoolapp.limiteddani.nl.obsheteiland.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;
import schoolapp.limiteddani.nl.obsheteiland.MainActivity;
import schoolapp.limiteddani.nl.obsheteiland.R;

/**
 * Created by daniq on 14-4-2017.
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View easySplashScreenView = new EasySplashScreen(SplashScreen.this)
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundResource(android.R.color.white)
                .withHeaderText("OBS Het Eiland")
                .withFooterText("Gemaakt door Daníque de Jong")
                .withLogo(R.drawable.splashscreen)
                .create();

        setContentView(easySplashScreenView);
    }
}
