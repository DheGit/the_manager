package com.infinitydheer.themanager.data.executor;

//import android.support.annotation.NonNull;

import androidx.annotation.NonNull;

import com.infinitydheer.themanager.domain.executor.ThreadExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the {@link ThreadExecutor} in the domain layer for multi-threading
 * Has only one sInstance per Application state
 */
public class WorkExecutor implements ThreadExecutor {
    private static WorkExecutor sInstance;
    private ThreadPoolExecutor mPoolExecutor;

    private WorkExecutor(){
        this.mPoolExecutor =new ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(), new WorkThreadFactory());
    }

    public static synchronized WorkExecutor getInstance(){
        if(sInstance ==null){
            sInstance =new WorkExecutor();
        }
        return sInstance;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        this.mPoolExecutor.execute(runnable);
    }

    /**
     * Implementation of {@link ThreadFactory} to directly start and manage threads
     */
    private static class WorkThreadFactory implements ThreadFactory {
        private int counter=0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "manager_"+counter++);
        }
    }
}
