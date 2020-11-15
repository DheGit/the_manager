package com.infinitydheer.themanager.data.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;

import androidx.core.app.NotificationCompat;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoMasterDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.repository.RazgoMasterRepository;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;

public class CleanConversationService extends Service {
    private static final int FOREGROUND_NOTIF_ID=1001;//TODO: Get this to a place for constants

    private RazgoMasterRepository mMasterRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        mMasterRepository= RazgoMasterDataRepository.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mMasterRepository==null) mMasterRepository=RazgoMasterDataRepository.getInstance();
        String channelId=createNotificationChannel();

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this,channelId);
        notificationBuilder.setContentText(S.Constants.CLEAN_CONV_NOTIFICATION_TEXT)
                .setContentTitle(S.Constants.CLEAN_CONV_NOTIFICATION_TITLE)
                .setSmallIcon(R.drawable.ic_come_back_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(FOREGROUND_NOTIF_ID,notificationBuilder.build());

        mMasterRepository.cleanConversation(intent.getLongExtra(S.Constants.INTENT_CONV_ID_CLEAN_KEY, -1),
                new ChangeListener<Void>() {
                    @Override
                    public void onNext(Void result) {}
                    @Override
                    public void onComplete() {
                        stopForeground(true);
                        stopSelf();
                    }
                    @Override
                    public void onError(Exception e) {
                        //Handle error if necessary here
                        stopForeground(true);
                        stopSelf();
                    }
                });

        return START_NOT_STICKY;
    }

    private String createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String CHANNEL_ID=getString(R.string.clean_conv_channel_id);
            CharSequence name = getString(R.string.clean_conv_channel_name);
            String descr = getString(R.string.clean_conv_channel_descr);
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descr);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            return CHANNEL_ID;
        }
        return "";
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
