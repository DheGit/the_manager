package com.infinitydheer.themanager.presentation;

import com.infinitydheer.themanager.domain.executor.PostExecutionThread;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class UIThread implements PostExecutionThread {
    private static UIThread sInstance;

    private UIThread(){}

    public static synchronized UIThread getInstance(){
        if(sInstance ==null) sInstance =new UIThread();
        return sInstance;
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
