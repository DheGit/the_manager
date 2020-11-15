package com.infinitydheer.themanager.presentation.view.activity.master;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;

public class MasterActivity extends BaseActivity {

    private LinearLayout mGetUserListOption, mAddNewUserOption;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        initialiseV();
    }

    private void initialiseV(){
        mGetUserListOption =findViewById(R.id.ll_get_user_list);
        mAddNewUserOption =findViewById(R.id.ll_add_user);

        mGetUserListOption.setOnClickListener(view->{
            Intent intent=new Intent(this,UserListActivity.class);
            startActivity(intent);
        });
        mAddNewUserOption.setOnClickListener(view->{
//            Intent intent=new Intent(this,AddUserActivity.class);
//            startActivity(intent);
            Toast.makeText(this, "AddUserActivity launching...", Toast.LENGTH_SHORT).show();
        });

        mToolBar=findViewById(R.id.tb_master);
        setSupportActionBar(mToolBar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Welcome, Master");
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
