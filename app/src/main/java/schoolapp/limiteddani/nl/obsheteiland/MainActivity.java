package schoolapp.limiteddani.nl.obsheteiland;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import schoolapp.limiteddani.nl.obsheteiland.activity.About;
import schoolapp.limiteddani.nl.obsheteiland.activity.Article;
import schoolapp.limiteddani.nl.obsheteiland.activity.News;
import schoolapp.limiteddani.nl.obsheteiland.activity.Settings;
import schoolapp.limiteddani.nl.obsheteiland.json.JSONParser;
import schoolapp.limiteddani.nl.obsheteiland.listener.EventListener;
import schoolapp.limiteddani.nl.obsheteiland.listener.NotificationService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CardView cv;
    ImageView cardimage;
    TextView cardtitle;
    TextView carddescription;
    String pdfurl = "";
    String[] permissions = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Config c = new Config();
        if (isNetworkAvailable()) {
            Log.d("device_id", "" + c.getDeviceID(this));
            setContentView(R.layout.activity_main);
            cv = (CardView) findViewById(R.id.home_card);
            cardimage = (ImageView) findViewById(R.id.home_card_image);
            cardtitle = (TextView) findViewById(R.id.home_card_title);
            carddescription = (TextView) findViewById(R.id.home_card_description);
            cv.setVisibility(View.INVISIBLE);
            EventListener event = new EventListener("app_open", c.getDeviceID(this), this);
            event.startEvent();
            try {
                loadHomeArticle lha = new loadHomeArticle();
                lha.execute();
            } catch (Exception e) {
                Log.i(Level.INFO.toString(), "Kan niet verbinden met server.");
            }
        } else {
            setContentView(R.layout.activity_internet);
        }
        checkPermissions();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (c.isFirstLaunch(this)) {
            c.startFirstLaunchConfiguration(this);
        }
        Intent serviceIntent = new Intent(this, NotificationService.class);
        this.startService(serviceIntent);
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent n = new Intent(MainActivity.this, Settings.class);
            MainActivity.this.startActivity(n);
            return true;
        } else if(id == R.id.action_about) {
            Intent n = new Intent(MainActivity.this, About.class);
            MainActivity.this.startActivity(n);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Config c = new Config();
        if(c.isDeviceIDSetted(this)) {
            int id = item.getItemId();
            if (id == R.id.nav_groep12a) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g12a");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep12b) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g12b");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep12c) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g12c");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep34) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g34");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep45) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g45");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep56) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g56");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep7) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g7");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            } else if (id == R.id.nav_groep8) {
                Intent n = new Intent(MainActivity.this, News.class);
                n.putExtra("groep", "g8");
                MainActivity.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
            Log.i(Level.INFO.toString(), "Kan niet verbinden met server.");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public class loadHomeArticle extends AsyncTask<Void, Void, Void> {

        JSONParser jParser = new JSONParser();
        String URL_NOTIFICATION = getResources().getString(R.string.url_news);
        private boolean cvinvisible = false;
        private boolean connected = true;
        private String titel;
        private String description;
        private String pdf;
        private String image;
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_NEWS = "articles";
        private static final String TAG_TITEL = "title";
        private static final String TAG_DESCRIPTION = "description";
        private static final String TAG_AUTHOR = "author";
        private static final String TAG_PDF = "pdf";
        private static final String TAG_IMAGE = "image";
        private static final String TAG_ID = "ID";

        JSONArray requests = null;

        @Override
        public Void doInBackground(Void... params) {
            List<NameValuePair> get = new ArrayList<NameValuePair>();
            get.add(new BasicNameValuePair("page", "home"));
            JSONObject json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);

            try {
                Log.d("Return: ", json.toString());
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    requests = json.getJSONArray(TAG_NEWS);
                    for (int i = 0; i < requests.length(); i++) {
                        JSONObject c = requests.getJSONObject(i);

                        titel = c.getString(TAG_TITEL);
                        description = c.getString(TAG_DESCRIPTION);
                        pdf = c.getString(TAG_PDF);
                        image = c.getString(TAG_IMAGE);
                    }
                } else if(success == 2){
                    cvinvisible = true;
                }
            } catch (Exception e) {
                connected = false;
                Log.i(Level.INFO.toString(), "Kan niet verbinden met server.");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(!connected) {
                cv.setVisibility(View.INVISIBLE);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.content_main), "Geen internet verbinding!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Opnieuw", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    loadHomeArticle lha = new loadHomeArticle();
                                    lha.execute();
                                } catch (Exception e) {
                                    Log.i(Level.INFO.toString(), "Kan niet verbinden met server.");
                                }
                            }
                        });
                snackbar.setActionTextColor(Color.WHITE);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }else if(cvinvisible) {
                cv.setVisibility(View.INVISIBLE);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.content_main), "Geen artikelen gevonden!", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.WHITE);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            } else {
                cardtitle.setText(titel);
                carddescription.setText(description);
                Picasso.with(MainActivity.this).load(image).into(cardimage);
                pdfurl = pdf;
                cv.setVisibility(View.VISIBLE);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Article.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("pdf", pdf);
                        intent.putExtra("title", titel);
                        v.getContext().startActivity(intent);
                    }
                }) ;
            }
        }
    }
}
