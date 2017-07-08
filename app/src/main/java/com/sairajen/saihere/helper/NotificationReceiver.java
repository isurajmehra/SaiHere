package com.sairajen.saihere.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.sairajen.saihere.R;
import com.sairajen.saihere.activity.Home;

/**
 * @author Gmonetix
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeatingIntent = new Intent(context, Home.class);
        repeatingIntent.putExtra("IS_ACCESSED_FROM_NOTIFICATION",true);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle("Sai Here")
                .setContentText("Today saibaba's message for you")
                .setAutoCancel(true);

        notificationManager.notify(100,builder.build());

    }

}
