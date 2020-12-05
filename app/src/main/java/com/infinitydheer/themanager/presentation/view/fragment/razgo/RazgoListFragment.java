package com.infinitydheer.themanager.presentation.view.fragment.razgo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.executor.WorkExecutor;
import com.infinitydheer.themanager.data.repository.RazgoDataRepository;
import com.infinitydheer.themanager.domain.constants.S;
import com.infinitydheer.themanager.domain.interactor.razgo.UCRazgoList;
import com.infinitydheer.themanager.domain.utils.CommonUtils;
import com.infinitydheer.themanager.presentation.UIThread;
import com.infinitydheer.themanager.presentation.model.RazgoModel;
import com.infinitydheer.themanager.presentation.presenter.razgo.RazgoListPresenter;
import com.infinitydheer.themanager.presentation.view.adapter.RazgoListAdapter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.razgo.RazgoListView;

import java.util.List;

public class RazgoListFragment extends BaseFragment implements RazgoListView{
    private static final String KEY_CONV_ID="conv_id_bundle_key";

    private static final long SCROLL_TO_BOTTOM_THRESHOLD=300;

    private RazgoListListener mListener;

    private long mConvId;
    private String mPartnerId;

    private Button mSubmitButton;
    private EditText mEntryBox;
    private ProgressBar mProgressBar;
    private RazgoListAdapter mAdapter;
    private RecyclerView mRazgoRecycler;

    private RazgoListPresenter mPresenter;

    private long mTotalVerticalScroll;
    private boolean mShouldScrollToBottom;

    public static RazgoListFragment forConvId(long convid){
        final Bundle options=new Bundle();
        options.putLong(KEY_CONV_ID,convid);
        final RazgoListFragment frag=new RazgoListFragment();
        frag.setArguments(options);
        return frag;
    }

    public RazgoListFragment(){
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_razgo_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseD();
        initialiseV();
    }

    @Override
    public void loadRazgos(List<RazgoModel> models) {
        int initSize=mAdapter.getItemCount();
        boolean empty = (mAdapter.getItemCount()==0);
        this.mAdapter.addRazgosToStart(models); //TODO: Reverse the layout and stack from end to avoid such hell

        if(empty){
            this.mRazgoRecycler.scrollToPosition(mAdapter.getItemCount()-1);
//            mAdapter.notifyItemRangeInserted(1,models.size());
            mAdapter.notifyDataSetChanged();
        }
        else mRazgoRecycler.post(() -> {
            RazgoListFragment.this.mAdapter.notifyDataSetChanged();
            int curSize=RazgoListFragment.this.mAdapter.getItemCount();
            RazgoListFragment.this.mRazgoRecycler.scrollToPosition(curSize-initSize);
            //TODO Handle this more elegantly
        });
//        else mRazgoRecycler.post(() -> RazgoListFragment.this.mAdapter.notifyItemRangeInserted(1,models.size()));
    }

    @Override
    public void addRazgo(RazgoModel model, boolean self) {
        this.mAdapter.addRazgoToEnd(model);
        if(self||mShouldScrollToBottom) this.mRazgoRecycler.scrollToPosition(mAdapter.getItemCount()-1);
        else{
            mRazgoRecycler.scrollBy(0,50);
        }
    }

    @Override
    public void setSent(long oldId, long newId) {
        mAdapter.setSent(oldId, newId);
    }

    @Override
    public void onStart() {
        super.onStart();

        this.mPresenter.setView(this);
        this.mPresenter.setConvid(getConvId());
        this.mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        this.mPresenter.setView(null);
        this.mPresenter.onStop();
    }

    @Override
    public boolean isInternet() {
        Activity activity=getActivity();
        if(activity==null) return false;

        ConnectivityManager cm=(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null&&cm.getActiveNetworkInfo()!=null){
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }

        return false;
    }

    @Override
    public void setPartnerId(String partnerId) {
        this.mPartnerId =partnerId;
        this.mListener.setTitle(partnerId);
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
    //Implement this
    }
    @Override
    public void showRetry() {
    //Implement this
    }

    private void initialiseD(){
        this.mConvId =-1;

        UCRazgoList inter=new UCRazgoList(WorkExecutor.getInstance(),
                RazgoDataRepository.getInstance(getActivity()),
                UIThread.getInstance());

        this.mPresenter =new RazgoListPresenter(inter);
        this.mPresenter.setView(this);
    }

    private void initialiseV(){
        Activity activity=getActivity();
        if(activity==null) return;

        this.mProgressBar =activity.findViewById(R.id.pb_basic);
        this.mEntryBox =activity.findViewById(R.id.et_razgo_input);
        this.mSubmitButton =activity.findViewById(R.id.btn_next_razgo);
        this.mRazgoRecycler =activity.findViewById(R.id.rv_razgolist);

        this.mAdapter = new RazgoListAdapter(activity);
        this.mAdapter.setListener(end->{
            RazgoListFragment.this.mPresenter.getRazgos(end);
        });

        this.mRazgoRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRazgoRecycler.setAdapter(mAdapter);
        mRazgoRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalVerticalScroll-=dy;
                mShouldScrollToBottom = mTotalVerticalScroll <= SCROLL_TO_BOTTOM_THRESHOLD;
            }
        });

        this.mSubmitButton.setOnClickListener(view -> RazgoListFragment.this.submitRazgo());
    }

    public long getConvId() {
        Bundle args=getArguments();

        if(args==null) return 1;
        if(mConvId ==-1) mConvId =args.getLong(KEY_CONV_ID,-1);

        return mConvId;
    }

    public void setListener(RazgoListListener liste){
        this.mListener =liste;
    }

    private void submitRazgo(){
        String content=this.mEntryBox.getText().toString();
        content=CommonUtils.removeUselessExtremes(content);
        if(content.length()==0) return;

        mEntryBox.setText("");
        this.mPresenter.submitRazgo(content);
    }

    public interface RazgoListListener{
        void setTitle(String partnerId);
    }
}
