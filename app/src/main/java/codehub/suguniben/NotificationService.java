package codehub.suguniben;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by IK on 2018-01-03.
 */

public class NotificationService extends FirebaseMessagingService {

    FireClass fireClass;
    Activity activity;

    public NotificationService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        fireClass = new FireClass();
        String Title = remoteMessage.getNotification().getTitle();
        String clickAction = remoteMessage.getNotification().getClickAction();
        String dataImage = remoteMessage.getData().get("image");
        String time = fireClass.timeago(Long.parseLong(remoteMessage.getData().get("time")));
        String Body = remoteMessage.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Title)
                .setContentText(Body);

        Intent intent = new Intent(clickAction);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        int notificationId = (int) System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

}
