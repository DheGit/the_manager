package com.infinitydheer.themanager.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.presentation.model.UserModel;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserHolder> {
    private List<UserModel> mData;

    private UserListAdapterListener mListener;

    public UserListAdapter(){
        this.mData =new ArrayList<>();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.holder_user,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserModel user= mData.get(position);

        holder.userName.setText(user.getUserId());

        holder.itemView.setOnClickListener(view-> UserListAdapter.this.mListener.onTaskClicked(user));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<UserModel> newData){
        this.mData =newData;
        notifyDataSetChanged();
    }

    public void setListener(UserListAdapterListener listener){mListener=listener;}

    public class UserHolder extends RecyclerView.ViewHolder{
        private TextView userName;

        public UserHolder(View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.tv_user_name);
        }
    }

    public interface UserListAdapterListener{
        void onTaskClicked(UserModel model);
    }
}
