package schoolapp.limiteddani.nl.obsheteiland;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schoolapp.limiteddani.nl.obsheteiland.json.JSONParser;
import schoolapp.limiteddani.nl.obsheteiland.listener.NotificationService;

/**
 * Created by daniq on 7-4-2017.
 */

public class Config {
    public boolean isFirstLaunch(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("first_launch", true);
    }
    public void setDeviceID(Context context, String value) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("device_id", value);
        editor.apply();
    }
    public String getDeviceID(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        if(settings.contains("device_id")
                && !settings.getString("device_id", "error").contains("error")
                && !settings.getString("device_id", "error").isEmpty()) {
            return settings.getString("device_id", "error");
        } else {
            generateNewID gnid = new generateNewID();
            gnid.execute(context);
            return settings.getString("device_id", "error");
        }
    }
    public boolean isDeviceIDSetted(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        return settings.contains("device_id")
                && !settings.getString("device_id", "error").contains("error")
                && !settings.getString("device_id", "error").isEmpty();
    }
    public boolean getBackgroundSearchForNotifications(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("background_running", true);
    }
    public void setBackgroundSearchForNotifications(Context context, boolean value) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("background_running", value);
        editor.apply();
    }
    public void setFirstLaunch(Context context, boolean value) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("first_launch", value);
        editor.apply();
    }
    public boolean receiveNotifications(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean("notifications", true);
    }
    public void setReceiveNotifications(Context context, boolean value) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("notifications", value);
        editor.apply();
    }
    public void startFirstLaunchConfiguration(Context context) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("first_launch", false);
        editor.putString("user_id", "");
        editor.putBoolean("background_running", true);

        editor.putBoolean("g12a", false);
        editor.putBoolean("g12b", false);
        editor.putBoolean("g12c", false);
        editor.putBoolean("g34", false);
        editor.putBoolean("g45", false);
        editor.putBoolean("g56", false);
        editor.putBoolean("g7", false);
        editor.putBoolean("g8", false);
        editor.putBoolean("not", true);
        editor.apply();
    }
    public void setGroupSubcribe(Context context, String value, boolean bol) {
        String[] groups = {"g12a", "g12b", "g12c", "g34", "g45", "g56", "g7", "g8"};
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(value, bol);
        editor.apply();
    }
    public boolean isSubcribedOnGroup(Context context, String group) {
        SharedPreferences settings = context.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        return settings.getBoolean(group, false);
    }
    public class generateNewID extends AsyncTask<Context, Void, Void> {

        private String newid;
        JSONParser jParser = new JSONParser();
        NotificationService ns = new NotificationService();
        String URL_NOTIFICATION;
        Context context;
        private static final String TAG_SUCCESS = "success";
        private static final String TAG_NEWS = "device";
        private static final String TAG_ID = "id";

        JSONArray requests = null;

        @Override
        public Void doInBackground(Context... params) {
            List<NameValuePair> get = new ArrayList<NameValuePair>();
            context = params[0];
            URL_NOTIFICATION = context.getResources().getString(R.string.url_device);
            get.add(new BasicNameValuePair("type", "generate"));
            JSONObject json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);

            try {
                Log.d("Return: ", json.toString());
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    requests = json.getJSONArray(TAG_NEWS);
                    for (int i = 0; i < requests.length(); i++) {
                        JSONObject c = requests.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        newid = id;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setDeviceID(context, newid);
        }
    }
}
