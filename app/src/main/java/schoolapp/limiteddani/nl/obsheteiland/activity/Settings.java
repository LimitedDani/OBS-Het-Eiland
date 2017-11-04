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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

import schoolapp.limiteddani.nl.obsheteiland.BuildConfig;
import schoolapp.limiteddani.nl.obsheteiland.Config;
import schoolapp.limiteddani.nl.obsheteiland.R;

public class Settings extends AppCompatActivity
{
    CheckBox g12a, g12b, g12c, g34, g45, g56, g7, g8;
    Switch not;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        final Config c = new Config();

        g12a = (CheckBox) findViewById(R.id.cb_g12a);
        g12b = (CheckBox) findViewById(R.id.cb_g12b);
        g12c = (CheckBox) findViewById(R.id.cb_g12c);
        g34 = (CheckBox) findViewById(R.id.cb_g34);
        g45 = (CheckBox) findViewById(R.id.cb_g45);
        g56 = (CheckBox) findViewById(R.id.cb_g56);
        g7 = (CheckBox) findViewById(R.id.cb_g7);
        g8 = (CheckBox) findViewById(R.id.cb_g8);
        not = (Switch) findViewById(R.id.cb_n);

        if(c.isSubcribedOnGroup(this, "g12a")) {
            g12a.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g12b")) {
            g12b.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g12c")) {
            g12c.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g34")) {
            g34.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g45")) {
            g45.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g56")) {
            g56.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g7")) {
            g7.setChecked(true);
        }
        if(c.isSubcribedOnGroup(this, "g8")) {
            g8.setChecked(true);
        }

        not.setChecked(c.receiveNotifications(this));

        g12a.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g12a", isChecked);
            }
        });
        g12b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g12b", isChecked);
            }
        });
        g12c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g12c", isChecked);
            }
        });
        g34.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g34", isChecked);
            }
        });
        g45.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g45", isChecked);
            }
        });
        g56.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g56", isChecked);
            }
        });
        g7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g7", isChecked);
            }
        });
        g8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setGroupSubcribe(Settings.this, "g8", isChecked);
            }
        });
        not.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setReceiveNotifications(Settings.this, isChecked);
            }
        });







    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
