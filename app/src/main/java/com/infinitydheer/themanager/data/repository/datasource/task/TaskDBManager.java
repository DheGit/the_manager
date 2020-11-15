package com.infinitydheer.themanager.data.repository.datasource.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.infinitydheer.themanager.data.entity.TaskEntity;
import com.infinitydheer.themanager.data.repository.datasource.base.DBHelper;
import com.infinitydheer.themanager.domain.constants.OfflineTableConstants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class which uses {@link DBHelper} to manage CRUD operations on everything other than the Note
 */
public class TaskDBManager {
    private static TaskDBManager sInstance;

    private SQLiteDatabase mDb;

    private TaskDBManager(Context context){
        this.mDb = DBHelper.getInstance(context).getWritableDatabase();
    }

    public static synchronized TaskDBManager getInstance(Context context) {
        if(sInstance ==null){
            sInstance =new TaskDBManager(context);
        }
        return sInstance;
    }

    public long addTask(String name, int quad, String dueDate){
        ContentValues cv=new ContentValues();
        cv.put(TaskMeta.COL_NAME,name);
        cv.put(TaskMeta.COL_QUAD,quad);
        cv.put(TaskMeta.COL_DONE,0);
        cv.put(TaskMeta.COL_DUE_DATE,dueDate);
        return mDb.insert(TaskMeta.TABLE_NAME, null, cv);
    }

    public int removeTask(long id){
        return mDb.delete(TaskMeta.TABLE_NAME, TaskMeta._ID+"="+id,null);
    }

    public void updateTask(long id, String name, int quad, String dueDate){
        ContentValues cv=new ContentValues();
        cv.put(TaskMeta.COL_DUE_DATE, dueDate);
        cv.put(TaskMeta.COL_QUAD, quad);
        cv.put(TaskMeta.COL_NAME, name);
        cv.put(TaskMeta.COL_DONE, 0);
        mDb.update(TaskMeta.TABLE_NAME, cv, TaskMeta._ID+"="+id, null);
    }

    /**
     * Removes tasks with the given IDs
     * @param ids {@link List} of Task IDs
     */
    public void removeTasks(List<Integer> ids){
        mDb.beginTransaction();
        try{
            for(int x: ids){
                mDb.delete(TaskMeta.TABLE_NAME, TaskMeta._ID+"="+x, null);
            }
            mDb.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            mDb.endTransaction();
        }
    }

    public TaskEntity getTask(long id){
        TaskEntity entity=new TaskEntity();
        String[] col={TaskMeta._ID,
                TaskMeta.COL_NAME,
                TaskMeta.COL_QUAD,
                TaskMeta.COL_DONE,
                TaskMeta.COL_DUE_DATE};
        Cursor c= mDb.query(TaskMeta.TABLE_NAME, col, TaskMeta._ID+"="+id,
                null,null,null,null);
        c.moveToNext();
        entity.setId(c.getInt(c.getColumnIndexOrThrow(TaskMeta._ID)));
        entity.setDone(c.getInt(c.getColumnIndexOrThrow(TaskMeta.COL_DONE)));
        entity.setDueDate(c.getString(c.getColumnIndexOrThrow(TaskMeta.COL_DUE_DATE)));
        entity.setName(c.getString(c.getColumnIndexOrThrow(TaskMeta.COL_NAME)));
        entity.setQuad(c.getInt(c.getColumnIndexOrThrow(TaskMeta.COL_QUAD)));
        entity.setNote(null);
        c.close();
        return entity;
    }

    public void flipDone(long id){
        TaskEntity entity=getTask(id);
        entity.setDone(1-entity.isDone());
        ContentValues cv=new ContentValues();
        cv.put(TaskMeta.COL_NAME,entity.getName());
        cv.put(TaskMeta.COL_QUAD,entity.getQuad());
        cv.put(TaskMeta.COL_DONE,entity.isDone());
        cv.put(TaskMeta.COL_DUE_DATE,entity.getDueDate());
        mDb.update(TaskMeta.TABLE_NAME, cv, TaskMeta._ID+"="+id, null);
    }

    public List<TaskEntity> getTasksOf(int quad){
        List<TaskEntity> res=new ArrayList<>();
        String[] col={TaskMeta._ID,
        TaskMeta.COL_NAME,
        TaskMeta.COL_QUAD,
        TaskMeta.COL_DONE,
        TaskMeta.COL_DUE_DATE};
        Cursor c= mDb.query(TaskMeta.TABLE_NAME, col, TaskMeta.COL_QUAD+"="+quad,
                null,null,null,null);
        while(c.moveToNext()){
            TaskEntity entity=new TaskEntity();
            entity.setId(c.getInt(c.getColumnIndexOrThrow(TaskMeta._ID)));
            entity.setDone(c.getInt(c.getColumnIndexOrThrow(TaskMeta.COL_DONE)));
            entity.setDueDate(c.getString(c.getColumnIndexOrThrow(TaskMeta.COL_DUE_DATE)));
            entity.setName(c.getString(c.getColumnIndexOrThrow(TaskMeta.COL_NAME)));
            entity.setQuad(quad);
            entity.setNote(null);
            res.add(entity);
        }
        c.close();
        return res;
    }

    public long getNumTasksOf(int quad){
        return DatabaseUtils.queryNumEntries(mDb, TaskMeta.TABLE_NAME, TaskMeta.COL_QUAD+"="+quad);
    }
}
