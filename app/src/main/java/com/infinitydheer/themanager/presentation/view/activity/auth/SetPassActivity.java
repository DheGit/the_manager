package com.infinitydheer.themanager.presentation.view.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.datasource.general.GeneralDataStore;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.MainActivity;

public class SetPassActivity extends AppCompatActivity {
    private EditText mPinBox1;
    private EditText mPinBox2;
    private TextView mErrorLabel;
    private Toolbar mToolBar;

    private GeneralDataStore mDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);

        initialiseD();
        initialiseV();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_task,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done_edit:
                setNewPin();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseD(){
        this.mDataStore =new GeneralDataStore(this);
        String curPass= mDataStore.getMainPassword();
        if(curPass.equals("")){

        }else{
            Intent intent=new Intent(this,AuthenticateUser.class);
            startActivityForResult(intent, S.Constants.REQ_AUTH_USER);
        }
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

    private void initialiseV(){
        mToolBar =findViewById(R.id.tb_set_pass);
        setSupportActionBar(mToolBar);

        mPinBox1 =findViewById(R.id.et_new_pin1);
        mPinBox2 =findViewById(R.id.et_new_pin2);
        mErrorLabel =findViewById(R.id.tv_error_pin_mismatch);

        setV();
    }

    private void setNewPin(){
        mErrorLabel.setVisibility(View.INVISIBLE);
        String pass1= mPinBox1.getText().toString();
        String pass2= mPinBox2.getText().toString();

        if(pass1.equals(pass2)){
            mDataStore.setMainPassword(pass1);
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            mErrorLabel.setVisibility(View.VISIBLE);
            mPinBox2.setText("");
        }
    }

    private void setV(){
        mErrorLabel.setVisibility(View.INVISIBLE);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.set_pin_title);
        }
    }
}
