package com.infinitydheer.themanager.presentation.presenter.razgo;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.interactor.DefaultObserver;
import com.infinitydheer.themanager.domain.interactor.razgo.UCConvList;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.model.ConvModel;
import com.infinitydheer.themanager.presentation.model.mapper.ConvModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.razgo.ConvlistView;

import java.util.ArrayList;
import java.util.List;

/**
 * The presenter for the RazgoMain mView
 */
public class ConvlistPresenter extends DefaultPresenter {
    private ConvlistView mView;
    private UCConvList mInteractor;
    private ConvModelDataMapper mConvMapper;

    public ConvlistPresenter(UCConvList inter){
        this.mInteractor =inter;
        this.mConvMapper =new ConvModelDataMapper();
    }

    @Override
    public void onStart() {
        super.onStart();

        this.mInteractor.setListener(new ConvListCl());
        this.initialise();
    }
    @Override
    public void onStop() {
        super.onStop();
        this.mInteractor.setListener(null);
        this.loadData(new ArrayList<>());

        this.mInteractor.dispose();
        this.mInteractor.refresh();
    }

    /**
     * Starts the operations required, like data exchange
     */
    public void initialise(){
        this.hideRetry();
        this.showProgress();

        mInteractor.execute(new ConvListObserver(),null);
        mInteractor.setListener(new ConvListCl());
    }

    public void showRetry(){this.mView.showRetry();}
    public void showProgress(){this.mView.showProgress();}
    public void hideRetry(){this.mView.hideRetry();}
    public void hideProgress(){this.mView.hideProgress();}

    public void addConv(ConvDomain domain){
        ConvModel model= mConvMapper.transformToModel(domain);
        this.mView.addConv(model);
    }

    public void createConversationWith(String partnerModel){
        showProgress();hideRetry();
        String partner= Nkrpt.processDef(partnerModel);

        this.mInteractor.getConvid(partner, new ChangeListener<Long>() {
            @Override
            public void onNext(Long result) {
                ConvlistPresenter.this.mView.loadRazgoRoom(result);
            }
            @Override
            public void onComplete() {
                hideProgress();
                hideRetry();
            }
            @Override
            public void onError(Exception e) {
                hideProgress();
                showRetry();
                ConvlistPresenter.this.mView.showToast(e.getMessage());
            }
        });
    }

    public void loadData(List<ConvDomain> domainList){
        this.mView.populateList(mConvMapper.tranformToModel(domainList));
    }

    public void setView(ConvlistView vi){
        this.mView =vi;
    }

    public class ConvListCl implements ChangeListener<ConvDomain>{
        @Override
        public void onNext(ConvDomain result) {
            ConvlistPresenter.this.addConv(result);
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Exception e) {

        }
    }

    public class ConvListObserver extends DefaultObserver<List<ConvDomain>>{
        @Override
        public void onNext(List<ConvDomain> convDomains) {
            ConvlistPresenter.this.loadData(convDomains);
        }

        @Override
        public void onError(Throwable e) {
            ConvlistPresenter.this.hideProgress();
            ConvlistPresenter.this.showRetry();
        }

        @Override
        public void onComplete() {
            ConvlistPresenter.this.hideProgress();
            ConvlistPresenter.this.hideRetry();
        }
    }
}
