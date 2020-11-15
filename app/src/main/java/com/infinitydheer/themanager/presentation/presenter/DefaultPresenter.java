package com.infinitydheer.themanager.presentation.presenter;

/**
 * Default presenter class avoid boilerplate code in the real presenters.
 * The lifecycle-aware methods do nothing here by default.
 * Although, they are always called with the corresponding lifecycle-methods of the respective views(activities, fragments, etc)
 */
public class DefaultPresenter implements BasePresenter {
    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
