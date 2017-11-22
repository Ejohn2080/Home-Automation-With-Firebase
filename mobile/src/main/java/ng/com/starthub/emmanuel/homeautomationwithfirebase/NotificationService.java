package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by Emmanuel on 21/11/2017.
 */

public class NotificationService extends FirebaseMessagingService{
    private static final String TAG = NotificationService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Msg", "Message received ["+remoteMessage+"]");

        // Create Notification
        Intent intent = new Intent(this, MainActivityMobile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1690, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String info = null;

        if (remoteMessage.getData().size() > 0) {
            info = remoteMessage.getData().get("message");
        }

        if (remoteMessage.getNotification() != null) {
            info = remoteMessage.getNotification().getBody();
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Message")
                .setContentText(info)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(1690, notificationBuilder.build());
        }
    }
}
