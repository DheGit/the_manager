package com.infinitydheer.themanager.presentation.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

import androidx.core.app.NotificationManagerCompat;

import java.lang.ref.WeakReference;

public class NotificationUtils {
    public static final int NOTIF_COME_BACK = 1;

    private static NotificationUtils sInstance;

    private WeakReference<Context> mContext;

    private NotificationUtils(Context context) {
        mContext = new WeakReference<>(context);
    }

    public static synchronized NotificationUtils getInstance(Context context) {
        if (sInstance == null) sInstance = new NotificationUtils(context);
        return sInstance;
    }

    public void showNotification(int priority, CharSequence title, CharSequence text, int smallIcon, int notificationId,
                                 PendingIntent pendingIntent, boolean autoCancel) {
        Context c = mContext.get();
        if (c == null) return;

        String channelId="";

        Notification.Builder builder = new Notification.Builder(c);//FIXME This is deprecated
        builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(smallIcon)
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(autoCancel)
                .setOnlyAlertOnce(true);//TODO Make this a parameter

        NotificationManagerCompat.from(c).notify(notificationId, builder.build());
    }

    public void showNotification(int priority, CharSequence title, CharSequence text, int smallIcon, int notificationId,
                                 PendingIntent pendingIntent, boolean autoCancel, String channelId) {
        Context c = mContext.get();
        if (c == null) return;

        Notification.Builder builder = null;//FIXME This is deprecated
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(c, channelId);
        }else{
            builder=new Notification.Builder(c);
        }
        builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(smallIcon)
                .setPriority(priority)
                .setContentIntent(pendingIntent)
                .setAutoCancel(autoCancel)
                .setOnlyAlertOnce(true);//TODO Make this a parameter

        NotificationManagerCompat.from(c).notify(notificationId, builder.build());
    }

    public void clearAllNotifications(){
        Context c=mContext.get();
        if(c==null) return;

        NotificationManagerCompat.from(c).cancelAll();
    }
}
