package com.infinitydheer.themanager.presentation.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.infinitydheer.themanager.R;

public class BaseFragment extends Fragment {

    protected void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Other methods common to all kinds of fragments
    protected void showBasicDialog(String message, String title){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setMessage(message).setTitle(title).show();
    }
}
