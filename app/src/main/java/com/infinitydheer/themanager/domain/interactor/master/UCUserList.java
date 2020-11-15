package com.infinitydheer.themanager.domain.interactor.master;

import com.infinitydheer.themanager.domain.data.UserDomain;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.repository.RazgoMasterRepository;

import java.util.List;

public class UCUserList{
    private RazgoMasterRepository mRepository;

    public UCUserList(RazgoMasterRepository repo){
        this.mRepository =repo;
    }

    public void getUsers(ChangeListener<List<UserDomain>> listener){
        mRepository.getUserList(listener);
    }
}
