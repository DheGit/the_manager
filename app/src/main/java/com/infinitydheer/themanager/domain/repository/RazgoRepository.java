package com.infinitydheer.themanager.domain.repository;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.listener.RazgoListener;

import java.util.List;

import io.reactivex.Observable;

public interface RazgoRepository {
    long addRazgo(long convid, RazgoDomain razgoDomain, boolean internet);
    void deleteRazgo(long id, long convid);
    void destroyData();
    Observable<List<ConvDomain>> getConversations();
    void getConvid(String partnerName, ChangeListener<Long> listener);
    String getPartnerId(long convid);
    Observable<List<RazgoDomain>> getRazgos(long convid, long endid);
    String getSelfId();
    void removeUpdateListener();
    void setConversationListener(ChangeListener<ConvDomain> conversationListener);
    void setRazgoListener(ChangeListener<RazgoDomain> razgoListener);
    void setMainListener(RazgoListener razgoListener);
    void setSelfId(String id);
    void setUpdateListener();
    void sync();
    void syncUserData(int limit);
}
