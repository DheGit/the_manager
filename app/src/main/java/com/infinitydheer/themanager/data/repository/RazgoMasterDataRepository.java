package com.infinitydheer.themanager.data.repository;

import com.infinitydheer.themanager.data.entity.ConvEntity;
import com.infinitydheer.themanager.data.entity.UserEntity;
import com.infinitydheer.themanager.data.entity.mapper.ConvEntityDataMapper;
import com.infinitydheer.themanager.data.entity.mapper.UserEntityDataMapper;
import com.infinitydheer.themanager.data.repository.datasource.master.MasterDataStore;
import com.infinitydheer.themanager.data.repository.datasource.master.MasterStore;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.UserDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.repository.RazgoMasterRepository;

import java.util.List;

public class RazgoMasterDataRepository implements RazgoMasterRepository {
    private static RazgoMasterDataRepository sInstance;

    private MasterStore mStore;
    private ConvEntityDataMapper mConvMapper;
    private UserEntityDataMapper mUserMapper;

    private RazgoMasterDataRepository(){
        mStore = MasterDataStore.getInstance();
        mConvMapper =new ConvEntityDataMapper();
        mUserMapper=new UserEntityDataMapper();
    }

    public static synchronized RazgoMasterDataRepository getInstance(){
        if(sInstance==null){
            sInstance=new RazgoMasterDataRepository();
        }
        return sInstance;
    }

    @Override
    public void getUserList(ChangeListener<List<UserDomain>> changeListener) {
        this.mStore.getUserList(new ChangeListener<List<UserEntity>>() {
            @Override
            public void onNext(List<UserEntity> result) {
                changeListener.onNext(mUserMapper.transform(result));
            }
            @Override
            public void onComplete() {
                changeListener.onComplete();
            }
            @Override
            public void onError(Exception e) {
                changeListener.onError(e);
            }
        });
    }


    @Override
    public void allowAccess(boolean allow, String userId, ChangeListener<Void> changeListener) {
        this.mStore.allowAccess(allow, userId,changeListener);
    }

    @Override
    public void getUserAccessStatus(String userId, ChangeListener<Boolean> changeListener) {
        this.mStore.getUserAccessStatus(userId,changeListener);
    }

    @Override
    public void getParticipatingConversations(String userId, ChangeListener<ConvDomain> changeListener) {
        this.mStore.getParticipatingConversations(userId, new ChangeListener<ConvEntity>() {
            @Override
            public void onNext(ConvEntity result) {
                changeListener.onNext(mConvMapper.transform(result));
            }
            @Override
            public void onComplete() {
                changeListener.onComplete();
            }
            @Override
            public void onError(Exception e) {
                changeListener.onError(e);
            }
        });
    }

    @Override
    public void cleanConversation(long convId, ChangeListener<Void> then) {
        mStore.cleanConversation(convId, then);
    }
}
