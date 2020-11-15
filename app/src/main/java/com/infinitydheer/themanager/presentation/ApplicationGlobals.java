package com.infinitydheer.themanager.presentation;

import android.app.Application;
import android.util.Log;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.repository.RazgoRepository;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;

public class ApplicationGlobals extends Application {
    public static String SELF_ID="";
    public static String SELF_ID_UP="";

    private RazgoRepository mRazgoRepository;

    private NotificationUtils mNotificationUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        if(S.Constants.LOG_ENABLED) Log.d(S.Constants.LOG_ID, "onCreate: Application created");
        mRazgoRepository = RazgoDataRepository.getInstance(this);
        mNotificationUtils = NotificationUtils.getInstance(this);

        mNotificationUtils.clearAllNotifications();

        SELF_ID= mRazgoRepository.getSelfId();
        SELF_ID_UP= Nkrpt.unprocessDef(SELF_ID);

        mRazgoRepository.sync();
        mRazgoRepository.setUpdateListener();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if(S.Constants.LOG_ENABLED) Log.d(S.Constants.LOG_ID, "onTerminate: Application Terminated");
        mRazgoRepository.removeUpdateListener();
    }

    public int getQuadColor(int quad){
        return getResources().getColor(getQuadColorId(quad));
    }
    public int getQuadColorId(int quad){
        switch (quad){
            case 1: return R.color.colorOne;
            case 2: return R.color.colorTwo;
            case 3: return R.color.colorThree;
            case 4: return R.color.colorFour;
            default: return R.color.colorDistinct;
        }
    }
}
