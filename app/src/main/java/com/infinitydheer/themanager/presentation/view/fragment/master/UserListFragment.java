package com.infinitydheer.themanager.presentation.view.fragment.master;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.repository.RazgoMasterDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.interactor.master.UCUserList;
import com.infinitydheer.themanager.domain.repository.RazgoMasterRepository;
import com.infinitydheer.themanager.presentation.model.UserModel;
import com.infinitydheer.themanager.presentation.presenter.master.UserListPresenter;
import com.infinitydheer.themanager.presentation.view.activity.master.UserDetailsActivity;
import com.infinitydheer.themanager.presentation.view.adapter.UserListAdapter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.master.UserListView;

import java.util.ArrayList;
import java.util.List;

public class UserListFragment extends BaseFragment implements UserListView {
    private ProgressBar mProgressBar;

    private RecyclerView mUserRecycler;
    private UserListAdapter mAdapter;

    private UserListPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_master_user_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialiseV();
        initialiseD();

        mPresenter.setView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setView(this);
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        mPresenter.onStop();
        mPresenter.setView(null);
    }

    private void initialiseV(){
        Activity activity=getActivity();
        if(activity==null) return;

        this.mProgressBar=activity.findViewById(R.id.pb_basic);
        this.mUserRecycler =activity.findViewById(R.id.rv_user_list);
        this.mAdapter=new UserListAdapter();

        mAdapter.setListener(new UserListAdapter.UserListAdapterListener() {
            @Override
            public void onTaskClicked(UserModel model) {
                launchDetailScreen(model);
//                showToast("Launching details screen...");
            }
        });

        mUserRecycler.setLayoutManager(new LinearLayoutManager(activity));
        mUserRecycler.setAdapter(mAdapter);

        hideProgress();
    }

    private void initialiseD(){
        RazgoMasterRepository masterRepository= RazgoMasterDataRepository.getInstance();
        mPresenter=new UserListPresenter(new UCUserList(masterRepository));
    }

    private void launchDetailScreen(UserModel model){
        Intent intent=new Intent(getActivity(), UserDetailsActivity.class);

        intent.putExtra(S.Constants.INTENT_USER_KEY,model);

        startActivity(intent);
    }

    @Override
    public void populateList(List<UserModel> userModelList){
        mAdapter.setData(userModelList);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideRetry() {

    }
    @Override
    public void showRetry() {

    }
}
