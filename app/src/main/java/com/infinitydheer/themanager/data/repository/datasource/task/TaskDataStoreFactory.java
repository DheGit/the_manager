package com.infinitydheer.themanager.data.repository.datasource.task;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Factory to return the appropriate {@link TaskDataStore} implementation depending upon the conditions
 */
public class TaskDataStoreFactory {
    private WeakReference<Context> mContext;

    public TaskDataStoreFactory(Context c){
        this.mContext =new WeakReference<>(c);
    }

    public TaskDataStore create(){
        Context c=mContext.get();

        if(c!=null) return DiskTaskDataStore.getInstance(c);
        else return null;
    }
}
