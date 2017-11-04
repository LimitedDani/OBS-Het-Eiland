package schoolapp.limiteddani.nl.obsheteiland.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import schoolapp.limiteddani.nl.obsheteiland.Config;
import schoolapp.limiteddani.nl.obsheteiland.MainActivity;
import schoolapp.limiteddani.nl.obsheteiland.R;
import schoolapp.limiteddani.nl.obsheteiland.json.JSONParser;
import schoolapp.limiteddani.nl.obsheteiland.listener.EventListener;
import schoolapp.limiteddani.nl.obsheteiland.news.CustomAdapter;
import schoolapp.limiteddani.nl.obsheteiland.news.DataModel;

public class News extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<DataModel> dataModels;
    ListView listView;
    String groep = "";
    ProgressDialog dialog = null;
    private static CustomAdapter adapter;
    String[] permissions = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Config c = new Config();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            final String action = (String) bundle.get("groep");
            if(!action.isEmpty()) {
                switch (action) {
                    case "g12a":
                        setTitle("Nieuws groep 1/2a");
                        break;
                    case "g12b":
                        setTitle("Nieuws groep 1/2b");
                        break;
                    case "g12c":
                        setTitle("Nieuws groep 1/2c");
                        break;
                    case "g34":
                        setTitle("Nieuws groep 3/4");
                        break;
                    case "g45":
                        setTitle("Nieuws groep 4/5");
                        break;
                    case "g56":
                        setTitle("Nieuws groep 5/6");
                        break;
                    case "g7":
                        setTitle("Nieuws groep 7");
                        break;
                    case "g8":
                        setTitle("Nieuws groep 8");
                        break;
                    default:
                        break;
                }
            }
            try {
                if (!action.isEmpty()) {
                    if(isNetworkAvailable()) {
                        dialog=new ProgressDialog(News.this);
                        dialog.setCancelable(true);
                        dialog.setMessage("Laden ....");
                        dialog.show();
                        groep = action;
                        loadArticles la = new loadArticles();
                        la.execute(action);

                        EventListener e = new EventListener("search_news", c.getDeviceID(this), this);
                        e.startEvent();
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.content_news), "Geen internet verbinding!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Opnieuw", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(isNetworkAvailable()) {
                                            try {
                                                News.loadArticles lha = new News.loadArticles();
                                                lha.execute(action);
                                            } catch (Exception e) {
                                                Log.i(Level.INFO.toString(), "Kan niet verbinden met server.");
                                            }
                                        }
                                    }
                                });
                        snackbar.setActionTextColor(Color.WHITE);

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
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
            Intent n = new Intent(News.this, Settings.class);
            News.this.startActivity(n);
            return true;
        } else if(id == R.id.action_about) {
            Intent n = new Intent(News.this, About.class);
            News.this.startActivity(n);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startNoInternet() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.content_news), "Geen internet verbinding!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Opnieuw", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            News.loadArticles lha = new News.loadArticles();
                            lha.execute(groep);
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
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_groep12a) {
            if(!groep.contains("12a")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g12a");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep12b) {
            if(!groep.contains("12b")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g12b");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep12c) {
            if(!groep.contains("12c")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g12c");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep34) {
            if(!groep.contains("34")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g34");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep45) {
            if(!groep.contains("45")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g45");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep56) {
            if(!groep.contains("56")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g56");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep7) {
            if(!groep.contains("7")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g7");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if (id == R.id.nav_groep8) {
            if(!groep.contains("8")) {
                Intent n = new Intent(News.this, News.class);
                n.putExtra("groep", "g8");
                finish();
                News.this.startActivity(n);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        } else if(id == R.id.nav_home) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
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
    public class loadArticles extends AsyncTask<String, Void, Void> {
        boolean connection = true;
        JSONParser jParser = new JSONParser();
        String URL_NOTIFICATION = getResources().getString(R.string.url_news);
        final private List<Integer> id = new ArrayList<Integer>();
        private HashMap<Integer, String> title = new HashMap<>();
        private HashMap<Integer, String> image = new HashMap<>();
        private HashMap<Integer, String> description = new HashMap<>();
        private HashMap<Integer, String> pdf = new HashMap<>();
        private String group = "12a";

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_ID = "ID";
        private static final String TAG_NEWS = "articles";
        private static final String TAG_TITEL = "title";
        private static final String TAG_DESCRIPTION = "description";
        private static final String TAG_PDF = "pdf";
        private static final String TAG_IMAGE = "image";

        JSONArray requests = null;

        @Override
        public Void doInBackground(final String... params) {
            if(!isNetworkAvailable()) {
                startNoInternet();
                cancel(true);
            }
            List<NameValuePair> get = new ArrayList<NameValuePair>();
            get.add(new BasicNameValuePair("page", params[0]));
            get.add(new BasicNameValuePair("getid", ""));
            JSONObject json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);
            group = params[0];
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    requests = json.getJSONArray(TAG_NEWS);
                    for (int i = 0; i < requests.length(); i++) {
                        JSONObject c = requests.getJSONObject(i);
                        id.add(Integer.parseInt(c.getString(TAG_ID)));
                    }
                } else if(success == 2) {
                    Log.d("Return: ", json.toString());
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.content_news), "Geen artikelen gevonden!", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();
                    dialog.cancel();
                } else {
                    dialog.cancel();
                    cancel(true);
                }
            } catch (Exception e) {
                dialog.cancel();
                connection = false;
                e.printStackTrace();
            }
            for(int ii = 0; ii < id.size(); ii++) {
                get.clear();
                get.add(new BasicNameValuePair("page", params[0]));
                get.add(new BasicNameValuePair("id", id.get(ii) + ""));
                get.add(new BasicNameValuePair("title", ""));
                json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        requests = json.getJSONArray(TAG_NEWS);
                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);
                            title.put(id.get(ii), c.getString(TAG_TITEL));
                        }
                    } else {
                        cancel(true);
                    }
                } catch (JSONException e) {
                    connection = false;
                    e.printStackTrace();
                }
            }
            for(int ii = 0; ii < id.size(); ii++) {
                get.clear();
                get.add(new BasicNameValuePair("page", params[0]));
                get.add(new BasicNameValuePair("id", id.get(ii) + ""));
                get.add(new BasicNameValuePair("description", ""));
                json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        requests = json.getJSONArray(TAG_NEWS);
                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);
                            description.put(id.get(ii), c.getString(TAG_DESCRIPTION));
                        }
                    } else {
                        cancel(true);
                    }
                } catch (JSONException e) {
                    connection = false;
                    e.printStackTrace();
                }
            }
            for(int ii = 0; ii < id.size(); ii++) {
                get.clear();
                get.add(new BasicNameValuePair("page", params[0]));
                get.add(new BasicNameValuePair("id", id.get(ii) + ""));
                get.add(new BasicNameValuePair("pdf", ""));
                json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        requests = json.getJSONArray(TAG_NEWS);
                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);
                            pdf.put(id.get(ii), c.getString(TAG_PDF));
                        }
                    } else {
                        cancel(true);
                    }
                } catch (JSONException e) {
                    connection = false;
                    e.printStackTrace();
                }
            }
            for(int ii = 0; ii < id.size(); ii++) {
                get.clear();
                get.add(new BasicNameValuePair("page", params[0]));
                get.add(new BasicNameValuePair("id", id.get(ii) + ""));
                get.add(new BasicNameValuePair("image", ""));
                json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        requests = json.getJSONArray(TAG_NEWS);
                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);
                            image.put(id.get(ii), c.getString(TAG_IMAGE));
                        }
                    }
                } catch (JSONException e) {
                    connection = false;
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (!connection) {
                    final Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.content_news), "Geen internet verbinding!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Opnieuw", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    News.loadArticles lha = new News.loadArticles();
                                    lha.execute(group);
                                    Log.i(Level.INFO.toString(), "Uitvoeren.");
                                }
                            });
                    snackbar.setActionTextColor(Color.WHITE);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } else if (id.size() > 0) {
                    listView = (ListView) findViewById(R.id.news_list);
                    dataModels = new ArrayList<>();
                    for (int i = 0; i < id.size(); i++) {
                        dataModels.add(new DataModel(image.get(id.get(i)), title.get(id.get(i)), description.get(id.get(i)), pdf.get(id.get(i))));
                    }
                    adapter = new CustomAdapter(dataModels, getApplicationContext());
                    listView.setAdapter(adapter);
                    dialog.cancel();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
