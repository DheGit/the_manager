package com.infinitydheer.themanager.presentation.view.fragment.task;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.executor.WorkExecutor;
import com.infinitydheer.themanager.data.repository.TaskDataRepository;
import com.infinitydheer.themanager.data.repository.datasource.task.TaskDataStoreFactory;
import com.infinitydheer.themanager.domain.interactor.task.UCTaskDetails;
import com.infinitydheer.themanager.domain.utils.CalendarUtils;
import com.infinitydheer.themanager.presentation.UIThread;
import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.model.mapper.TaskModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.task.TaskDetailsPresenter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.task.TaskDetailsView;

public class TaskDetailsFragment extends BaseFragment implements TaskDetailsView {
    private final static String PARAM_ID="param_id";

    private TaskDetailsPresenter mPresenter;
    private TextView mTaskNameLabel, mTaskNoteLabel, mTaskDueLabel;
    private ProgressBar mProgressBar;

    public static TaskDetailsFragment forTask(long id){
        final Bundle instanceState = new Bundle();
        instanceState.putLong(PARAM_ID, id);

        final TaskDetailsFragment fragment=new TaskDetailsFragment();
        fragment.setArguments(instanceState);

        return fragment;
    }

    public TaskDetailsFragment(){setRetainInstance(true);}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_task_details, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseV();
        initialiseD();

        this.mPresenter.setView(this);
        this.mPresenter.getTaskDetails(getTaskID());
    }

    @Override
    public void renderTask(TaskModel model) {
        this.mTaskNameLabel.setText(model.getName());
        this.mTaskNoteLabel.setText(model.getNote());
        this.mTaskDueLabel.setText(CalendarUtils.convertToString(model.getDate(),false));
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

    private void initialiseD(){
        TaskDataStoreFactory factory=new TaskDataStoreFactory(getActivity());
        WorkExecutor executor=WorkExecutor.getInstance();
        TaskDataRepository repository=new TaskDataRepository(factory,executor);
        UIThread uiThread=UIThread.getInstance();
        UCTaskDetails interactor=new UCTaskDetails(repository,executor,uiThread);
        TaskModelDataMapper mapper=new TaskModelDataMapper();
        mPresenter =new TaskDetailsPresenter(interactor, mapper);
    }

    private void initialiseV(){
        Activity activity=getActivity();
        if(activity==null) return;

        this.mTaskNameLabel =activity.findViewById(R.id.tv_task_name);
        this.mTaskDueLabel =activity.findViewById(R.id.tv_task_due);
        this.mTaskNoteLabel =activity.findViewById(R.id.tv_task_note);
        this.mProgressBar =activity.findViewById(R.id.pb_basic);
    }

    private long getTaskID(){
        final Bundle args=this.getArguments();
        return args.getLong(PARAM_ID, -1);
    }
}
