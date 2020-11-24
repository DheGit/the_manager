package com.infinitydheer.themanager.data.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.presentation.services.SyncService;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;
import com.infinitydheer.themanager.presentation.view.activity.MainActivity;

public class NetworkChangedReceiver extends BroadcastReceiver {
    //TODO: Don't use this, use WorkManager instead
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        //FIXME Add a filter of IFs to check if the intent received is a good one
//        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(cm==null) return;
//        if(cm.getActiveNetworkInfo()!=null&&
//                cm.getActiveNetworkInfo().isConnectedOrConnecting()){
//            RazgoDataRepository dataRepository=RazgoDataRepository.getInstance(context);
//            dataRepository.setConversationListener(new ChangeListener<ConvDomain>() {
//                @Override
//                public void onNext(ConvDomain result) {
//                    notifyUser(context);
//                }
//                @Override
//                public void onComplete() {}
//                @Override
//                public void onError(Exception e) {
//                    Log.e("TheManagerLog","Error while NetworkChangedReceiver synced the razgos: "+e.getMessage());
//                }
//            });
//            dataRepository.sync();
//            //FIXME Start a service here instead. //Probably done now.
//        }
//    }


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //FIXME Make an intent filter here
        if(cm==null) return;
        if(cm.getActiveNetworkInfo()!=null&&
                cm.getActiveNetworkInfo().isConnectedOrConnecting()){
            context.startService(new Intent(context, SyncService.class));
            Log.d("TheManagerLog","NetworkChangedReceiver starting the SyncService now.");
        }
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
