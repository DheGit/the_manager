package com.infinitydheer.themanager.presentation.presenter.master;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.interactor.master.UCUserDetails;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.presentation.model.mapper.ConvModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.master.UserDetailsView;

public class UserDetailsPresenter extends DefaultPresenter {
    private UCUserDetails mInteractor;

    private ConvModelDataMapper mDataMapper;
    private UserDetailsView mView;

    private String mUserId;
    private boolean mAccessAllowed;

    public UserDetailsPresenter(UCUserDetails interactor){
        mInteractor=interactor;
        mDataMapper=new ConvModelDataMapper();
    }

    @Override
    public void onResume(){
        if(mUserId!=null&&mUserId.length()!=0){
            getConvList(mUserId);
            getUserAccessStatus();
        }
    }
    @Override
    public void onPause(){}

    public void getConvList(String userId){
        showProgress();
        hideRetry();

        mInteractor.getParticipatingConversations(userId,
                new ChangeListener<ConvDomain>() {
                    @Override
                    public void onNext(ConvDomain result) {
                        mView.onNewConversationReceived(mDataMapper.transformToModel(result));
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

    public void toggleAccessStatus(){
        mAccessAllowed=!mAccessAllowed;

        mInteractor.allowAccess(mAccessAllowed, mUserId, new ChangeListener<Void>() {
            @Override
            public void onNext(Void result) {}
            @Override
            public void onComplete() {
                mView.onAccessAllowed(mAccessAllowed);
                mView.showMessage("The account access has successfully been "+(mAccessAllowed?"granted":"revoked"));
            }

            @Override
            public void onError(Exception e) {
                mAccessAllowed=!mAccessAllowed;

                mView.showMessage(e.getMessage());
            }
        });
    }

    public void getUserAccessStatus(){
        mInteractor.getUserAccessStatus(mUserId, new ChangeListener<Boolean>() {
            @Override
            public void onNext(Boolean result) {
                mView.onAccessAllowed(result);
            }
            @Override
            public void onComplete() {}
            @Override
            public void onError(Exception e) {
                mView.showMessage(e.getMessage());
            }
        });
    }

    public void setView(UserDetailsView view){
        mView=view;
    }

    public void setUserId(String userId){
        mUserId=userId;
    }

    public void hideProgress(){if(mView!=null) mView.hideProgress();}
    public void showProgress(){if(mView!=null) mView.showProgress();}
    public void hideRetry(){if(mView!=null) mView.hideRetry();}
    public void showRetry(){if(mView!=null) mView.showRetry();}
}
