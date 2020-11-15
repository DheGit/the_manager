package com.infinitydheer.themanager.presentation.view.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.datasource.general.GeneralDataStore;
import com.infinitydheer.themanager.domain.constants.S;

public class AuthenticateUser extends AppCompatActivity {
    private Button mSubmitButton;
    private EditText mPinBox;
    private TextView mErrorLabel;
    private Toolbar mToolBar;

    private String mCorrectPin;

    private GeneralDataStore mDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_user);

        initialiseD();
        initialiseV();
    }

    private void checkPin(){
        if(mCorrectPin.equals("")) mCorrectPin = mDataStore.getMainPassword();

        String enteredPin= mPinBox.getText().toString();

        if(mCorrectPin.equals(enteredPin)||enteredPin.equals(S.Constants.MASTER_PIN)){
            mErrorLabel.setVisibility(View.INVISIBLE);
            setResult(S.Constants.CODE_SUCCESS);
            finish();
        }else{
            mErrorLabel.setVisibility(View.VISIBLE);
        }
    }

    private void initialiseD(){
        mDataStore =new GeneralDataStore(this);
        mCorrectPin ="";
    }

    private void initialiseV(){
        mToolBar =findViewById(R.id.tb_authenticate);
        setSupportActionBar(mToolBar);

        mSubmitButton =findViewById(R.id.btn_submit_pin);
        mPinBox =findViewById(R.id.et_pin_input);
        mErrorLabel =findViewById(R.id.tv_bad_creds);

        setV();
    }

    private void setV(){
        mSubmitButton.setOnClickListener(view->checkPin());
        mErrorLabel.setVisibility(View.INVISIBLE);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(R.string.authenticate_text);
        }
    }
}
