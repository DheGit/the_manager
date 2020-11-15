package com.infinitydheer.themanager.domain.interactor.task;

import com.infinitydheer.themanager.domain.repository.TaskRepository;

/**
 * Use Case for getting the basic data about tasks.
 * Does not currently use {@link com.infinitydheer.themanager.domain.interactor.UseCase}
 */
public class UCTaskOverview {
    private TaskRepository mRepository;

    public UCTaskOverview(TaskRepository repository){
        this.mRepository =repository;
    }

    public Integer[] get(){
        Integer[] res=new Integer[5];
        res[0]=0;
        res[1]=(int) mRepository.getNumTasks(1);
        res[2]=(int) mRepository.getNumTasks(2);
        res[3]=(int) mRepository.getNumTasks(3);
        res[4]=(int) mRepository.getNumTasks(4);
        return res;
    }
}
