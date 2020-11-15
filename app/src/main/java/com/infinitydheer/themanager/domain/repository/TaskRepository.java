package com.infinitydheer.themanager.domain.repository;

import com.infinitydheer.themanager.domain.data.TaskDomain;

import java.util.List;

import io.reactivex.Observable;

public interface TaskRepository {
    void addTask(TaskDomain td);
    void removeTask(long id);
    void removeTasks(List<Long> idList);
    Observable<TaskDomain> getTask(long id);
    Observable<List<TaskDomain> > getTasks(int quad);
    long getNumTasks(int quad);
    void flipDone(long id);
    void updateTask(TaskDomain taskDomain);
}
