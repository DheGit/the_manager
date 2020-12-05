package com.infinitydheer.themanager.presentation.view.iview.master;

import com.infinitydheer.themanager.presentation.model.ConvModel;
import com.infinitydheer.themanager.presentation.view.iview.LoadDataView;


public interface UserDetailsView extends LoadDataView{
    void onNewConversationReceived(ConvModel convModel);
    void onAccessAllowed(boolean allowed);
    void showMessage(String message);

    void resetConvList();
}
