package com.infinitydheer.themanager.presentation.view.iview.master;

import com.infinitydheer.themanager.presentation.model.UserModel;
import com.infinitydheer.themanager.presentation.view.iview.LoadDataView;

import java.util.List;

public interface UserListView extends LoadDataView {
    void populateList(List<UserModel> userModelList);
}
