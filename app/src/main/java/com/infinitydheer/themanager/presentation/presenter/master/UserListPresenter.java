package com.infinitydheer.themanager.presentation.presenter.master;

import com.infinitydheer.themanager.domain.data.UserDomain;
import com.infinitydheer.themanager.domain.interactor.master.UCUserList;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.presentation.model.UserModel;
import com.infinitydheer.themanager.presentation.model.mapper.UserModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.master.UserListView;

import java.util.List;

public class UserListPresenter extends DefaultPresenter {
    private UCUserList mInteractor;
    private UserModelDataMapper dataMapper;

    private UserListView mView;

    public UserListPresenter(UCUserList interactor){
        mInteractor=interactor;
        dataMapper=new UserModelDataMapper();
    }

    public void onResume(){
        getUsers();
    }

    public void onPause(){}

    public void getUsers(){
        hideRetry();
        showProgress();

        mInteractor.getUsers(new ChangeListener<List<UserDomain>>() {
            @Override
            public void onNext(List<UserDomain> result) {
                if(mView!=null) mView.populateList(dataMapper.transformToModel(result));
            }

            @Override
            public void onComplete() {
                hideProgress();
            }

            @Override
            public void onError(Exception e) {
                hideProgress();
                showRetry();
            }
        });
    }

    public void setView(UserListView view){
        mView=view;
    }

    public void showRetry(){if(mView!=null) mView.showRetry();}
    public void hideRetry(){if(mView!=null) mView.hideRetry();}
    public void showProgress(){if(mView!=null) mView.showProgress();}
    public void hideProgress(){if(mView!=null) mView.hideProgress();}
}
