package com.infinitydheer.themanager.presentation.view.activity.razgo;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.razgo.RazgoListFragment;

public class RazgoListActivity extends BaseActivity implements RazgoListFragment.RazgoListListener{
    long mConvId;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razgo_list);

        initialiseV();

        Intent intent=getIntent();
        this.mConvId =intent.getLongExtra(S.Constants.INTENT_CONVID_KEY, -1);
        if(this.mConvId ==-1) finish();

        initialise();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof RazgoListFragment){
            RazgoListFragment listFragment=(RazgoListFragment)fragment;
            listFragment.setListener(this);
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,RazgoActivityMain.class);
        startActivity(intent);
    }

    @Override
    public void setTitle(String partnerId) {
        this.mActionBar.setTitle(partnerId);
    }

    private void initialise(){
        addFragment(R.id.fl_razgo_list, RazgoListFragment.forConvId(this.mConvId));
    }

    private void initialiseV(){
        Toolbar toolbar=findViewById(R.id.tb_razgo_list);
        setSupportActionBar(toolbar);

        this.mActionBar =getSupportActionBar();
        if(this.mActionBar !=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
