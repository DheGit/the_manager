package com.infinitydheer.themanager.presentation.view.fragment.razgo;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.crypt.Nkrpt;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;

public class RazgoEntryFragment extends BaseFragment {
    private static final int STAT_ACCOUNT_REVOKED=3;
    private static final int STAT_FAILURE_NO_USER = 2;
    private static final int STAT_SUCCESS = 1;
    private static final int STAT_FAILURE = 0;

    private static final String ACCOUNT_REVOKED_TITLE="Oops!";
    private static final String ACCOUNT_REVOKED_MESSAGE="There is a problem in logging in to your account. Please bear with us.";


    private Button mSubmitButton;
    private EditText mUserNameBox, mPassBox;
    private ProgressBar mProgressBar;
    private TextView mErrorLabel;

    private RazgoEntryListener mListener;


    public RazgoEntryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_razgo_entry, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setV();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseV();
    }

    private void initialiseV() {
        Activity activity = getActivity();
        if (activity == null) return;

        mSubmitButton = activity.findViewById(R.id.btn_submit_creds);
        mUserNameBox = activity.findViewById(R.id.et_username);
        mPassBox = activity.findViewById(R.id.et_password);
        mErrorLabel = activity.findViewById(R.id.tv_error_creds);
        mProgressBar = activity.findViewById(R.id.pb_basic);
    }

    private void setV() {
        mProgressBar.setVisibility(View.GONE);
        mErrorLabel.setVisibility(View.GONE);
        mSubmitButton.setOnClickListener(view -> checkCreds());
    }

    private void checkCreds() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorLabel.setVisibility(View.GONE);

        String userN = this.mUserNameBox.getText().toString();
        String pass = this.mPassBox.getText().toString();

        userN = Nkrpt.processDef(userN);
        pass = Nkrpt.processDef(pass);

        final String finalPass = pass;
        final String finalUserN = userN;

        FirebaseFirestore.getInstance()
                .collection(S.RConst.COLL_META)
                .document(userN)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot res = task.getResult();

                            if (res != null && res.exists()) {
                                String correctPass = res.getString(S.RConst.K_UPASS);

                                boolean goodAccount = true;
                                try {
                                    goodAccount = res.getBoolean(S.RConst.K_ACCOUNT_ALLOWED);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (goodAccount)
                                    RazgoEntryFragment.this.postGet(STAT_SUCCESS, finalUserN, correctPass, finalPass);
                                else
                                    RazgoEntryFragment.this.postGet(STAT_ACCOUNT_REVOKED,finalUserN,correctPass,finalPass);
                            }else {
                                RazgoEntryFragment.this.postGet(STAT_FAILURE_NO_USER, "", "", "");
                            }
                        } else {
                            RazgoEntryFragment.this.postGet(STAT_FAILURE, "", "", "");
                        }
                    }
                });
    }

    private void postGet(int statusCode, String userN, String POY, String POYentered) {
        mProgressBar.setVisibility(View.GONE);

        if (statusCode == STAT_SUCCESS) {
            if (POY.equals(POYentered)) {
                if (this.mListener != null) mListener.launchRazgoMain(userN);
            } else {
                mErrorLabel.setVisibility(View.VISIBLE);
                mErrorLabel.setText(R.string.error_wrong_creds);
            }
        } else if (statusCode == STAT_FAILURE) {
            mErrorLabel.setVisibility(View.VISIBLE);
            mErrorLabel.setText(R.string.error_bad_internet);
        } else if (statusCode == STAT_FAILURE_NO_USER) {
            mErrorLabel.setVisibility(View.VISIBLE);
            mErrorLabel.setText(R.string.error_bad_username);
        }else if(statusCode==STAT_ACCOUNT_REVOKED){
            if (POY.equals(POYentered)) {
                showBasicDialog(ACCOUNT_REVOKED_MESSAGE,ACCOUNT_REVOKED_TITLE);
            } else {
                mErrorLabel.setVisibility(View.VISIBLE);
                mErrorLabel.setText(R.string.error_wrong_creds);
            }
        }
    }

    public void setListener(RazgoEntryListener list) {
        this.mListener = list;
    }

    public interface RazgoEntryListener {
        void launchRazgoMain(String userId);
    }
}
