package com.infinitydheer.themanager.presentation.view.fragment.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.executor.WorkExecutor;
import com.infinitydheer.themanager.data.repository.TaskDataRepository;
import com.infinitydheer.themanager.data.repository.datasource.task.TaskDataStoreFactory;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.interactor.task.UCTaskOverview;
import com.infinitydheer.themanager.presentation.presenter.task.TaskOverviewPresenter;
import com.infinitydheer.themanager.presentation.view.activity.task.TaskListActivity;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.task.TaskOverviewView;

public class TaskOverviewFragment extends BaseFragment implements TaskOverviewView {
    private TaskOverviewPresenter mPresenter;

    private RelativeLayout mQuadrant1, mQuadrant2, mQuadrant3, mQuadrant4;
    private TextView mLabel1, mLabel2, mLabel3, mLabel4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_task_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseD();
        initialiseV();

        mPresenter.setView(this);
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
    public void fillQuadrants(Integer[] data) {
        mLabel1.setText(String.format("%d", data[1]));
        mLabel2.setText(String.format("%d", data[2]));
        mLabel3.setText(String.format("%d", data[3]));
        mLabel4.setText(String.format("%d", data[4]));
    }

    private void initialiseD(){
        TaskDataStoreFactory factory=new TaskDataStoreFactory(getActivity());
        WorkExecutor w=WorkExecutor.getInstance();
        TaskDataRepository repo=new TaskDataRepository(factory,w);
        UCTaskOverview inter=new UCTaskOverview(repo);
        mPresenter =new TaskOverviewPresenter(inter);
    }

    private void initialiseV(){
        Activity activity=getActivity();
        if(activity==null) return;

        mQuadrant1 =activity.findViewById(R.id.rl_quadrant_1);
        mQuadrant2 = activity.findViewById(R.id.rl_quadrant_2);
        mQuadrant3 = activity.findViewById(R.id.rl_quadrant_3);
        mQuadrant4 = activity.findViewById(R.id.rl_quadrant_4);

        mLabel1 =activity.findViewById(R.id.tv_quadrant_1_num);
        mLabel2 =activity.findViewById(R.id.tv_quadrant_2_num);
        mLabel3 =activity.findViewById(R.id.tv_quadrant_3_num);
        mLabel4 =activity.findViewById(R.id.tv_quadrant_4_num);

        //TODO Improve this mess
        mQuadrant1.setOnClickListener(view -> {
            Intent intent = new Intent(TaskOverviewFragment.this.getActivity(), TaskListActivity.class);
            intent.putExtra(S.Constants.INTENT_QUAD_KEY, 1);
            startActivity(intent);
        });
        mQuadrant2.setOnClickListener(view -> {
            Intent intent = new Intent(TaskOverviewFragment.this.getActivity(), TaskListActivity.class);
            intent.putExtra(S.Constants.INTENT_QUAD_KEY, 2);
            startActivity(intent);
        });
        mQuadrant3.setOnClickListener(view -> {
            Intent intent = new Intent(TaskOverviewFragment.this.getActivity(), TaskListActivity.class);
            intent.putExtra(S.Constants.INTENT_QUAD_KEY, 3);
            startActivity(intent);
        });
        mQuadrant4.setOnClickListener(view -> {
            Intent intent = new Intent(TaskOverviewFragment.this.getActivity(), TaskListActivity.class);
            intent.putExtra(S.Constants.INTENT_QUAD_KEY, 4);
            startActivity(intent);
        });
    }
}
