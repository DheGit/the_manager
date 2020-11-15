package com.infinitydheer.themanager.domain.interactor.razgo;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.executor.PostExecutionThread;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;
import com.infinitydheer.themanager.domain.interactor.UseCase;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.repository.RazgoRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Use Case to get Convs from the offline store.
 */
public class UCConvList extends UseCase<List<ConvDomain>,Void> {
    private RazgoRepository mRepository;

    public UCConvList(RazgoRepository repo, ThreadExecutor executor, PostExecutionThread pet){
        super(executor,pet);
        this.mRepository =repo;
    }

    @Override
    public Observable<List<ConvDomain>> makeObservable(Void unused) {
        return mRepository.getConversations();
    }

    public void getConvid(String partnerName, ChangeListener<Long> changeListener){
        this.mRepository.getConvid(partnerName,changeListener);
    }

    public void setListener(ChangeListener<ConvDomain> listener){
        this.mRepository.setConversationListener(listener);
    }
}
