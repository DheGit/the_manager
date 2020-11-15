package com.infinitydheer.themanager.presentation.view.activity.razgo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;
import com.infinitydheer.themanager.presentation.utils.NotificationUtils;
import com.infinitydheer.themanager.presentation.view.activity.BaseActivity;
import com.infinitydheer.themanager.presentation.view.fragment.razgo.ConvlistFragment;
import com.infinitydheer.themanager.presentation.view.fragment.razgo.RazgoEntryFragment;

public class RazgoActivityMain extends BaseActivity implements
        ConvlistFragment.ConvFragmentListener, RazgoEntryFragment.RazgoEntryListener{

    private ActionBar mActionBar;

    private RazgoDataRepository mRazgoRepository;

    private String mSelfId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razgo);

        initialiseV();

        start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_razgo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_log_out:
                confirmLogOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof ConvlistFragment){
            ConvlistFragment frag=(ConvlistFragment) fragment;
            frag.setListener(this);

        }else if(fragment instanceof RazgoEntryFragment){
            RazgoEntryFragment frag=(RazgoEntryFragment) fragment;
            frag.setListener(this);

        }
    }

    @Override
    public void onConvClicked(long id) {
        Intent intent=new Intent(this,RazgoListActivity.class);

        intent.putExtra(S.Constants.INTENT_CONVID_KEY,id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    @Override
    public void launchRazgoMain(String userId) {
        this.mRazgoRepository.setSelfId(userId);

        ApplicationGlobals.SELF_ID=userId;
        ApplicationGlobals.SELF_ID_UP=Nkrpt.unprocessDef(userId);
        this.mSelfId =userId;

        this.mRazgoRepository.syncUserData(100);
//        this.mRazgoRepository.sync();
        this.mRazgoRepository.setUpdateListener();

        loadRazgoMain();
    }

    private void confirmLogOut(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.yes_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                RazgoActivityMain.this.logOut();
            }
        }).setNegativeButton(R.string.no_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setTitle(R.string.log_out_action).setMessage(R.string.log_out_message)
                .create().show();
    }

    private void start() {
        this.mRazgoRepository = RazgoDataRepository.getInstance(this);

        mSelfId = ApplicationGlobals.SELF_ID;
        if (mSelfId.equals("")) {
            mSelfId = this.mRazgoRepository.getSelfId();
        }

        if (this.mSelfId.equals("")) {
            loadRazgoEntry();
        } else {
            if(ApplicationGlobals.SELF_ID.equals("")){
                ApplicationGlobals.SELF_ID= mSelfId;
                ApplicationGlobals.SELF_ID_UP=Nkrpt.unprocessDef(mSelfId);
            }

            loadRazgoMain();
        }
    }

    private void initialiseV(){
        Toolbar bar=findViewById(R.id.tb_razgo);
        setSupportActionBar(bar);

        this.mActionBar =getSupportActionBar();
        if(mActionBar !=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initialiseD(){
        NotificationUtils.getInstance(this).clearAllNotifications();
    }

    private void loadRazgoEntry(){
        replaceFragment(R.id.fl_razgo,new RazgoEntryFragment(),false);
    }

    private void loadRazgoMain(){
        replaceFragment(R.id.fl_razgo,new ConvlistFragment(),false);
    }

    private void logOut(){
        this.mRazgoRepository.setSelfId("");
        ApplicationGlobals.SELF_ID_UP="";
        ApplicationGlobals.SELF_ID="";

        this.mRazgoRepository.removeUpdateListener();
        this.mRazgoRepository.destroyData();

        loadRazgoEntry();
    }
}
