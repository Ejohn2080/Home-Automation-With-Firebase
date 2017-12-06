package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by francesco on 12/02/2017.
 */

class NotificationManager {

    private static final String TAG = NotificationManager.class.getSimpleName();

    private static NotificationManager me = null;

    private NotificationManager() {}

    static NotificationManager getInstance() {
        if (me == null)
            me = new NotificationManager();

        return me;
    }

    void sendNotificaton(String message) {

        (new FirebaseNotificationTask()).execute(message, "Your token here");
    }

    private class FirebaseNotificationTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String msg = strings[0];
            String key = strings[1];
            Log.d(TAG, "Send data");
            try {
                HttpURLConnection con = (HttpURLConnection) (new URL("http://fcm.googleapis.com/fcm/send")).openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "key=" + key);
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                String body = "{\n" +
                        "  \"to\": \"/topics/Alert\",\n" +
                        "  \"data\": {\n" +
                        "  \"message\": \"" + msg + "\"" +
                        "  }\n" +
                        "}";
                Log.d(TAG, "Body [" + body + "]");
                con.getOutputStream().write(body.getBytes());
                InputStream is = con.getInputStream();
                byte[] buffer = new byte[1024];
                while ( is.read(buffer) != -1)
                    Log.d(TAG, new String(buffer));
                con.disconnect();

            }
            catch(Throwable t) {
                t.printStackTrace();
            }

            return null;
        }
    }
}
