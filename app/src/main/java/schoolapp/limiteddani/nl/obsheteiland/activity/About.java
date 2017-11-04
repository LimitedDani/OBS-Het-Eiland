package schoolapp.limiteddani.nl.obsheteiland.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

import schoolapp.limiteddani.nl.obsheteiland.BuildConfig;
import schoolapp.limiteddani.nl.obsheteiland.Config;
import schoolapp.limiteddani.nl.obsheteiland.R;

public class About extends AppCompatActivity
{
    TextView version;
    TextView author_line;
    Integer[] backgrounds = {
            R.drawable.background_1_blurred, R.drawable.background_2_blurred,
            R.drawable.background_3_blurred, R.drawable.background_4_blurred,
            R.drawable.background_5_blurred
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Config c = new Config();
        version = (TextView) findViewById(R.id.about_version);
        author_line = (TextView) findViewById(R.id.about_authorline);

        String versionName = BuildConfig.VERSION_NAME;
        version.setText("versie " + versionName);
        author_line.setText(R.string.app_author_line);

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeveloperInfo(v);
            }

        });

        RelativeLayout layout =(RelativeLayout)findViewById(R.id.content_about);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap background = BitmapFactory.decodeResource(getResources(), backgrounds[new Random().nextInt(backgrounds.length)], options);
        BitmapDrawable ob = new BitmapDrawable(getResources(), background);
        layout.setBackgroundDrawable(ob);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void openDeveloperInfo(View view) {
        Config c = new Config();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle((CharSequence) "Uitgebreide Informatie");
        builder.setMessage(Html.fromHtml("<b>Device ID:</b> " + c.getDeviceID(this) + "<br /><br /><b>Package name:</b> schoolapp.limiteddani.nl.obsheteiland<br /><br /><b>Google Play Services:</b> 9.4.0<br /><b>Fabric Version:</b> 2.7.0<br /><br /><b>Gebruikte Libraries:</b><br />Android Support Library <br />Google Play Services<br />Picasso<br />Fabric<br />"));
        builder.setPositiveButton((CharSequence) "Sluiten", null);
        builder.show();
    }
}
