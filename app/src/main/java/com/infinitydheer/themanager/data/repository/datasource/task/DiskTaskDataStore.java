package com.infinitydheer.themanager.data.repository.datasource.task;

import android.content.Context;

import com.infinitydheer.themanager.data.entity.TaskEntity;
import com.infinitydheer.themanager.data.repository.datasource.base.FileManager;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;

/**
 * Helper class to manage the CRUD operations related to the Tasks
 */
public class DiskTaskDataStore implements TaskDataStore{
    private static DiskTaskDataStore sInstance;
    private File mDataDirectory;
    private FileManager mFileManager;
    private TaskDBManager mDbManager;

    private final String FILE_PREFIX="task_";

    private DiskTaskDataStore(Context context){
        this.mDbManager =TaskDBManager.getInstance(context);
        this.mDataDirectory =context.getFilesDir();
        this.mFileManager =new FileManager();
    }

    public static synchronized DiskTaskDataStore getInstance(Context context){
        if(sInstance ==null){
            sInstance =new DiskTaskDataStore(context);
        }
        return sInstance;
    }

    /**
     * Method to write a new Task to the App directory and DataBase
     * @param entity The task to be written
     */
    @Override
    public void addTask(TaskEntity entity) {
        long id= mDbManager.addTask(entity.getName(),entity.getQuad(),entity.getDueDate());
        if(id!=-1){
            File targetFile=buildFile(id);
            mFileManager.writeToFile(targetFile, entity.getNote());
        }
    }

    /**
     * Gets a task with all the associated data
     * @param id The ID of the task required
     * @return An {@link Observable} which emits the {@link TaskEntity}
     */
    @Override
    public Observable<TaskEntity> getTask(final long id) {
        return Observable.create(e -> {
            TaskEntity result= mDbManager.getTask(id);
            File taskFile=buildFile(id);
            String note= mFileManager.readFromFile(taskFile);
            if(result!=null){
                result.setNote(note);
                e.onNext(result);
                e.onComplete();
            }else{
                e.onError(new Exception());
            }
        });
    }

    @Override
    public void deleteTask(long id) {
        mDbManager.removeTask(id);
        File taskFile=buildFile(id);
        mFileManager.deleteFile(taskFile);
    }

    /**
     * Gets the number of tasks belonging to a specific quadrant
     * @param quad The quadrant. Pass only 1,2,3 or 4 here
     * @return Number of tasks in a quadrant. Returns -1 if an error occurred
     */
    @Override
    public long getNumTasks(int quad) {
        if(quad>4||quad<1) return -1;
        return mDbManager.getNumTasksOf(quad);
    }

    /**
     * Gets all the tasks with their data of a specific Quadrant
     * @param quad The quadrant number. Pass only 1,2,3 or 4 here
     * @return An {@link Observable} which emits the {@link List} of tasks
     */
    @Override
    public Observable<List<TaskEntity>> getTasks(final int quad) {
        return Observable.create(e -> {
            List<TaskEntity> entities= mDbManager.getTasksOf(quad);
            if(entities!=null){
                e.onNext(entities);
                e.onComplete();
            }else{
                e.onError(new Exception());
            }
        });
    }

    /**
     * Flips the status of the status(Complete or Incomplete)
     * @param id ID of the task whose status has to be flipped
     */
    @Override
    public void flipDone(long id) {
        mDbManager.flipDone(id);
    }

    /**
     * Updates a task. Creates one if a task with the ID in the {@link TaskEntity} passed does not exist
     * @param entity The content of the task
     */
    @Override
    public void updateTask(TaskEntity entity) {
        mDbManager.updateTask(entity.getId(), entity.getName(), entity.getQuad(), entity.getDueDate());
        File target=buildFile(entity.getId());
        mFileManager.writeToFile(target, entity.getNote());
    }

    /**
     * Builds a {@link File} object for a task with the corresponding ID
     * @param id ID of the task whose file is to be built
     * @return The File object
     */
    private File buildFile(long id){
        String fileNameBuilder = this.mDataDirectory.getPath() +
                File.separator +
                FILE_PREFIX +
                id;
        return new File(fileNameBuilder);
    }

}
