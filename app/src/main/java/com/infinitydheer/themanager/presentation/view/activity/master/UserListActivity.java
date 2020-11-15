package com.infinitydheer.themanager.presentation.view.activity.master;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.master.UserListFragment;

public class UserListActivity extends BaseActivity {
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        initialiseV();

        addFragment(R.id.fl_user_list,new UserListFragment());
    }

    private void initialiseV(){
        mToolBar=findViewById(R.id.tb_user_list);
        setSupportActionBar(mToolBar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("All Users");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
