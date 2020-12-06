package com.infinitydheer.themanager.presentation.presenter.task;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.interactor.DefaultObserver;
import com.infinitydheer.themanager.domain.interactor.task.UCEditTask;
import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.model.mapper.TaskModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.task.EditTaskView;

public class EditTaskPresenter extends DefaultPresenter {
    private EditTaskView mView;
    private UCEditTask mInteractor;
    private TaskModelDataMapper mTaskMapper;

    public EditTaskPresenter(UCEditTask iota, TaskModelDataMapper mappe){
        this.mInteractor =iota;
        this.mTaskMapper =mappe;
    }

    @Override
    public void onStop() {
        super.onStop();

        showEmptyTask();
        this.mInteractor.dispose();
        this.mInteractor.refresh();
    }

    @Override
    public void onStart() {
        super.onStart();

        //Start something, which should be there when the screen shows up
    }

    public void initialise(long id){
        hideRetry();
        if(id!=-1) {
            showProgress();
            this.mInteractor.execute(new EditTaskObserver(), UCEditTask.Param.forTask(id));
        }
    }

    private void showEmptyTask(){
        TaskDomain domain=new TaskDomain();
        domain.setDone(0);
        domain.setDueDate("");
        domain.setId(0L);
        domain.setName("Task");
        domain.setQuad(4);
        domain.setNote("Note");
        showTask(domain);
    }
    private void showTask(TaskDomain domain){
        TaskModel model= mTaskMapper.transformToModel(domain);
        this.mView.renderTask(model);
    }

    public void saveTask(TaskModel model, boolean isNewTask){
        if(model.getName().equals("")){
            mView.onNoNameOnSave();
            return;
        }

        TaskDomain reqData= mTaskMapper.transform(model);
        this.mInteractor.put(reqData, isNewTask);
    }

    public final class EditTaskObserver extends DefaultObserver<TaskDomain>{

        @Override
        public void onNext(TaskDomain taskDomain) {
            EditTaskPresenter.this.showTask(taskDomain);
        }

        @Override
        public void onError(Throwable e) {
            EditTaskPresenter.this.hideProgress();
            EditTaskPresenter.this.showRetry();
        }
        @Override
        public void onComplete() {
            EditTaskPresenter.this.hideProgress();
        }

    }

    public void setView(EditTaskView vie){
        this.mView =vie;
    }

    private void hideProgress(){
        mView.hideProgress();}
    private void showProgress(){
        mView.showProgress();}
    private void showRetry(){
        mView.showRetry();}
    private void hideRetry(){
        mView.hideRetry();}
}
