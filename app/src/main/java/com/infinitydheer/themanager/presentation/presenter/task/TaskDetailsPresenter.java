package com.infinitydheer.themanager.presentation.presenter.task;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.interactor.DefaultObserver;
import com.infinitydheer.themanager.domain.interactor.task.UCTaskDetails;
import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.model.mapper.TaskModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.BaseView;
import com.infinitydheer.themanager.presentation.view.iview.task.TaskDetailsView;

public class TaskDetailsPresenter extends DefaultPresenter {
    private TaskDetailsView mView;
    private UCTaskDetails mInteractor;
    private TaskModelDataMapper mDataMapper;

    private long mId;

    public TaskDetailsPresenter(UCTaskDetails inter, TaskModelDataMapper mapper){
        this.mInteractor =inter;
        this.mDataMapper =mapper;
        this.mId =-1;
    }

    @Override
    public void onDestroy() {
        this.mView =null;
        this.mInteractor.dispose();
    }
    @Override
    public void onResume() {
        getTaskDetails(mId);
    }

    public void getTaskDetails(long id){
        this.mId =id;

        hideRetry();
        if(id!=-1) {
            showProgress();
            this.mInteractor.execute(new TaskDetailsObserver(), UCTaskDetails.Param.forTask(id));
        }
    }

    private void showTask(TaskDomain domain){
        TaskModel model= mDataMapper.transformToModel(domain);
        this.mView.renderTask(model);
    }

    private final class TaskDetailsObserver extends DefaultObserver<TaskDomain> {
        @Override
        public void onNext(TaskDomain taskDomain) {
            TaskDetailsPresenter.this.showTask(taskDomain);
        }

        @Override
        public void onError(Throwable e) {
            TaskDetailsPresenter.this.hideProgress();
            TaskDetailsPresenter.this.showRetry();
        }

        @Override
        public void onComplete() {
            TaskDetailsPresenter.this.hideProgress();
        }
    }

    public void setView(TaskDetailsView vie){
        this.mView =vie;
    }

    private void hideProgress(){
        if(mView!=null) mView.hideProgress();
    }
    private void showProgress(){
        if(mView!=null) mView.showProgress();}
    private void showRetry(){
        if(mView!=null) mView.showRetry();}
    private void hideRetry(){
        if(mView!=null) mView.hideRetry();}
}
