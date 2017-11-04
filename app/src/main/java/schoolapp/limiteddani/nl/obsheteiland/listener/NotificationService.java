package schoolapp.limiteddani.nl.obsheteiland.listener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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

import schoolapp.limiteddani.nl.obsheteiland.Config;
import schoolapp.limiteddani.nl.obsheteiland.MainActivity;
import schoolapp.limiteddani.nl.obsheteiland.R;
import schoolapp.limiteddani.nl.obsheteiland.activity.Article;
import schoolapp.limiteddani.nl.obsheteiland.json.JSONParser;

/**
 * Created by daniq on 6-4-2017.
 */

public class NotificationService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static boolean isStarted = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Creating service
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (isNetworkAvailable()) {
                    Config c = new Config();
                    if(c.isDeviceIDSetted(context)) {
                        LoadNotifications ln = new LoadNotifications();
                        ln.execute();
                        handler.postDelayed(runnable, 10000);

                        /*int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "Searching for notifications", duration);
                        toast.show();*/
                    }
                }
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        //Service Stopped
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Notification service stopped", duration);
        toast.show();
        sendNotification("Notification service stopped", "Notification service stopped", "all", "");

        Intent serviceIntent = new Intent(this, NotificationService.class);
        this.startService(serviceIntent);
    }
    public void DestroyService() {
        //Service destroyed
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Notification service destroyed", duration);
        toast.show();
        sendNotification("Notification service destroyed", "Notification service destroyed", "all", "");
        handler.removeCallbacks(runnable);
    }
    @Override
    public void onStart(Intent intent, int startid) {
        //Service started by user
    }
    public void onTaskRemoved(Intent intent){
        super.onTaskRemoved(intent);
        Intent service = new Intent(this,this.getClass());
        startService(service);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        if (isStarted){      //yes - do nothing
            return Service.START_STICKY;
        } else {             //no
            isStarted = true;
        }
        return Service.START_STICKY;
    }
    public void sendNotification(String title, String text, String receiver, String extra) {
        Config c = new Config();
        boolean send;
        if(receiver.contains("all")) {
            send = true;
        } else {
            if(c.isSubcribedOnGroup(context, receiver) && c.receiveNotifications(context)) {
                send = true;
            } else {
                send = false;
            }
        }
        if(send) {
            Intent intent = null;
            if(extra.contains("pdf: ")) {
                intent = new Intent(this, Article.class);
                intent.putExtra("pdf", extra.split(",")[0].replaceAll("pdf: ", ""));
                intent.putExtra("title", extra.split(",")[1].replaceAll("title: ", ""));
                intent.putExtra("notification", "true");
            } else if(extra.contains("url: ")) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(extra.split(" ")[1]));
            } else {
                intent = new Intent(this, MainActivity.class);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_launcher))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setSound(notificationSound)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notifiBuilder.build());
        }
    }
    public class LoadNotifications extends AsyncTask<Void, Void, Void> {
        private List<Integer> notifications = new ArrayList<>();
        private HashMap<Integer, String> notifications_title = new HashMap<>();
        private HashMap<Integer, String> notifications_text = new HashMap<>();
        private HashMap<Integer, String> notifications_receiver = new HashMap<>();
        private HashMap<Integer, String> notifications_extra = new HashMap<>();
            JSONParser jParser = new JSONParser();
        NotificationService ns = new NotificationService();
        String URL_NOTIFICATION = getResources().getString(R.string.url_notification);

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_NEWS = "notifications";
        private static final String TAG_TITEL = "title";
        private static final String TAG_TEKST = "text";
        private static final String TAG_EXTRA = "extra";
        private static final String TAG_RECEIVER = "receiver";

        JSONArray requests = null;

        @Override
        public Void doInBackground(Void... params) {
            List<NameValuePair> get = new ArrayList<NameValuePair>();
            Config config = new Config();
            if(config.isDeviceIDSetted(context)) {
                get.add(new BasicNameValuePair("user", config.getDeviceID(context)));
                JSONObject json = jParser.makeHttpRequest(URL_NOTIFICATION, "GET", get);
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        requests = json.getJSONArray(TAG_NEWS);
                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);

                            String titel = c.getString(TAG_TITEL);
                            String tekst = c.getString(TAG_TEKST);
                            String receiver = c.getString(TAG_RECEIVER);
                            String extra = c.getString(TAG_EXTRA);
                            notifications_title.put(i, titel);
                            notifications_text.put(i, tekst);
                            notifications_receiver.put(i, receiver);
                            notifications_extra.put(i, extra);
                            notifications.add(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            for(int i : notifications) {
                String not_title = notifications_title.get(i);
                String not_text = notifications_text.get(i);
                String not_receiver = notifications_receiver.get(i);
                String not_extra = notifications_extra.get(i);
                sendNotification(not_title, not_text, not_receiver, not_extra);
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
