package com.infinitydheer.themanager.presentation.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;

import com.infinitydheer.themanager.presentation.ApplicationGlobals;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void addFragment(int containerId, Fragment fragment){
        final androidx.fragment.app.FragmentTransaction transaction=this.getSupportFragmentManager().beginTransaction();
        transaction.add(containerId,fragment);
        transaction.commit();
    }

    public void replaceFragment(int containerId, Fragment fragment, boolean addToBackStack){
        final androidx.fragment.app.FragmentTransaction fragmentTransaction=this.getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(containerId, fragment);
        if(addToBackStack) fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void removeFragment(int containerId){
        final FragmentManager fragmentManager=getSupportFragmentManager();
        final Fragment fragment=fragmentManager.findFragmentById(containerId);
        final FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public ApplicationGlobals getApp(){
        return (ApplicationGlobals) getApplication();
    }
}