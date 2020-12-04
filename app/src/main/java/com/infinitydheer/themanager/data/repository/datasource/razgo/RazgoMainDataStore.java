package com.infinitydheer.themanager.data.repository.datasource.razgo;

import android.content.Context;
import android.util.Log;

import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.RazgoEntity;
import com.infinitydheer.themanager.data.entity.mapper.ConvEntityDataMapper;
import com.infinitydheer.themanager.data.entity.mapper.RazgoEntityDataMapper;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.listener.RazgoListener;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;
import static com.infinitydheer.themanager.domain.constants.S.Constants;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Observable;

public class RazgoMainDataStore implements RazgoOnlineDataStore.OnlineMainListener {
    private static RazgoMainDataStore sInstance;

    private final WeakReference<Context> mContext;

    private ChangeListener<ConvDomain> mConvChangeListener;
    private ChangeListener<RazgoDomain> mRazgoChangeListener;
    private RazgoListener mMainChangeListener;

    private final ConvEntityDataMapper mConvDataMapper;
    private final RazgoEntityDataMapper mRazgoDataMapper;

    private final RazgoOnlineDataStore mOnlineDataStore;
    private final RazgoOfflineDataStore mOfflineDataStore;


    private RazgoMainDataStore(Context context){
        this.mOnlineDataStore = RazgoOnlineDataStore.getInstance(this);
        this.mOfflineDataStore =new RazgoOfflineDataStore(context);

        this.mConvDataMapper =new ConvEntityDataMapper();
        this.mRazgoDataMapper =new RazgoEntityDataMapper();

        this.mOnlineDataStore.setParentDS(this);

        this.mContext=new WeakReference<Context>(context);
    }

    public static synchronized RazgoMainDataStore getInstance(Context context){
        if(sInstance ==null) sInstance =new RazgoMainDataStore(context);
        return sInstance;
    }


    @Override
    public void addConversation(ConvEntity entity) {
        this.mOfflineDataStore.addRazgoConv(entity.getConvid(),entity.getPartnerName());
    }

    @Override
    public void addRazgosToMain(List<RazgoEntity> entityList, long convid){
        this.mOfflineDataStore.addRazgosToMain(entityList,convid);
    }

    @Override
    public void addRazgosToOfflineDB(List<RazgoEntity> entities, long convid) {
        String senderId=this.mOfflineDataStore.getPartnerId(convid);

        for(RazgoEntity entity:entities) entity.setSender(senderId);
        this.addRazgosToMain(entities,convid);
    }

    @Override
    public void onConvReceived(ConvEntity convEntity) {
        if(convEntity==null) return;

//        notifyUser();

        if(this.mConvChangeListener ==null) return;

        ConvDomain convDomain=this.mConvDataMapper.transform(convEntity);
        this.mConvChangeListener.onNext(convDomain);
    }
    @Override
    public void onConvReceived(ConvEntity convEntity, boolean completed) {
        if(this.mConvChangeListener ==null) return;
        if(convEntity==null){
            if(completed) onSyncDone();
            return;
        }

//        notifyUser();

        ConvDomain convDomain=this.mConvDataMapper.transform(convEntity);
        this.mConvChangeListener.onNext(convDomain);
        if(completed) onSyncDone();
    }

    @Override
    public void onSyncDone() {
        this.mConvChangeListener.onComplete();
    }

    @Override
    public void onRazgoReceived(RazgoEntity razgoEntity) {
        if(razgoEntity==null) return;
//        if(this.mRazgoChangeListener ==null){
//            if(Constants.LOG_ENABLED) Log.d(Constants.LOG_ID, "RazgoListener is null");
//            return;
//        }
        if(this.mMainChangeListener==null) return;

        if(Constants.LOG_ENABLED) Log.d(Constants.LOG_ID, "onRazgoReceived: Calling back razgo: "+razgoEntity.toString());

        RazgoDomain razgoDomain=this.mRazgoDataMapper.transform(razgoEntity);
//        this.mRazgoChangeListener.onNext(razgoDomain);
        this.mMainChangeListener.onRazgoRecieved(razgoDomain);
    }
    @Override
    public void onRazgosSent(List<Long> oldIds, List<Long> newIds) {
        if(mMainChangeListener==null) return;

        for(int i=0;i<oldIds.size();i++){
            mMainChangeListener.onRazgoSent(oldIds.get(i),newIds.get(i));
        }
    }

    @Override
    public String getPartnerId(long convid) {
        return this.mOfflineDataStore.getPartnerId(convid);
    }

    @Override
    public List<Long> getUpdateIds() {
        return this.mOfflineDataStore.getUpdateIds();
    }

    @Override
    public String getUserId() {
        return this.mOfflineDataStore.getSelfId();
    }

    @Override
    public List<RazgoEntity> getUpdateRazgos(long id) {
        return this.mOfflineDataStore.getUpdatesFrom(id);
    }

    @Override
    public long getLastRazgoId(long convid) {
        return mOfflineDataStore.getLastRazgoId(convid);
    }

    @Override
    public void markUpdates(List<RazgoEntity> entityList, long convid) {
        String selfId= ApplicationGlobals.SELF_ID;
        for(RazgoEntity entity: entityList) entity.setSender(selfId);
        this.addRazgosToMain(entityList,convid);
        this.mOfflineDataStore.clearUpdates(convid);
    }

    public long addRazgo(long convid, RazgoEntity entity, boolean internetAvailable){
        long id=this.mOfflineDataStore.addRazgoToUpdates(convid,entity);
        if(internetAvailable) this.mOnlineDataStore.syncPut();

        return id;
    }

    public void deleteRazgo(long id,long convid){
        this.mOfflineDataStore.deleteRazgo(id,convid);
    }

    public void destroyData(){
        this.mOfflineDataStore.destroyEverything();
    }

    public void getConvid(String partnerName, ChangeListener<Long> changeListener){
        long convid=this.mOfflineDataStore.getConvId(partnerName);

        if(convid!=0) {
            changeListener.onNext(convid); changeListener.onComplete();
        }
        else{
            this.mOnlineDataStore.getConvId(partnerName,changeListener);
        }
    }

    public Observable<List<ConvEntity>> getConversations(){
        return Observable.create(emmitter -> {
            List<ConvEntity> sres = RazgoMainDataStore.this.mOfflineDataStore.getConversations();
            if (sres != null) {
                emmitter.onNext(sres);
                emmitter.onComplete();
            } else {
                emmitter.onError(new Exception("The Conversations could not be loaded"));
            }
        });
    }

    public void getParticipatingConv(int limit){
        this.mOnlineDataStore.getParticipatingConv(limit);
    }

    public Observable<List<RazgoEntity>> getRazgos(long endid, long convid){
        return Observable.create(emmitter -> {
            List<RazgoEntity> sres=RazgoMainDataStore.this.mOfflineDataStore.getRazgos(convid,endid);
            if(sres!=null){
                emmitter.onNext(sres);
                emmitter.onComplete();
            }else{
                emmitter.onError(new Exception("The Razgos could not be loaded"));
            }
        });
    }

    public void removeUpdateListener(){
        this.mOnlineDataStore.removeUpdateListener();
    }

    public void setOvChangeListener(ChangeListener<ConvDomain> listener){
        this.mConvChangeListener =listener;
    }

    public void setRazgoChangeListener(ChangeListener<RazgoDomain> listener){
        this.mRazgoChangeListener =listener;
    }

    public void setMainChangeListener(RazgoListener mainChangeListener){
        this.mMainChangeListener=mainChangeListener;
    }

    public void setSelfId(String id){
        this.mOfflineDataStore.setSelfId(id);
    }

    public void setUpdateListener(){
        this.mOnlineDataStore.setUpdateListener();
    }

    public void sync(){
        this.mOnlineDataStore.syncGetAll(true);
    }
}
