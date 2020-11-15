package com.infinitydheer.themanager.domain.interactor;

import io.reactivex.observers.DisposableObserver;

/**
 * Wrapper class which represents the basic {@link DisposableObserver}
 * By default, no action is taken at any change
 * @param <T> The datatype emitted
 */
public class DefaultObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
