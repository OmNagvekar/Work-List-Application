package com.example.worklist;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,MainActivity.class);

        SharedPreferences sh = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        int uid = sh.getInt("UID",9999);
        String s1 = sh.getString("name", "");
        int a = sh.getInt("age", 0);
        String noti = sh.getString("title","Your Task");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,uid,i,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder   builder = new NotificationCompat.Builder(context,"note1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Work List")
                .setContentText(noti+" Is Due")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());

    }
}
