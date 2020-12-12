package com.infinitydheer.themanager.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.datasource.general.GeneralDataStore;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.auth.AuthenticateUser;
import com.infinitydheer.themanager.presentation.view.fragment.logger.LoggerOverviewFragment;
import com.infinitydheer.themanager.presentation.view.fragment.task.TaskOverviewFragment;
import com.infinitydheer.themanager.presentation.view.fragment.settings.SettingsFragment;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticateUser();

        initialiseV();

        if(getFragmentManager().findFragmentById(R.id.fl_main)==null) addFragment(R.id.fl_main, new TaskOverviewFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == S.Constants.REQ_AUTH_USER) {
            if (resultCode == S.Constants.CODE_SUCCESS) {
            } else {
                finish();
            }
        }
    }

    private void authenticateUser(){
        GeneralDataStore dataStore=new GeneralDataStore(this);

        String pi=dataStore.getMainPassword();

        if(pi.equals("")){}
        else{
            Intent intent=new Intent(this, AuthenticateUser.class);
            startActivityForResult(intent,S.Constants.REQ_AUTH_USER);
        }
    }

    private void clearNavigation(){
        int size= mNavigationView.getMenu().size();
        for(int i=0;i<size;i++){
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void navigate(int id){
        switch (id){
            case R.id.nav_task_manager:
                replaceFragment(R.id.fl_main, new TaskOverviewFragment(), false);
                break;
            case R.id.nav_log_manager:
                replaceFragment(R.id.fl_main,new LoggerOverviewFragment(), false);
                break;
            case R.id.nav_settings:
                replaceFragment(R.id.fl_main,new SettingsFragment(),false);
                break;
        }
    }

    private void initialiseV(){
        Toolbar toolBar=findViewById(R.id.tb_main);
        setSupportActionBar(toolBar);
        ActionBar actionBar=getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        mDrawerLayout =findViewById(R.id.dl_main);
        mNavigationView =findViewById(R.id.nv_main);

        setV();
    }

    private void setV(){
        mNavigationView.setNavigationItemSelectedListener(item -> {
            if(!item.isChecked()){
                clearNavigation();
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                navigate(item.getItemId());
            }else{
                mDrawerLayout.closeDrawers();
            }
            return true;
        });
    }
}
