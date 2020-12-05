package com.infinitydheer.themanager.presentation.view.fragment.master;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinitydheer.themanager.data.repository.RazgoMasterDataRepository;
import com.infinitydheer.themanager.data.service.CleanConversationService;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.interactor.master.UCUserDetails;
import com.infinitydheer.themanager.presentation.model.ConvModel;
import com.infinitydheer.themanager.presentation.model.UserModel;
import com.infinitydheer.themanager.presentation.presenter.master.UserDetailsPresenter;
import com.infinitydheer.themanager.presentation.view.adapter.MasterConvListAdapter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.master.UserDetailsView;

import com.infinitydheer.themanager.R;

import java.util.ArrayList;

public class UserDetailsFragment extends BaseFragment implements UserDetailsView {
    private static final String BUNDLE_USERNAME = "man_bundle_user_key";
    private static final String BUNDLE_PASS = "man_bundle_user_pass";

    private static final String ALL_CONVERSATIONS_TEXT = "All Conversations with this user: ";
    private static final String LOADING_TEXT = "Loading all conversation...";

    private LinearLayout mRevokeAccessOption;
    private TextView mUserLabel, mPassText, mConvListHeader, mRevokeAccessLabel;
    private RecyclerView mConvRecycler;
    private MasterConvListAdapter mAdapter;

    private UserModel mUserModel;
    private UserDetailsPresenter mPresenter;

    private boolean mIsAccessAllowed = true;

    public static UserDetailsFragment forUser(UserModel toBeShown) {
        final Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PASS, toBeShown.getUserPass());
        bundle.putString(BUNDLE_USERNAME, toBeShown.getUserId());

        final UserDetailsFragment fragment = new UserDetailsFragment();
        fragment.setArguments(bundle);
        fragment.mUserModel = toBeShown;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_master_user_details, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        renderViews();

        mPresenter.setView(this);
        mPresenter.setUserId(getUserModel().getUserId());
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        mPresenter.onStop();
        mPresenter.setView(null);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        renderTestConvList();
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//        mPresenter.onPause();
//        mPresenter.setView(null);
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialiseV();
        initialiseD();

        mPresenter.setView(this);
        mPresenter.setUserId(getUserModel().getUserId());
    }

    private void initialiseV() {
        Activity activity = getActivity();
        if (activity == null) return;

        mUserLabel = activity.findViewById(R.id.tv_user_details_name);
        mPassText = activity.findViewById(R.id.tv_user_details_pass);
        mConvListHeader = activity.findViewById(R.id.tv_user_details_static_conv_header);
        mConvRecycler = activity.findViewById(R.id.rv_user_details_conv_list);
        mRevokeAccessOption = activity.findViewById(R.id.ll_user_details_revoke);
        mRevokeAccessLabel = activity.findViewById(R.id.tv_user_revoke_access);

        mAdapter = new MasterConvListAdapter();
        mAdapter.setListener(new MasterConvListAdapter.MasterConvListListener() {
            @Override
            public void onConvClicked(ConvModel model) {
                showClearConversationDialog(model.getConvid());
            }
        });

        mConvRecycler.setLayoutManager(new LinearLayoutManager(activity));
        mConvRecycler.setAdapter(mAdapter);

        mRevokeAccessOption.setOnClickListener(view -> mPresenter.toggleAccessStatus());

        hideProgress();
    }

    private void initialiseD() {
//        mTestRepo=RazgoMasterDataRepository.getInstance();
        mPresenter = new UserDetailsPresenter(new UCUserDetails(RazgoMasterDataRepository.getInstance()));
    }

    private void renderViews() {
        if (mUserModel == null) mUserModel = getUserModel();

        mUserLabel.setText(mUserModel.getUserId());
        mPassText.setText(mUserModel.getUserPass());
    }

    @Override
    public void onNewConversationReceived(ConvModel convModel) {
        if (convModel == null) return;

        mAdapter.addConvModel(convModel);
    }

    @Override
    public void onAccessAllowed(boolean allowed) {
        mIsAccessAllowed = allowed;
        setAccessOptionTextView();
    }

    @Override
    public void resetConvList() {
        this.mAdapter.setData(new ArrayList<>());
        mAdapter.notifyDataSetChanged();
    }

    public void setAccessOptionTextView() {
        if (mRevokeAccessOption.getVisibility() == View.GONE)
            mRevokeAccessOption.setVisibility(View.VISIBLE);

        String correctString = mIsAccessAllowed ? "Revoke User Access" : "Grant User Access";
        mRevokeAccessLabel.setText(correctString);
    }

    public UserModel getUserModel() {
        if (mUserModel == null) {
            Bundle args = getArguments();
            if (args == null) mUserModel = new UserModel("NoName", "NoPass");
            else
                mUserModel = new UserModel(args.getString(BUNDLE_USERNAME), args.getString(BUNDLE_PASS));
        }

        return mUserModel;
    }

    private void showClearConversationDialog(long convId){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.clean_conversation_title)
                .setMessage(R.string.clean_conversation_message)
                .setPositiveButton(R.string.yes_text, (dialogInterface, i) -> {
                    UserDetailsFragment.this.cleanConversation(convId);
                    UserDetailsFragment.this.showToast(getString(R.string.toast_clean_in_progress));
                })
                .setNegativeButton(R.string.no_text, (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create().show();
    }

    private void cleanConversation(long convid){
        Activity activity=getActivity();
        if(activity==null) return;

        Intent intent=new Intent(activity, CleanConversationService.class);
        intent.putExtra(S.Constants.INTENT_CONV_ID_CLEAN_KEY, convid);

        activity.startService(intent);
    }

    @Override
    public void hideProgress() {
        mConvListHeader.setText(ALL_CONVERSATIONS_TEXT);
    }

    @Override
    public void showProgress() {
        mConvListHeader.setText(LOADING_TEXT);
    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }
}
