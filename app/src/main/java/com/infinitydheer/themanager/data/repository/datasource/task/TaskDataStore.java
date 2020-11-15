package com.infinitydheer.themanager.data.repository.datasource.task;

import com.infinitydheer.themanager.data.entity.TaskEntity;

import java.util.List;

import io.reactivex.Observable;

public interface TaskDataStore {
    void addTask(TaskEntity entity);
    void deleteTask(long id);
    long getNumTasks(int quad);
    Observable<TaskEntity> getTask(long id);
    Observable<List<TaskEntity> > getTasks(int quad);
    void flipDone(long id);
    void updateTask(TaskEntity entity);
}
