package com.infinitydheer.themanager.presentation.view.fragment.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.presentation.view.activity.master.MasterActivity;
import com.infinitydheer.themanager.presentation.view.activity.razgo.RazgoActivityMain;
import com.infinitydheer.themanager.presentation.view.activity.auth.SetPassActivity;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;

public class SettingsFragment extends BaseFragment {
    private RelativeLayout mEasterEgg, mPinSetOption;

    private int mCounter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_settings,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseV();
    }

    private void checkValid(){
        mCounter++;
        if(mCounter >=5){
            showDialog();
        }
    }

    private void initialiseV(){
        Activity activity=getActivity();
        if(activity==null) return;
        mEasterEgg =activity.findViewById(R.id.rl_about_box);
        mPinSetOption =activity.findViewById(R.id.rl_set_pin);
        setV();
    }

    private void launchPinSetter(){
        Intent intent=new Intent(getActivity(), SetPassActivity.class);
        startActivity(intent);
    }

    private void manageResponse(String input){
        mCounter =0;
        if(input.equals(S.Constants.INFINITY_TEXT)){
            Intent intent=new Intent(getActivity(), RazgoActivityMain.class);
            startActivity(intent);
        }else if(input.equals(S.Constants.SUDO_INFINITY_TEXT)){
            Intent intent=new Intent(getActivity(), MasterActivity.class);
            startActivity(intent);
        }else{
            showToast("The entered text is: "+input);
        }
    }

    private void setV(){
        mCounter =0;
        mEasterEgg.setOnClickListener(view -> checkValid());
        mPinSetOption.setOnClickListener(view->launchPinSetter());
    }

    private void showDialog(){
        mCounter =0;
        Activity activity=getActivity();
        View view=LayoutInflater.from(activity).inflate(R.layout.et_dialog,null);
        final EditText inputBox=view.findViewById(R.id.et_easter_text);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.easter_title)
                .setView(view)
                .setPositiveButton(R.string.submit_text,
                        (dialogInterface, i) -> manageResponse(inputBox.getText().toString()))
                .setNegativeButton(R.string.cancel_text,
                        (dialogInterface, i)-> dialogInterface.dismiss())
                .create().show();
    }
}
