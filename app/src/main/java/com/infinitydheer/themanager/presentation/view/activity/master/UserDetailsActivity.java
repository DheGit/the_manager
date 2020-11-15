package com.infinitydheer.themanager.presentation.view.activity.master;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S.*;
import com.infinitydheer.themanager.presentation.model.UserModel;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.master.UserDetailsFragment;

public class UserDetailsActivity extends BaseActivity {
    private Toolbar mToolBar;

    private UserModel mModelShown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        initialiseD();
        initialiseV();

        addFragment(R.id.fl_user_details, UserDetailsFragment.forUser(mModelShown));
    }

    public void initialiseD(){
        Intent intent=getIntent();

        mModelShown=intent.getParcelableExtra(Constants.INTENT_USER_KEY);
    }

    public void initialiseV(){
        mToolBar=findViewById(R.id.tb_user_details);
        setSupportActionBar(mToolBar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mModelShown.getUserId());
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
