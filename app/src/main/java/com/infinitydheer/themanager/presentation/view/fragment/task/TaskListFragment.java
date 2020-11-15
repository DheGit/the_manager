package com.infinitydheer.themanager.presentation.view.fragment.task;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.executor.WorkExecutor;
import com.infinitydheer.themanager.data.repository.TaskDataRepository;
import com.infinitydheer.themanager.data.repository.datasource.task.TaskDataStoreFactory;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.interactor.task.UCTaskList;
import com.infinitydheer.themanager.presentation.UIThread;
import com.infinitydheer.themanager.presentation.view.activity.task.EditTaskActivity;
import com.infinitydheer.themanager.presentation.view.activity.task.TaskDetailsActivity;
import com.infinitydheer.themanager.presentation.view.activity.task.TaskListActivity;
import com.infinitydheer.themanager.presentation.view.adapter.TaskListAdapter;
import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.model.mapper.TaskModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.task.TaskListPresenter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.task.TaskListView;

import java.util.List;

public class TaskListFragment extends BaseFragment implements TaskListView {
    private static final String PARAM_QUAD_VAL = "param_quad_val";

    private int mQuad;
    private TaskListFragmentListener mListener;

    private TaskListAdapter mAdapter;
    private TaskListPresenter mPresenter;
    private View mNoTaskView;
    private RecyclerView mTaskRecycler;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;

    public static TaskListFragment forQuadrant(int q) {
        if (q > 4 || q < 1) return null;

        TaskListFragment fragment = new TaskListFragment();
        final Bundle instanceState = new Bundle();

        instanceState.putInt(PARAM_QUAD_VAL, q);
        fragment.setQuadrant(q);
        fragment.setArguments(instanceState);

        return fragment;
    }

    public TaskListFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_task_list, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialiseV();
        initialiseD();

        mPresenter.setView(this);
        mPresenter.initialise(getQuadrant());
    }

    private void initialiseD() {
        TaskDataStoreFactory factory = new TaskDataStoreFactory(this.getActivity());
        WorkExecutor executor = WorkExecutor.getInstance();
        TaskDataRepository repository = new TaskDataRepository(factory, executor);
        UIThread uiThread = UIThread.getInstance();
        UCTaskList ucTaskList = new UCTaskList(repository, uiThread, executor);
        TaskModelDataMapper mapper = new TaskModelDataMapper();
        mPresenter = new TaskListPresenter(ucTaskList, mapper);
    }
    private void initialiseV() {
        Activity activity = getActivity();
        if (activity == null) return;

        this.mTaskRecycler = activity.findViewById(R.id.rv_tasklist);
        this.mFab = activity.findViewById(R.id.fab_add_task);
        this.mProgressBar = activity.findViewById(R.id.pb_basic);
        this.mNoTaskView = activity.findViewById(R.id.no_task_layout);

        this.mFab.setOnClickListener(view -> launchAddTask());

        int color = ((TaskListActivity) getActivity()).getColorForQuadrant(getQuadrant());
        this.mFab.setBackgroundTintList(ColorStateList.valueOf(color));

        mAdapter = new TaskListAdapter(new TaskListAdapter.TaskListAdapterListener() {
            @Override
            public void onTaskClicked(TaskModel taskModel) {
                TaskListFragment.this.showTask(taskModel.getId());
            }

            @Override
            public void setSelectMode(boolean sm) {
                TaskListFragment.this.mListener.setSelected(sm);
            }
        });

        mTaskRecycler.setLayoutManager(new LinearLayoutManager(activity));
        mTaskRecycler.setAdapter(mAdapter);

        hideNullTasks();
    }

    @Override
    public void populateTasks(List<TaskModel> tasks) {
        mAdapter.setData(tasks);
    }

    @Override
    public void showTask(long id) {
        Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);

        intent.putExtra(S.Constants.INTENT_TASKID_KEY, id);
        intent.putExtra(S.Constants.INTENT_QUAD_KEY, mQuad);

        startActivity(intent);
    }

    public void deleteTasks() {
        List<Long> taskIds = mAdapter.getSelected();
        this.mPresenter.deleteTasks(taskIds);

        this.mAdapter.clearSelected();
        this.mAdapter.removeTasks(taskIds);

        if (mAdapter.getItemCount() == 0) showNullTasks();
    }

    private void launchAddTask() {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);

        intent.putExtra(S.Constants.INTENT_TASKID_KEY, -1);
        intent.putExtra(S.Constants.INTENT_QUAD_KEY, mQuad);

        startActivity(intent);
    }

    public void setListener(TaskListFragmentListener listen) {
        this.mListener = listen;
    }

    public void clearSelected() {
        this.mAdapter.clearSelected();
    }

    private int getQuadrant() {
        if (mQuad > 4 || mQuad < 1) mQuad = this.getArguments().getInt(PARAM_QUAD_VAL, 1);
        return mQuad;
    }
    private void setQuadrant(int qua) {
        this.mQuad = qua;
    }

    @Override
    public void hideProgress() {
        this.mProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void showProgress() {
        this.mProgressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideRetry() {
        //Add something
    }
    @Override
    public void showRetry() {
        //Add something
    }
    @Override
    public void hideNullTasks() {
        this.mNoTaskView.setVisibility(View.GONE);
    }
    @Override
    public void showNullTasks() {
        this.mNoTaskView.setVisibility(View.VISIBLE);
    }

    public interface TaskListFragmentListener {
        void setSelected(boolean selected);
    }
}
