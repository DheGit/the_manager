package com.infinitydheer.themanager.presentation.view.activity.task;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.task.TaskDetailsFragment;

public class TaskDetailsActivity extends BaseActivity {
    private long mId;
    private int mQuad;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit_task:
                launchEditTask();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_task_details, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        initialise();

        addFragment(R.id.fl_taskdetails,TaskDetailsFragment.forTask(this.mId));
    }

    private void initialise(){
        Intent intent=getIntent();
        this.mId =intent.getLongExtra(S.Constants.INTENT_TASKID_KEY, -1);
        this.mQuad =intent.getIntExtra(S.Constants.INTENT_QUAD_KEY,1);
        initialiseV();
    }

    private void initialiseV(){
        Toolbar bar=findViewById(R.id.tb_task_details);
        setSupportActionBar(bar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getApp().getQuadColor(mQuad)));
        }
    }

    private void launchEditTask(){
        Intent intent = new Intent(this, EditTaskActivity.class);

        intent.putExtra(S.Constants.INTENT_TASKID_KEY, mId);
        intent.putExtra(S.Constants.INTENT_QUAD_KEY, mQuad);

        startActivity(intent);
    }
}
