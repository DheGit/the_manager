package com.infinitydheer.themanager.data.repository.datasource.razgo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.RazgoEntity;
import com.infinitydheer.themanager.data.repository.datasource.base.DBHelper;
import com.infinitydheer.themanager.domain.constants.S.*;
import com.infinitydheer.themanager.domain.constants.OfflineTableConstants.*;
import com.infinitydheer.themanager.domain.utils.CommonUtils;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;

import java.util.ArrayList;
import java.util.List;

public class RazgoOfflineDataStore {
    private static final String QUER_PRE_CONV = "CREATE TABLE IF NOT EXISTS conv_";
    private static final String QUER_POST_CONV = " (" + RazgoOne._ID + " INTEGER PRIMARY KEY," +
            RazgoOne.COL_CONTENT + " TEXT NOT NULL," +
            RazgoOne.COL_SENDER + " TEXT," +
            RazgoOne.COL_DT + " TEXT NOT NULL," +
            RazgoOne.COL_K + " INTEGER NOT NULL)";
    private static final String C_PREFIX = "conv_";

    private SQLiteDatabase mOfflineDb;

    RazgoOfflineDataStore(Context context) {
        this.mOfflineDb = DBHelper.getInstance(context).getWritableDatabase();
    }

    void addRazgoConv(long id, String partnerName) {
        if (conversationExists(id)) return;
        mOfflineDb.execSQL(QUER_PRE_CONV + id + QUER_POST_CONV);
        addToOV(new ConvEntity(partnerName, id));
    }

    private void addToOV(ConvEntity entity) {
        ContentValues cv = createRazgoOvCv(entity.getConvid(),entity.getLastSender(),
                entity.getLastTime(),entity.getLastMsg(),entity.getK(),
                entity.getPartnerName());

        mOfflineDb.insert(RazgoOV.TABLE_NAME, null, cv);
    }
    List<ConvEntity> getConversations() {
        String[] col_req = {RazgoOV.COL_LAST,
                RazgoOV.COL_LASTTIME,
                RazgoOV.COL_PARTNER,
                RazgoOV._ID,
                RazgoOV.COL_LAST_SENDER,
                RazgoOV.COL_K};

        Cursor c = mOfflineDb.query(RazgoOV.TABLE_NAME, col_req, null,
                null, null, null, null);
        List<ConvEntity> res = new ArrayList<>();

        while (c.moveToNext()) {
            ConvEntity newC = new ConvEntity();
            if (c.getLong(c.getColumnIndexOrThrow(RazgoOV._ID)) == -1) continue;

            newC.setConvid(c.getLong(c.getColumnIndexOrThrow(RazgoOV._ID)));
            newC.setPartnerName(c.getString(c.getColumnIndexOrThrow(RazgoOV.COL_PARTNER)));
            newC.setLastTime(c.getString(c.getColumnIndexOrThrow(RazgoOV.COL_LASTTIME)));
            newC.setLastSender(c.getString(c.getColumnIndexOrThrow(RazgoOV.COL_LAST_SENDER)));
            newC.setLastMsg(c.getString(c.getColumnIndexOrThrow(RazgoOV.COL_LAST)));
            newC.setK(c.getInt(c.getColumnIndexOrThrow(RazgoOV.COL_K)));

            res.add(newC);
        }
        c.close();
        return res;
    }
    private boolean conversationExists(long convid) {
        Cursor c = mOfflineDb.query(RazgoOV.TABLE_NAME, new String[]{RazgoOV.COL_K},
                RazgoOV._ID + "=" + convid, null, null, null,
                null);

        if (c != null) {
            if (c.getCount() > 0) {
                return true;
            }
            c.close();
        }

        return false;
    }
    long getConvId(String partnerId){
        String[] col_req={RazgoOV._ID};
        Cursor c= mOfflineDb.query(RazgoOV.TABLE_NAME,col_req,
                RazgoOV.COL_PARTNER+"='"+partnerId+"'",null,null,
                null,null);

        if(c==null||c.getCount()==0) return 0;

        c.moveToNext();
        long res=c.getLong(c.getColumnIndexOrThrow(RazgoOV._ID));
        c.close();

        return res;
    }
    String getPartnerId(long convid) {
        String[] col_req = {RazgoOV.COL_PARTNER};
        Cursor c = mOfflineDb.query(RazgoOV.TABLE_NAME, col_req, RazgoOV._ID + "=" + convid,
                null, null, null, null);

        if (c == null || c.getCount() == 0) return "";

        c.moveToNext();
        String res = c.getString(c.getColumnIndexOrThrow(RazgoOV.COL_PARTNER));
        c.close();

        return res;
    }
    private void updateOV(long convid) {
        //First get from updates
        Cursor c = mOfflineDb.query(RazgoUpdates.TABLE_NAME,
                new String[]{RazgoUpdates.COL_CONTENT, RazgoUpdates.COL_DATETIME, RazgoUpdates.COL_K},
                RazgoUpdates.COL_CONVID + "=" + convid,
                null, null, null, RazgoUpdates._ID + " DESC", "1");

        String lastMsg, lastTime;
        String lastSender = ApplicationGlobals.SELF_ID;
        long k;

        if (c.getCount() == 0) { //If no updates, get from main
            Cursor c1 = mOfflineDb.query(C_PREFIX + convid,
                    new String[]{RazgoOne.COL_K, RazgoOne.COL_DT,
                            RazgoOne.COL_CONTENT, RazgoOne.COL_SENDER},
                    null, null, null, null,
                    RazgoOne._ID + " DESC", "1");

            c1.moveToNext();
            lastMsg = c1.getString(c1.getColumnIndexOrThrow(RazgoOne.COL_CONTENT));

            lastTime = c1.getString(c1.getColumnIndexOrThrow(RazgoOne.COL_DT));
            k = c1.getLong(c1.getColumnIndexOrThrow(RazgoOne.COL_K));
            lastSender = c1.getString(c1.getColumnIndexOrThrow(RazgoOne.COL_SENDER));

            c1.close();
        } else { //If updates exist, take that as the overview
            c.moveToNext();

            lastMsg = c.getString(c.getColumnIndexOrThrow(RazgoUpdates.COL_CONTENT));
            lastTime = c.getString(c.getColumnIndexOrThrow(RazgoUpdates.COL_DATETIME));
            k = c.getLong(c.getColumnIndexOrThrow(RazgoUpdates.COL_K));
        }
        c.close();

        ContentValues cv = createRazgoOvCv(-2,lastSender,lastTime,lastMsg,k,null);
        mOfflineDb.update(RazgoOV.TABLE_NAME, cv, RazgoOV._ID + "=" + convid, null);
    }

    long addRazgoToUpdates(long convid, RazgoEntity entity) {
        ContentValues cv = createRazgoUpdateCV(entity,convid);
        long rowid = mOfflineDb.insert(RazgoUpdates.TABLE_NAME, null, cv);
        updateOV(convid);
        return rowid;
    }
    List<RazgoEntity> getUpdatesFrom(long id) {
        String[] col_req = {RazgoUpdates.COL_K,
                RazgoUpdates.COL_DATETIME,
                RazgoUpdates.COL_CONTENT,
                RazgoUpdates._ID};

        String sender = ApplicationGlobals.SELF_ID;
        List<RazgoEntity> result = new ArrayList<>();

        Cursor c = mOfflineDb.query(RazgoUpdates.TABLE_NAME, col_req,
                RazgoUpdates.COL_CONVID + "=" + id,
                null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                RazgoEntity entity = new RazgoEntity();

                entity.setDatetime(c.getString(c.getColumnIndexOrThrow(RazgoUpdates.COL_DATETIME)));
                entity.setContent(c.getString(c.getColumnIndexOrThrow(RazgoUpdates.COL_CONTENT)));
                entity.setSender(sender);
                entity.setId(-(c.getLong(c.getColumnIndexOrThrow(RazgoUpdates._ID))));
                entity.setK((int) c.getLong(c.getColumnIndexOrThrow(RazgoUpdates.COL_K)));
                entity.setSent(false);

                result.add(entity);
            }
            c.close();
        }
        return result;
    }
    List<Long> getUpdateIds() {
        List<Long> res = new ArrayList<>();
        Cursor c = mOfflineDb.query(true, RazgoUpdates.TABLE_NAME,
                new String[]{RazgoUpdates.COL_CONVID}, null, null,
                null, null, null, null);

        while (c.moveToNext()) res.add(c.getLong(c.getColumnIndexOrThrow(RazgoUpdates.COL_CONVID)));

        c.close();
        return res;
    }
    void clearUpdates(long convid) {
        mOfflineDb.delete(RazgoUpdates.TABLE_NAME, RazgoUpdates.COL_CONVID + "=" + convid, null);
    }

    long getLastRazgoId(long convid){
        long res=0;

        try {
            Cursor c = mOfflineDb.query(C_PREFIX + convid,
                    new String[]{RazgoOne._ID}, null, null, null, null,
                    RazgoOne._ID + " DESC", "1");

            if(c.moveToNext()){
                res=c.getLong(c.getColumnIndexOrThrow(RazgoOne._ID));
            }

            c.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }
    List<RazgoEntity> getRazgos(long convid, long endid) {
        Cursor c;
        List<RazgoEntity> res = new ArrayList<>();
        if (endid == -1) {
            c = mOfflineDb.query(C_PREFIX + convid,
                    new String[]{RazgoOne._ID, RazgoOne.COL_CONTENT,
                            RazgoOne.COL_DT, RazgoOne.COL_K, RazgoOne.COL_SENDER},
                    null, null, null, null,
                    RazgoOne._ID + " DESC", "100");
        } else {
            c = mOfflineDb.query(C_PREFIX + convid,
                    new String[]{RazgoOne._ID, RazgoOne.COL_CONTENT, RazgoOne.COL_DT, RazgoOne.COL_K},
                    RazgoOne._ID + "<" + endid,
                    null, null, null,
                    RazgoOne._ID + " DESC", "100");
        }
        if (c != null) {
            if (c.getCount() != 0) {
                while (c.moveToNext()) {
                    RazgoEntity entity = new RazgoEntity();

                    entity.setId(c.getLong(c.getColumnIndexOrThrow(RazgoOne._ID)));
                    entity.setDatetime(c.getString(c.getColumnIndexOrThrow(RazgoOne.COL_DT)));
                    entity.setContent(c.getString(c.getColumnIndexOrThrow(RazgoOne.COL_CONTENT)));
                    entity.setSender(c.getString(c.getColumnIndexOrThrow(RazgoOne.COL_SENDER)));
                    entity.setK((int) c.getLong(c.getColumnIndexOrThrow(RazgoOne.COL_K)));

                    res.add(entity);
                }
            }
            c.close();
        }
        res = CommonUtils.reverseList(res);

        if(endid==-1) res.addAll(getUpdatesFrom(convid));
        return res;
    }
    private void addRazgoToMain(RazgoEntity entity, long convid){
        ContentValues cv = createRazgoOneCV(entity.getId(),entity.getDatetime(),
                entity.getSender(),entity.getContent(),entity.getK());
        try{
            mOfflineDb.insert(C_PREFIX + convid, null, cv);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(Constants.LOG_ID, "addRazgoToMain: ", e);
        }
    }
    void addRazgosToMain(List<RazgoEntity> entityList, long convid){
        for (RazgoEntity entity : entityList) {
            addRazgoToMain(entity,convid);
        }
        updateOV(convid);
    }
    void deleteRazgo(long id, long convid) {
        try{
            if (id < 0)
                mOfflineDb.delete(RazgoUpdates.TABLE_NAME, RazgoUpdates._ID + "=" + (-id), null);
            mOfflineDb.delete(C_PREFIX + convid, RazgoOne._ID + "=" + id, null);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(Constants.LOG_ID, "deleteRazgo: ",e );
        }
    }
    public void deleteRazgos(List<Long> ids, long convid) {
        for (long id : ids) {
            deleteRazgo(id,convid);
        }
        updateOV(convid);
    }

    void destroyEverything(){
        List<ConvEntity> convEntities=getConversations();

        for(ConvEntity entity:convEntities){
            long convid=entity.getConvid();
            mOfflineDb.execSQL("DROP TABLE IF EXISTS "+C_PREFIX+convid);
        }
        mOfflineDb.delete(RazgoOV.TABLE_NAME,null,null);
        mOfflineDb.delete(RazgoUpdates.TABLE_NAME,null,null);
    }

    //Methods dealing with the ID of the user working
    String getSelfId() {
        return getPartnerId(-1);
    }

    void setSelfId(String id) {
        ContentValues cv = createRazgoOvCv(-1,null,
                null,null,
                -1,id);

        long x = mOfflineDb.insert(RazgoOV.TABLE_NAME, null, cv);
        if (x == -1) mOfflineDb.update(RazgoOV.TABLE_NAME, cv,
                RazgoOV._ID + "=-1", null);
    }

    //Utility methods to make ContentValues
    //TODO Consider moving them to a new class, static
    private ContentValues createRazgoOneCV(long id, String date, String sender, String content, long k){
        ContentValues cv=new ContentValues();

        cv.put(RazgoOne._ID, id);
        cv.put(RazgoOne.COL_DT, date);
        cv.put(RazgoOne.COL_SENDER,sender);
        cv.put(RazgoOne.COL_CONTENT,content);
        cv.put(RazgoOne.COL_K,k);

        return cv;
    }

    private ContentValues createRazgoOvCv(long convid, String lastSender,
                                          String lastTime,
                                          String lastMessage,
                                          long k, String partner){
        ContentValues cv=new ContentValues();

        if(convid>=-1) cv.put(RazgoOV._ID, convid);
        if(lastSender!=null) cv.put(RazgoOV.COL_LAST_SENDER, lastSender);
        if(lastTime!=null) cv.put(RazgoOV.COL_LASTTIME, lastTime);
        if(lastMessage!=null) cv.put(RazgoOV.COL_LAST,lastMessage);
        if(k>0&&k<104) cv.put(RazgoOV.COL_K, k);
        if(partner!=null) cv.put(RazgoOV.COL_PARTNER, partner);

        return cv;
    }

    private ContentValues createRazgoUpdateCV(RazgoEntity e, long convid){
        ContentValues cv=new ContentValues();

        cv.put(RazgoUpdates.COL_CONTENT,e.getContent());
        cv.put(RazgoUpdates.COL_K,e.getK());
        cv.put(RazgoUpdates.COL_DATETIME,e.getDatetime());
        cv.put(RazgoUpdates.COL_CONVID,convid);

        return cv;
    }

}