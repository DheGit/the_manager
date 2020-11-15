package com.infinitydheer.themanager.data.repository;

import android.content.Context;

import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.RazgoEntity;
import com.infinitydheer.themanager.data.entity.mapper.ConvEntityDataMapper;
import com.infinitydheer.themanager.data.entity.mapper.RazgoEntityDataMapper;
import com.infinitydheer.themanager.data.repository.datasource.razgo.RazgoMainDataStore;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.listener.RazgoListener;
import com.infinitydheer.themanager.domain.repository.RazgoRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Implementation of {@link RazgoRepository} to manage the Razgo data
 * This class maps all data to the domain layer data objects for better separation of concerns and ease of testing
 */
public class RazgoDataRepository implements RazgoRepository {
    private static RazgoDataRepository sInstance;
    private RazgoMainDataStore mDataStore;
    private RazgoEntityDataMapper mRazgoMapper;
    private ConvEntityDataMapper mConvMapper;


    private RazgoDataRepository(Context context){
        this.mDataStore = RazgoMainDataStore.getInstance(context);
        this.mRazgoMapper =new RazgoEntityDataMapper();
        this.mConvMapper =new ConvEntityDataMapper();
    }

    public static synchronized RazgoDataRepository getInstance(Context context){
        if(sInstance ==null) sInstance =new RazgoDataRepository(context);
        return sInstance;
    }

    @Override
    public void sync() {
        this.mDataStore.sync();
    }

    @Override
    public long addRazgo(long convid, RazgoDomain razgoDomain, boolean connectedToInternet) {
        RazgoEntity entity=this.mRazgoMapper.transformToEntity(razgoDomain);
        return this.mDataStore.addRazgo(convid,entity,connectedToInternet);
    }

    @Override
    public void deleteRazgo(long id, long convid) {
        this.mDataStore.deleteRazgo(id,convid);
    }

    @Override
    public Observable<List<ConvDomain>> getConversations() {
        Observable<List<ConvEntity>> sres=this.mDataStore.getConversations();
        return sres.map(this.mConvMapper::transform);
    }

    @Override
    public void destroyData() {
        this.mDataStore.destroyData();
    }

    @Override
    public void getConvid(String partnerName, ChangeListener<Long> listener) {
        this.mDataStore.getConvid(partnerName,listener);
    }

    @Override
    public String getPartnerId(long convid) {
        return this.mDataStore.getPartnerId(convid);
    }

    @Override
    public void syncUserData(int lim) {
        this.mDataStore.getParticipatingConv(lim);
    }

    @Override
    public Observable<List<RazgoDomain>> getRazgos(long convid, long endid) {
        Observable<List<RazgoEntity>> sres=this.mDataStore.getRazgos(endid,convid);
        return sres.map(this.mRazgoMapper::transform);
    }

    @Override
    public void setSelfId(String id) {
        this.mDataStore.setSelfId(id);
    }

    @Override
    public String getSelfId() {
        return this.mDataStore.getUserId();
    }

    @Override
    public void setRazgoListener(ChangeListener<RazgoDomain> razgoListener) {
        this.mDataStore.setRazgoChangeListener(razgoListener);
    }

    @Override
    public void setMainListener(RazgoListener razgoListener) {
        mDataStore.setMainChangeListener(razgoListener);
    }

    @Override
    public void setConversationListener(ChangeListener<ConvDomain> conversationListener) {
        this.mDataStore.setOvChangeListener(conversationListener);
    }

    @Override
    public void removeUpdateListener() {
        this.mDataStore.removeUpdateListener();
    }

    @Override
    public void setUpdateListener() {
        this.mDataStore.setUpdateListener();
    }
}
