package com.infinitydheer.themanager.presentation.presenter;

public interface BasePresenter {
    void onDestroy();

    void onStart();
    void onStop();

    void onPause();
    void onResume();
}
