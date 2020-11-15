package com.infinitydheer.themanager.data.repository.datasource.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.annotation.Nullable;

import androidx.annotation.Nullable;

import com.infinitydheer.themanager.domain.constants.OfflineTableConstants.*;

/**
 * Class to manage the basic level operations such as create and update of the local DB
 * Implementation of {@link SQLiteDatabase}
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sInstance;

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="TheManager.db";

    //Task Database statements
    private static final String CREATE_TASK_META="CREATE TABLE "+ TaskMeta.TABLE_NAME + " ("+
            TaskMeta._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            TaskMeta.COL_NAME + " TEXT NOT NULL,"+
            TaskMeta.COL_QUAD + " INTEGER NOT NULL," +
            TaskMeta.COL_DONE + " INTEGER," +
            TaskMeta.COL_DUE_DATE + " TEXT)";
    private static final String DELETE_TASK_META="DROP TABLE IF EXISTS "+TaskMeta.TABLE_NAME;

    //RazgoOverview Database statements
    private static final String CREATE_RAZGO_OV="CREATE TABLE "+RazgoOV.TABLE_NAME+" ("+
            RazgoOV._ID + " INTEGER PRIMARY KEY NOT NULL,"+
            RazgoOV.COL_LAST + " TEXT,"+
            RazgoOV.COL_LASTTIME + " TEXT,"+
            RazgoOV.COL_LAST_SENDER+ " TEXT,"+
            RazgoOV.COL_K + " INTEGER,"+
            RazgoOV.COL_PARTNER + " TEXT NOT NULL)";
    private static final String DELETE_RAZGO_OV="DROP TABLE IF EXISTS "+ RazgoOV.TABLE_NAME;

    //RazgoUpdate Database statements
    private static final String CREATE_UPDATE_TABLE="CREATE TABLE "+RazgoUpdates.TABLE_NAME+" ("+
            RazgoUpdates._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            RazgoUpdates.COL_CONVID+" INTEGER NOT NULL,"+
            RazgoUpdates.COL_CONTENT+" TEXT NOT NULL,"+
            RazgoUpdates.COL_DATETIME+" TEXT NOT NULL,"+
            RazgoUpdates.COL_K+" LONG NOT NULL)";
    private static final String DELETE_UPDATE_TABLE="DROP TABLE IF EXISTS "+RazgoUpdates.TABLE_NAME;

    private DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
    *getInstance: Makes the class Singleton
    * @param context: Context in which the database operates
    * @return sInstance: Instance of the helper class
    */
    public static synchronized DBHelper getInstance(Context context){
        if(sInstance ==null){
            sInstance =new DBHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TASK_META);
        sqLiteDatabase.execSQL(CREATE_RAZGO_OV);
        sqLiteDatabase.execSQL(CREATE_UPDATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TASK_META);
        sqLiteDatabase.execSQL(DELETE_RAZGO_OV);
        sqLiteDatabase.execSQL(DELETE_UPDATE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
