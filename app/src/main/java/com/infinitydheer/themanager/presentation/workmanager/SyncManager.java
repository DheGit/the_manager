package com.infinitydheer.themanager.presentation.workmanager;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;
import com.infinitydheer.themanager.presentation.view.activity.MainActivity;

import java.lang.ref.WeakReference;

public class SyncManager extends Worker {
    private WeakReference<Context> mContext;

    public SyncManager(@NonNull Context context, @NonNull WorkerParameters params){
        super(context,params);
        this.mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public Result doWork() {

        return null;
    }

    private void notifyUser(Context c){
        if(c==null) return;

        String channelId=c.getString(R.string.clean_conv_channel_id);

        NotificationUtils notificationUtils = NotificationUtils.getInstance(c);

        PendingIntent pendingIntent=PendingIntent.getActivity(c,0,
                new Intent(c, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        notificationUtils.showNotification(NotificationCompat.PRIORITY_HIGH,
                S.Constants.COME_BACK_NOTIFICATION_TITLE,
                S.Constants.COME_BACK_NOTIFICATION_TEXT,
                R.drawable.ic_come_back_notification,
                NotificationUtils.NOTIF_COME_BACK,
                pendingIntent,true,channelId);
    }
}
