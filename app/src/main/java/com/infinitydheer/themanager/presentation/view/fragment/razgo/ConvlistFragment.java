package com.infinitydheer.themanager.presentation.view.fragment.razgo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.executor.WorkExecutor;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.interactor.razgo.UCConvList;
import com.infinitydheer.themanager.presentation.UIThread;
import com.infinitydheer.themanager.presentation.model.ConvModel;
import com.infinitydheer.themanager.presentation.presenter.razgo.ConvlistPresenter;
import com.infinitydheer.themanager.presentation.view.adapter.ConvListAdapter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.razgo.ConvlistView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConvlistFragment extends BaseFragment implements ConvlistView {
    private ConvListAdapter mListAdapter = null;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;
    private RecyclerView mConvRecycler;

    private ConvFragmentListener mListener;

    private ConvlistPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_conv_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseV();
        initialiseD();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mPresenter.setView(null);
        this.mPresenter.onStop();
    }
    @Override
    public void onStart() {
        super.onStart();
        this.mPresenter.setView(this);
        this.mPresenter.onStart();
    }

    @Override
    public void populateList(List<ConvModel> models) {
        mListAdapter.setData(models);
    }

    @Override
    public void addConv(ConvModel model) {
        mListAdapter.addConv(model);
    }

    @Override
    public void hideProgress() {
        this.mProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void showProgress() {
        this.mProgressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void hideRetry() {
        //implement this
    }
    @Override
    public void showRetry() {
        //implement this
    }

    public void setListener(ConvFragmentListener l) {
        this.mListener = l;
    }

    public void initialiseD() {
        UCConvList inter = new UCConvList(RazgoDataRepository.getInstance(getActivity()),
                WorkExecutor.getInstance(),
                UIThread.getInstance());

        this.mPresenter = new ConvlistPresenter(inter);
        this.mPresenter.setView(this);
    }

    public void initialiseV() {
        Activity activity = getActivity();
        if (activity == null) return;

        this.mProgressBar = activity.findViewById(R.id.pb_basic);
        this.mConvRecycler = activity.findViewById(R.id.rv_razgomain);
        this.mFab =activity.findViewById(R.id.fab_new_conv);

        setV();
    }

    @Override
    public void loadRazgoRoom(long convid) {
        this.mListener.onConvClicked(convid);
    }

    @Override
    public void showToast(String message) {
        super.showToast(message);
    }

    public void makeNewConv(){
        View textGetter=LayoutInflater.from(getActivity()).inflate(R.layout.et_dialog,null);
        EditText textBox=textGetter.findViewById(R.id.et_easter_text);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.new_conv_title)
                .setView(textGetter)
                .setMessage(R.string.new_conv_message)
                .setPositiveButton(R.string.submit_text,
                        (DialogInterface.OnClickListener) (dialogInterface, i) ->
                                ConvlistFragment.this.mPresenter.createConversationWith(textBox.getText().toString()))
                .setNegativeButton(R.string.cancel_text,
                        (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.dismiss())

                .create().show();
    }

    public void setV(){
        mListAdapter =new ConvListAdapter(new ArrayList<>(), convClickedListener);

        this.mConvRecycler.setAdapter(mListAdapter);
        this.mConvRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mConvRecycler.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),
                DividerItemDecoration.VERTICAL));

        this.mFab.setOnClickListener(view->makeNewConv());
    }

    public ConvListAdapter.OnConvClickedListener convClickedListener= convid -> {
        if(ConvlistFragment.this.mListener !=null)
            ConvlistFragment.this.mListener.onConvClicked(convid);
    };

    public interface ConvFragmentListener{
        void onConvClicked(long id);
    }
}
