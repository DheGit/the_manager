package com.infinitydheer.themanager.presentation.presenter.task;

import com.infinitydheer.themanager.domain.interactor.task.UCTaskOverview;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.task.TaskOverviewView;

public class TaskOverviewPresenter extends DefaultPresenter {
    private TaskOverviewView mView;
    private UCTaskOverview mInteractor;

    public TaskOverviewPresenter(UCTaskOverview inter){
        this.mInteractor =inter;
    }

    public void setView(TaskOverviewView overviewView){
        this.mView =overviewView;
    }

    @Override
    public void onDestroy() {
        this.mView =null;
    }

    @Override
    public void onResume() {
        initialise();
    }

    public void initialise(){
        Integer[] data= mInteractor.get();
        mView.fillQuadrants(data);
    }
}
