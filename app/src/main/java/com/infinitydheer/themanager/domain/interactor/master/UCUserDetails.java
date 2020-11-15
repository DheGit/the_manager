package com.infinitydheer.themanager.domain.interactor.master;

import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.repository.RazgoMasterRepository;

public class UCUserDetails {
    private RazgoMasterRepository mRepository;

    public UCUserDetails(RazgoMasterRepository repo){
        this.mRepository =repo;
    }

    public void allowAccess(boolean allow, String userId, ChangeListener<Void> listener){
        mRepository.allowAccess(allow, Nkrpt.processDef(userId),listener);
    }

    public void getUserAccessStatus(String userId, ChangeListener<Boolean> listener){
        mRepository.getUserAccessStatus(Nkrpt.processDef(userId),listener);
    }

    public void getParticipatingConversations(String userId, ChangeListener<ConvDomain> listener){
        mRepository.getParticipatingConversations(Nkrpt.processDef(userId),listener);
    }
}
