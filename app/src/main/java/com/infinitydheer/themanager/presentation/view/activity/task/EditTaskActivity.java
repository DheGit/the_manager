package com.infinitydheer.themanager.presentation.view.activity.task;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.task.EditTaskFragment;
import com.infinitydheer.themanager.presentation.view.fragment.task.TaskDetailsFragment;

public class EditTaskActivity extends BaseActivity {
    private long mId;
    private int mQuad;
    private boolean mIsSaved =false;
    private boolean mIsNewTask =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initialise();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.fl_edit_task) == null) addFragment(R.id.fl_edit_task, EditTaskFragment.forTask(mId, mQuad));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mIsSaved){
            super.onBackPressed();
        }else{
            AlertDialog.Builder builder;

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                builder=new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }else{
                builder=new AlertDialog.Builder(this);
            }

            builder.setMessage(R.string.discard_task_message)
                    .setNegativeButton(R.string.no_text, (dialogInterface, i) -> dialogInterface.cancel())
                    .setPositiveButton(R.string.discard_text, (dialogInterface, i) -> {
                        mIsSaved =true;
                        onBackPressed();
                    }).create().show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_done_edit:
                saveTask();
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialise(){
        Intent intent=getIntent();

        this.mQuad =intent.getIntExtra(S.Constants.INTENT_QUAD_KEY,1);
        this.mId = intent.getLongExtra(S.Constants.INTENT_TASKID_KEY,-1);
        if(mId ==-1) mIsNewTask =true;

        initialiseV();
    }

    private void initialiseV(){
        Toolbar toolbar=findViewById(R.id.tb_edit_task);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getApp().getQuadColor(this.mQuad)));
        }
    }

    private void saveTask(){
        EditTaskFragment frag=(EditTaskFragment)getSupportFragmentManager().findFragmentById(R.id.fl_edit_task);
        frag.saveTask(mIsNewTask);
        mIsSaved =true;
    }
}
