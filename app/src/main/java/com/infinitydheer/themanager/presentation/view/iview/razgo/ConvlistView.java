package com.infinitydheer.themanager.presentation.view.iview.razgo;

import com.infinitydheer.themanager.presentation.model.ConvModel;
import com.infinitydheer.themanager.presentation.view.iview.LoadDataView;

import java.util.List;

public interface ConvlistView extends LoadDataView {
    void populateList(List<ConvModel> models);
    void addConv(ConvModel model);
    void loadRazgoRoom(long convid);
    void showToast(String message);
}
