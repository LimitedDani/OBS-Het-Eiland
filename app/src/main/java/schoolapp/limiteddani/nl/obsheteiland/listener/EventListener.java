package schoolapp.limiteddani.nl.obsheteiland.listener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import schoolapp.limiteddani.nl.obsheteiland.Config;
import schoolapp.limiteddani.nl.obsheteiland.MainActivity;
import schoolapp.limiteddani.nl.obsheteiland.R;
import schoolapp.limiteddani.nl.obsheteiland.json.JSONParser;

/**
 * Created by daniq on 14-4-2017.
 */

public class EventListener {
    String event;
    String user;
    Context context;
    public EventListener(String event, String user, Context context) {
        this.event = event;
        this.user = user;
        this.context = context;
    }
    public void startEvent() {
        ExecuteEvent e = new ExecuteEvent();
        e.execute(this.user, this.event);
    }
    private class ExecuteEvent extends AsyncTask<String, Void, String> {
        JSONParser jParser = new JSONParser();
        String URL = context.getResources().getString(R.string.url_device);
        @Override
        protected String doInBackground(String... s) {
            try {
                List<NameValuePair> get = new ArrayList<NameValuePair>();
                get.add(new BasicNameValuePair("type", "event"));
                get.add(new BasicNameValuePair("id", s[0]));
                get.add(new BasicNameValuePair("event", s[1]));
                jParser.makeHttpRequest(URL, "GET", get);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
