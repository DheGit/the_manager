package com.infinitydheer.themanager.domain.repository;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.UserDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;

import java.util.List;

public interface RazgoMasterRepository {
    void getUserList(ChangeListener<List<UserDomain>> changeListener);

    void allowAccess(boolean allow, String userId, ChangeListener<Void> changeListener);
    void getUserAccessStatus(String userId, ChangeListener<Boolean> changeListener);

    void getParticipatingConversations(String userId, ChangeListener<ConvDomain>changeListener);

    void cleanConversation(long convId, ChangeListener<Void> then);
}
