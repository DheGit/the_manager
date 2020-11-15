package com.infinitydheer.themanager.data.repository;

import com.infinitydheer.themanager.data.entity.TaskEntity;
import com.infinitydheer.themanager.data.entity.mapper.TaskDataEntityMapper;
import com.infinitydheer.themanager.data.repository.datasource.task.TaskDataStore;
import com.infinitydheer.themanager.data.repository.datasource.task.TaskDataStoreFactory;
import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;
import com.infinitydheer.themanager.domain.repository.TaskRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Implementation of {@link TaskRepository} to manage the data of the Tasks
 * Maps everything to and from the domain data objects for separation of concerns and ease of testing
 */
public class TaskDataRepository implements TaskRepository {
    private TaskDataStoreFactory mStoreFactory;
    private TaskDataStore mDataStore;
    private TaskDataEntityMapper mTaskMapper;
    private ThreadExecutor mExecutor;

    public TaskDataRepository(TaskDataStoreFactory tdsf, ThreadExecutor executor){
        this.mStoreFactory =tdsf;
        mDataStore =this.mStoreFactory.create();
        this.mTaskMapper =new TaskDataEntityMapper();
        this.mExecutor =executor;
    }


    @Override
    public void addTask(TaskDomain td) {
        TaskEntity te= mTaskMapper.transformToEntity(td);
        mDataStore.addTask(te);
    }

    @Override
    public void removeTask(long id) {
        mDataStore.deleteTask(id);
    }

    @Override
    public void removeTasks(List<Long> idList) {
        for(long id: idList){
            removeTask(id);
        }
    }

    @Override
    public Observable<TaskDomain> getTask(long id) {
        Observable<TaskEntity> sres= mDataStore.getTask(id);
        return sres.map(this.mTaskMapper::transform);
    }

    @Override
    public Observable<List<TaskDomain>> getTasks(int quad) {
        Observable<List<TaskEntity>> sres=this.mDataStore.getTasks(quad);
        return sres.map(this.mTaskMapper::transform);
    }

    @Override
    public long getNumTasks(int quad) {
        return mDataStore.getNumTasks(quad);
    }

    @Override
    public void flipDone(long id) {
        mDataStore.flipDone(id);
    }

    @Override
    public void updateTask(TaskDomain taskDomain) {
        TaskEntity entity= mTaskMapper.transformToEntity(taskDomain);
        this.mDataStore.updateTask(entity);
    }

}
