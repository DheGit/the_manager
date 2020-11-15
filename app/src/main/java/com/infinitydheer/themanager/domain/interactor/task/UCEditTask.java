package com.infinitydheer.themanager.domain.interactor.task;

import com.infinitydheer.themanager.domain.data.TaskDomain;
import com.infinitydheer.themanager.domain.executor.PostExecutionThread;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;
import com.infinitydheer.themanager.domain.interactor.UseCase;
import com.infinitydheer.themanager.domain.repository.TaskRepository;

import io.reactivex.Observable;

/**
 * Use Case for the task editing actions
 */
public class UCEditTask extends UseCase<TaskDomain, UCEditTask.Param> {
    private TaskRepository mRepository;

    public UCEditTask(TaskRepository repo, PostExecutionThread pet, ThreadExecutor exec){
        super(exec, pet);
        this.mRepository =repo;
    }

    /**
     * Saves the changes to the disk
     * @param domain The task object to be saved
     * @param newTask Flag which tells the method whether
     *                the task passed in is a new task or is already existing
     */
    public void put(TaskDomain domain, boolean newTask){
        if(newTask){
            this.mRepository.addTask(domain);
        }else{
            this.mRepository.updateTask(domain);
        }
    }

    @Override
    public Observable<TaskDomain> makeObservable(Param param) {
        return this.mRepository.getTask(param.taskID);
    }

    public static final class Param{
        private final long taskID;

        private Param(long id){ this.taskID=id; }

        public static Param forTask(long id){return new Param(id);}
    }
}
