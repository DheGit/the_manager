package com.infinitydheer.themanager.presentation.view.iview.razgo;

import com.infinitydheer.themanager.presentation.model.RazgoModel;
import com.infinitydheer.themanager.presentation.view.iview.LoadDataView;

import java.util.List;

public interface RazgoListView extends LoadDataView {
    void addRazgo(RazgoModel model, boolean self);
    boolean isInternet();
    void setPartnerId(String partnerId);
    void loadRazgos(List<RazgoModel> models);
    void setSent(long oldId, long newId);
}
