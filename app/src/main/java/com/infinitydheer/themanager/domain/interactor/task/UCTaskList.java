package com.infinitydheer.themanager.domain.interactor.task;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.executor.PostExecutionThread;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;
import com.infinitydheer.themanager.domain.interactor.UseCase;
import com.infinitydheer.themanager.domain.repository.TaskRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Use Case to get the list of tasks
 */
public class UCTaskList extends UseCase<List<TaskDomain>, UCTaskList.Param> {
    private TaskRepository mRepository;

    public UCTaskList(TaskRepository repository, PostExecutionThread pet, ThreadExecutor te){
        super(te,pet);
        this.mRepository =repository;
    }

    @Override
    public Observable<List<TaskDomain>> makeObservable(Param param) {
        if(param==null) return null;
        return this.mRepository.getTasks(param.quadrant);
    }

    public void deleteTasks(List<Long> list){
        this.mRepository.removeTasks(list);
    }

    public static final class Param {
        private final int quadrant;

        private Param(int quad){this.quadrant=quad;}

        public static Param forQuadrant(int quad){return new Param(quad);}
    }
}
