package com.infinitydheer.themanager.presentation.view.iview.task;

import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.view.iview.LoadDataView;

import java.util.List;

public interface TaskListView extends LoadDataView {
    void populateTasks(List<TaskModel> tasks);
    void showTask(long id);

    void showNullTasks();
    void hideNullTasks();
}
