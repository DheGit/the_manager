package com.infinitydheer.themanager.data.repository.datasource.master;

import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.UserEntity;
import com.infinitydheer.themanager.domain.listener.ChangeListener;

import java.util.List;

public interface MasterStore {
    void getUserList(ChangeListener<List<UserEntity>> changeListener);

    void allowAccess(boolean allow,String userId, ChangeListener<Void> changeListener);
    void getUserAccessStatus(String userId, ChangeListener<Boolean> changeListener);

    void getParticipatingConversations(String userId, ChangeListener<ConvEntity> changeListener);

    void cleanConversation(long convId, ChangeListener<Void> then);
}
