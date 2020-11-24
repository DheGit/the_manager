package com.infinitydheer.themanager.presentation.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.interactor.razgo.UCSync;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;
import com.infinitydheer.themanager.presentation.view.activity.MainActivity;

public class SyncService extends Service {
    private UCSync mInteractor;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TheManagerLog","SyncService received the onStartCommand");
        ChangeListener<ConvDomain> changeListener = new ChangeListener<ConvDomain>() {
            @Override
            public void onNext(ConvDomain result) {
                notifyUser();
            }

            @Override
            public void onComplete() {
                notifyUser();
                Log.d("TheManagerLog","SyncService successfully completed the sync tasks, now stopping self.");
                stopSelfResult(startId);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                Log.d("TheManagerLog","SyncService failed to complete the sync tasks, now stopping self.");
                stopSelfResult(startId);
            }
        };
        if(mInteractor==null){
            mInteractor=new UCSync(RazgoDataRepository.getInstance(this), changeListener);
        }

        mInteractor.sync();
        return super.onStartCommand(intent, flags, startId);
    }

    private void notifyUser(){
        Log.d("TheManagerLog","SyncService notifying user now");

        String channelId = getString(R.string.clean_conv_channel_id);

        NotificationUtils notificationUtils = NotificationUtils.getInstance(this);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        notificationUtils.showNotification(NotificationCompat.PRIORITY_HIGH,
                S.Constants.COME_BACK_NOTIFICATION_TITLE,
                S.Constants.COME_BACK_NOTIFICATION_TEXT,
                R.drawable.ic_come_back_notification,
                NotificationUtils.NOTIF_COME_BACK,
                pendingIntent,true,channelId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
