package com.infinitydheer.themanager.presentation.view.iview.task;

import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.view.iview.LoadDataView;

public interface TaskDetailsView extends LoadDataView {
    void renderTask(TaskModel model);
}
