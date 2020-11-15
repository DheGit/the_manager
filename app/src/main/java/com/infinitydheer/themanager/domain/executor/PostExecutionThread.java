package com.infinitydheer.themanager.domain.executor;

import io.reactivex.Scheduler;

/**
 * Interface to manage the results to be given after an Observable completes work
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
