package com.infinitydheer.themanager.domain.interactor;

import com.infinitydheer.themanager.domain.executor.PostExecutionThread;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Abstract class to encapsulate a Use Case, that is, a specific case of
 * data exchange necessary in the application
 * @param <T> The datatype of the data exchanged
 * @param <Params> The class encapsulating the parameters to be passed
 */
public abstract class UseCase<T, Params> {
    private final ThreadExecutor mExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private CompositeDisposable mDisposables;

    public UseCase(ThreadExecutor threadExecutor, PostExecutionThread executionThread){
        this.mExecutor =threadExecutor;
        this.mPostExecutionThread =executionThread;
        this.mDisposables =new CompositeDisposable();
    }

    /**
     * Method to be implemented by child classes for making the {@link Observable} to be
     * executed
     * @param param The parameters required to be passed to the Repository
     * @return Observable emitting the required datatype
     */
    abstract public Observable<T> makeObservable(Params param);

    /**
     * Executes the {@link Observable} made, attaching the observer to it
     * @param observer The observer which reacts to the changes in the observable
     * @param params The parameters required to make the observable
     */
    public void execute(DisposableObserver<T> observer, Params params){
        if(observer==null) return;
        final Observable<T> observable=this.makeObservable(params)
                .subscribeOn(Schedulers.from(mExecutor))
                .observeOn(mPostExecutionThread.getScheduler());
        addDisposable(observable.subscribeWith(observer));
    }

    public void dispose(){
        if(!mDisposables.isDisposed()){
            mDisposables.dispose();
        }
    }

    public void refresh(){
        mDisposables.clear();
        mDisposables=null;
    }

    private void addDisposable(Disposable disposable){
        if(disposable==null) return;
        if(mDisposables==null) mDisposables = new CompositeDisposable();
        mDisposables.add(disposable);
    }
}
