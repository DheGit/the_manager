package com.infinitydheer.themanager.domain.interactor.razgo;

import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.executor.PostExecutionThread;
import com.infinitydheer.themanager.domain.executor.ThreadExecutor;
import com.infinitydheer.themanager.domain.interactor.UseCase;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.listener.RazgoListener;
import com.infinitydheer.themanager.domain.repository.RazgoRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Use Case to get the Razgos from the offline datastore
 */
public class UCRazgoList extends UseCase<List<RazgoDomain>, UCRazgoList.Param> {
    private RazgoRepository mRepository;

    public UCRazgoList(ThreadExecutor executor, RazgoRepository repo, PostExecutionThread pet){
        super(executor,pet);
        this.mRepository =repo;
    }

    @Override
    public Observable<List<RazgoDomain>> makeObservable(Param param) {
        return this.mRepository.getRazgos(param.convid,param.endid);
    }

    public String getPartnerId(long convid){
        return this.mRepository.getPartnerId(convid);
    }

    public void setListener(ChangeListener<RazgoDomain> listener){
        this.mRepository.setRazgoListener(listener);
    }

    public void setMainListener(RazgoListener listener){
        mRepository.setMainListener(listener);
    }

    public long submitRazgo(long convid,RazgoDomain domain, boolean internet){
        return this.mRepository.addRazgo(convid,domain,internet);
    }

    public static final class Param{
        private final long convid;
        private final long endid;

        private Param(long con, long end){
            this.convid=con;
            this.endid=end;
        }

        public static Param forConv(long convid, long endid){
            return new Param(convid,endid);
        }
    }
}
