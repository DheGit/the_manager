package com.infinitydheer.themanager.domain.interactor.razgo;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.interactor.BasicUseCase;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.repository.RazgoRepository;

public class UCSync extends BasicUseCase {
    private final RazgoRepository repository;
    public UCSync(RazgoRepository razgoRepository,ChangeListener<ConvDomain> conversationListener){
        this.repository=razgoRepository;
    }

    public void sync(){
        this.repository.sync();
    }
}
