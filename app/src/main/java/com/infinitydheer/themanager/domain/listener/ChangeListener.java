package com.infinitydheer.themanager.domain.listener;

/**
 * A standard callback interface which can be used in various listeners
 * such as those of {@link com.google.firebase.firestore.FirebaseFirestore}.
 * Based on the model of {@link io.reactivex.Observable}
 * @param <T> Type of the data exchanged
 */
public interface ChangeListener<T> {
    void onNext(T result);
    void onComplete();
    void onError(Exception e);
}
