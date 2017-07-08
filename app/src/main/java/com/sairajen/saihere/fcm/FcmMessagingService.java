package com.sairajen.saihere.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
            /*if (remoteMessage.getData().size() > 0) {
                Log.e("TAG",remoteMessage.toString());
                Map<String, String> data = remoteMessage.getData();
                FcmNotif fcmNotif = new FcmNotif();
                fcmNotif.setTitle(data.get("title"));
                fcmNotif.setContent(data.get("content"));
                fcmNotif.setPost_id(Integer.parseInt(data.get("post_id")));
                displayNotificationIntent(fcmNotif);
            }*/
    }

    /*private void displayNotificationIntent(FcmNotif fcmNotif) {
        Intent intent = new Intent(this, SplashActivity.class);
        if (fcmNotif.getPost_id() != -1) {
            intent = new Intent(this, SimplePostDetails.class);
            Post post = new Post();
            post.title = fcmNotif.getTitle();
            post.id = fcmNotif.getPost_id();
            boolean from_notif = !Home.active;
            intent.putExtra(SimplePostDetails.EXTRA_OBJC, post);
            intent.putExtra(SimplePostDetails.EXTRA_NOTIF, from_notif);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(fcmNotif.getTitle());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(fcmNotif.getContent()));
        builder.setContentText(fcmNotif.getContent());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int unique_id = (int) System.currentTimeMillis();
        notificationManager.notify(unique_id, builder.build());
    }*/

}
