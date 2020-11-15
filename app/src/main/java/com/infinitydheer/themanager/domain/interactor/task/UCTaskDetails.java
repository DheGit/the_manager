package com.infinitydheer.themanager.domain.interactor.task;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.executor.PostExecutionThread;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;
import com.infinitydheer.themanager.domain.interactor.UseCase;
import com.infinitydheer.themanager.domain.repository.TaskRepository;

import io.reactivex.Observable;

/**
 * Use Case for the actions requiring task details
 */
public class UCTaskDetails extends UseCase<TaskDomain, UCTaskDetails.Param> {
    private TaskRepository mRepository;

    public UCTaskDetails(TaskRepository repo, ThreadExecutor executor, PostExecutionThread pet){
        super(executor,pet);
        this.mRepository =repo;
    }

    @Override
    public Observable<TaskDomain> makeObservable(Param param) {
        if(param==null) return null;
        return mRepository.getTask(param.taskID);
    }


    public static final class Param{
        private final long taskID;

        private Param(long id){ this.taskID=id; }

        public static Param forTask(long id){return new Param(id);}
    }
}
