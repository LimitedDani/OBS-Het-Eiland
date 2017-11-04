package schoolapp.limiteddani.nl.obsheteiland.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;
import java.io.IOException;
import schoolapp.limiteddani.nl.obsheteiland.Config;
import schoolapp.limiteddani.nl.obsheteiland.MainActivity;
import schoolapp.limiteddani.nl.obsheteiland.R;
import schoolapp.limiteddani.nl.obsheteiland.extension.FileDownloader;
import schoolapp.limiteddani.nl.obsheteiland.listener.EventListener;
import schoolapp.limiteddani.nl.obsheteiland.listener.NotificationService;

public class Article extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    ProgressDialog dialog = null;
    String title = "";
    String notification = "";
    boolean toMainActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Artikel laden....");
        dialog.show();
        if(bundle != null)
        {
            String url =(String) bundle.get("pdf");
            if(!url.isEmpty()) {
                if(isFilePresent(url.split("/")[url.split("/").length - 1])) {
                    openPDF(url.split("/")[url.split("/").length - 1]);
                } else{
                    new DownloadFile().execute(url, url.split("/")[url.split("/").length - 1]);
                }
            }
            title = (String) bundle.get("title");
            if(!title.isEmpty()) {
                getSupportActionBar().setTitle(title);
            } else {
                getSupportActionBar().setTitle("Artikel");
            }
            if(bundle.containsKey("notification")) {
                notification = (String) bundle.get("notification");
                if (!notification.isEmpty()) {
                    toMainActivity = true;
                }
            }
        }

        Config c = new Config();
        EventListener e = new EventListener("article_view", c.getDeviceID(this), this);
        e.startEvent();
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        int pageNumber = page;
        getSupportActionBar().setTitle(String.format("%s, %s van %s", title, page + 1, pageCount));
    }
    @Override
    public void loadComplete(int nbPages) {
        dialog.cancel();
    }
    @Override
    public void onBackPressed() {
        if(toMainActivity) {
            Intent n = new Intent(Article.this, MainActivity.class);
            finish();
            Article.this.startActivity(n);
        } else {
            super.onBackPressed();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                dialog.setMessage("Artikel downloaden....");
                String fileUrl = strings[0];   // -> url
                String fileName = strings[1];  // -> .pdf file
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "obsheteiland");
                folder.mkdir();

                File pdfFile = new File(folder, fileName);

                pdfFile.createNewFile();
                if (FileDownloader.downloadFile(fileUrl, pdfFile)) {
                    openPDF(fileName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public boolean isFilePresent(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/obsheteiland/" + fileName);
        return file.exists();
    }
    public void openPDF(String fileName) {
        PDFView pdfviewer = (PDFView) findViewById(R.id.article);
        File pdf = new File(Environment.getExternalStorageDirectory() + "/obsheteiland/" + fileName);
        try {
            pdfviewer.fromFile(pdf)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .onPageChange(this)
                    .onLoad(this)
                    .defaultPage(0)
                    .enableAntialiasing(true)
                    .load();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
