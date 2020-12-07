package com.infinitydheer.themanager.presentation.presenter.task;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.interactor.DefaultObserver;
import com.infinitydheer.themanager.domain.interactor.task.UCTaskList;
import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.model.mapper.TaskModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.task.TaskListView;

import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter extends DefaultPresenter {
    private TaskListView mView;
    private UCTaskList mInteractor;
    private TaskModelDataMapper mTaskMapper;

    private int quad;

    public TaskListPresenter(UCTaskList interactor, TaskModelDataMapper dataMapper){
        this.mInteractor =interactor;
        this.mTaskMapper =dataMapper;
        this.quad=1;
    }

//    @Override
//    public void onDestroy() {
//        this.mInteractor.dispose();
//    }
//    @Override
//    public void onResume() {
//        initialise(quad);
//    }

    @Override
    public void onStart() {
        super.onStart();
        initialise(quad);
    }

    @Override
    public void onStop() {
        super.onStop();

        loadData(new ArrayList<>());
        mView.showNullTasks();
        this.mInteractor.dispose();
        this.mInteractor.refresh();
    }

    public void initialise(int quad){
        this.quad=quad;

        mView.hideNullTasks();
        hideRetry();
        showProgress();
        this.mInteractor.execute(new TaskListObserver(), UCTaskList.Param.forQuadrant(quad));
    }

    public void deleteTasks(List<Long> idList){
        this.mInteractor.deleteTasks(idList);
    }

    private void loadData(List<TaskDomain> data){
        if(data.size()==0){
            mView.showNullTasks();
            return;
        }

        List<TaskModel> reqData= mTaskMapper.transformToModel(data);
        mView.populateTasks(reqData);
    }

    public void setView(TaskListView aview){
        this.mView =aview;
    }

    private void showProgress(){
        mView.showProgress();}
    private void hideProgress(){
        mView.hideProgress();}
    private void hideRetry(){
        mView.hideRetry();}
    private void showRetry(){
        mView.showRetry();}

    private final class TaskListObserver extends DefaultObserver<List<TaskDomain>>{

        @Override
        public void onNext(List<TaskDomain> taskDomains) {
            TaskListPresenter.this.loadData(taskDomains);
        }

        @Override
        public void onError(Throwable e) {
            TaskListPresenter.this.hideProgress();
            TaskListPresenter.this.showRetry();
        }

        @Override
        public void onComplete() {
            TaskListPresenter.this.hideProgress();
        }

    }
}
