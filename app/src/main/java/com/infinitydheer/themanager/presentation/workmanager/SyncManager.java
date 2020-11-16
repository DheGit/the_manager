package com.infinitydheer.themanager.presentation.workmanager;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.interactor.razgo.UCSync;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;
import com.infinitydheer.themanager.presentation.view.activity.MainActivity;

import java.lang.ref.WeakReference;

public class SyncManager extends ListenableWorker {
    private final WeakReference<Context> mContext;

    public SyncManager(@NonNull Context context, @NonNull WorkerParameters params){
        super(context,params);
        this.mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Context context=this.mContext.get();
        if (context==null) return null;
        return CallbackToFutureAdapter.getFuture(completer -> {
            ChangeListener<ConvDomain> changeListener=new ChangeListener<ConvDomain>() {
                @Override
                public void onNext(ConvDomain result) {
                    SyncManager.this.notifyUser(context); //TODO: Consider removing this one
                }
                @Override
                public void onComplete() {
                    SyncManager.this.notifyUser(context);
                    completer.set(Result.success());
                }
                @Override
                public void onError(Exception e) {
                    completer.setException(e);
                }
            };

            UCSync interactor=new UCSync(RazgoDataRepository.getInstance(context),changeListener);
            interactor.sync();
            return changeListener;
        });
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
