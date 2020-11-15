package com.infinitydheer.themanager.presentation.view.activity.task;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.task.TaskListFragment;

public class TaskListActivity extends BaseActivity implements TaskListFragment.TaskListFragmentListener {
    private static final String PARAM_QUAD="bundle_quad";

    private int mQuad;
    private boolean mIsSelectMode =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        initialise();
        initialiseV();

        addFragment(R.id.fl_tasklist, TaskListFragment.forQuadrant(mQuad));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof TaskListFragment){
            TaskListFragment fragment1=(TaskListFragment)fragment;
            fragment1.setListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_delete_tasks).setVisible(mIsSelectMode);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete_tasks:
                confirmDeleteTasks();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelected(boolean selected) {
        this.mIsSelectMode =selected;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if(mIsSelectMode){
            ((TaskListFragment)getSupportFragmentManager().findFragmentById(R.id.fl_tasklist)).clearSelected();
            mIsSelectMode =false;
            invalidateOptionsMenu();
        }else{
            super.onBackPressed();
        }
    }

    private void confirmDeleteTasks(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle(R.string.delete_tasks_action)
                .setMessage(R.string.delete_tasks_message)
                .setPositiveButton(R.string.yes_text, ((dialogInterface, i) -> deleteTasks()))
                .setNegativeButton(R.string.no_text, ((dialogInterface, i) -> dialogInterface.dismiss()))
                .create().show();
    }

    private void deleteTasks(){
        TaskListFragment frag=(TaskListFragment)getSupportFragmentManager().findFragmentById(R.id.fl_tasklist);
        frag.deleteTasks();

        mIsSelectMode =false;
        invalidateOptionsMenu();
    }

    public int getColorForQuadrant(int quad){
        return getApp().getQuadColor(quad);
    }

    private void initialise(){
        Intent intent=getIntent();
        this.mQuad =intent.getIntExtra(S.Constants.INTENT_QUAD_KEY,1);

        initialiseV();
    }

    private void initialiseV(){
        Toolbar bar=findViewById(R.id.tb_task_list);
        setSupportActionBar(bar);
        setV();
    }

    private void setV(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getApp().getQuadColor(mQuad)));
        }
    }
}
