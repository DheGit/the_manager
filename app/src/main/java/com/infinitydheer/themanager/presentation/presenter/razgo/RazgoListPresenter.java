package com.infinitydheer.themanager.presentation.presenter.razgo;

import android.util.Log;

import com.infinitydheer.themanager.domain.data.ConvDomain;
import com.infinitydheer.themanager.domain.data.RazgoDomain;
import com.infinitydheer.themanager.domain.interactor.DefaultObserver;
import com.infinitydheer.themanager.domain.interactor.razgo.UCRazgoList;
import com.infinitydheer.themanager.domain.listener.ChangeListener;
import com.infinitydheer.themanager.domain.listener.RazgoListener;
import com.infinitydheer.themanager.domain.utils.CalendarUtils;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;
import com.infinitydheer.themanager.presentation.model.RazgoModel;
import com.infinitydheer.themanager.presentation.model.mapper.RazgoModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.DefaultPresenter;
import com.infinitydheer.themanager.presentation.view.iview.razgo.RazgoListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The presenter class for the RazgoList mView
 */
public class RazgoListPresenter extends DefaultPresenter implements RazgoListener {
    private RazgoListView mView;
    private RazgoModelDataMapper mRazgoMapper;
    private UCRazgoList mInteractor;

    private RazgoListObserver mRlObserver;

    private long mConvId =-1;

    private String mPartnerName;

    public RazgoListPresenter(UCRazgoList inter) {
        this.mInteractor = inter;
        this.mRazgoMapper = new RazgoModelDataMapper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mView = null;

        this.mInteractor.dispose();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mInteractor.setListener(null);
    }
    @Override
    public void onResume() {
        super.onResume();

//        this.mInteractor.setListener(new RazgoListCl());
        this.mInteractor.setMainListener(this);
        if(mConvId !=-1) initialise(mConvId);
    }

    public void initialise(long convid) {
        this.mConvId = convid;

        this.mPartnerName =this.mInteractor.getPartnerId(convid);
        mPartnerName = Nkrpt.unprocessDef(mPartnerName);
        mView.setPartnerId(mPartnerName);
        mRlObserver=new RazgoListObserver();

        getRazgos(-1);
    }

    public void getRazgos(long endid){
        RazgoListPresenter.this.hideRetry();
        RazgoListPresenter.this.showProgress();

        Log.d("TheManagerLog","getRazgos attempting to get 100 razgos before "+endid+" in convId"+mConvId);

        this.mInteractor.execute(mRlObserver, UCRazgoList.Param.forConv(mConvId,endid));
    }

    public void getDemoRazgos(long end){
        List<RazgoDomain> razgoDomains=new ArrayList<>();

        for(int i=1;i<=100;i++){
                RazgoDomain rd=new RazgoDomain();
                rd.setContent("Content"+i);
                rd.setDatetime("datetimeish+"+i);
                rd.setId(1000+i);
                rd.setK(12+i);
                rd.setSender(i%2==0?"dheerS":"sanchiD");
                rd.setSent(i%2==0);
                razgoDomains.add(rd);
        }

        loadData(razgoDomains);
    }

    public void addRazgo(RazgoDomain domain, boolean self) {
        RazgoModel usefulRazgo = this.mRazgoMapper.transformToModel(domain);
        if (usefulRazgo.getSender().equals(mPartnerName) ||
                usefulRazgo.getSender().equals(ApplicationGlobals.SELF_ID_UP)){
            this.mView.addRazgo(usefulRazgo, self);
        }
    }

    public void loadData(List<RazgoDomain> domainList) {
        this.mView.loadRazgos(this.mRazgoMapper.transformToModel(domainList));
    }

    public void submitRazgo(String content) {
        RazgoModel model = new RazgoModel();
        Calendar now = Calendar.getInstance();

        model.setContent(content);
        model.setDatetime(CalendarUtils.convertToString(now, true));
        model.setSender(ApplicationGlobals.SELF_ID_UP);

        RazgoDomain domain = mRazgoMapper.transform(model);
        long newId=this.mInteractor.submitRazgo(this.mConvId, domain, mView.isInternet());
        newId*=-1;

        domain.setSent(false);
        domain.setId(newId);

        addRazgo(domain, true);
    }

    @Override
    public void onRazgoRecieved(RazgoDomain razgoDomain) {
        addRazgo(razgoDomain, false);
    }

    @Override
    public void onRazgoSent(long oldId, long newId) {
        mView.setSent(oldId,newId);
    }

    @Override
    public void onRazgoReadByPartner(long razgoId) {
        //TODO: The double tick thingy
    }

    public class RazgoListCl implements ChangeListener<RazgoDomain> {
        @Override
        public void onNext(RazgoDomain result) {
            RazgoListPresenter.this.addRazgo(result, false);
        }

        @Override
        public void onComplete() {}
        @Override
        public void onError(Exception e) {}
    }

    public class RazgoListObserver extends DefaultObserver<List<RazgoDomain>> {
        @Override
        public void onNext(List<RazgoDomain> domains) {
            Log.d("TheManagerLog","RazgoListObserver received "+domains.size()+" razgos from interactor UCRazgoList, passing it to View");
            RazgoListPresenter.this.loadData(domains);
        }

        @Override
        public void onError(Throwable e) {
            RazgoListPresenter.this.hideProgress();
            RazgoListPresenter.this.showRetry();
            RazgoListPresenter.this.mInteractor.refresh();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            RazgoListPresenter.this.hideProgress();
            RazgoListPresenter.this.mInteractor.refresh();
        }
    }

    public void setView(RazgoListView vi) {
        this.mView = vi;
    }
    public void setConvid(long convid){
        this.mConvId =convid;
    }

    public void hideProgress() {
        this.mView.hideProgress();
    }
    public void showProgress() {
        this.mView.showProgress();
    }
    public void hideRetry() {
        this.mView.hideRetry();
    }
    public void showRetry() {
        this.mView.showRetry();
    }
}
