package com.infinitydheer.themanager.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.presentation.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {
    private List<TaskModel> mData;
    private TaskListAdapterListener mListener;

    private int mNumSelected;
    private boolean mIsSelectMode;

    public TaskListAdapter(TaskListAdapterListener liste){
        this.mListener = liste;
        this.mNumSelected =0;
        mData=new ArrayList<>();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View made=inflater.inflate(R.layout.holder_task, parent, false);
        return new TaskHolder(made);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        final TaskModel model= mData.get(position);

        holder.taskName.setText(model.getName());

        if(mIsSelectMode) holder.checkBox.setVisibility(View.VISIBLE);
        else holder.checkBox.setVisibility(View.GONE);

        holder.checkBox.setChecked(model.isSelected());

        holder.itemView.setOnClickListener(view -> {
            if(mIsSelectMode){
                holder.checkBox.setChecked(!model.isSelected());

                if(model.isSelected()){
                    --mNumSelected;

                    if(mNumSelected <=0){
                        mIsSelectMode =false;

                        TaskListAdapter.this.notifyDataSetChanged();
                        TaskListAdapter.this.mListener.setSelectMode(false);
                    }
                }
                else ++mNumSelected;

                 model.setSelected(!model.isSelected());
            }else{
                if(TaskListAdapter.this.mListener !=null){
                    TaskListAdapter.this.mListener.onTaskClicked(model);
                }
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            if(TaskListAdapter.this.mIsSelectMode) return true;
            else {
                TaskListAdapter.this.mIsSelectMode = true;
                model.setSelected(true);

                ++mNumSelected;

                TaskListAdapter.this.notifyDataSetChanged();
                TaskListAdapter.this.mListener.setSelectMode(true);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private CheckBox checkBox;

        public TaskHolder(View itemView) {
            super(itemView);

            taskName=itemView.findViewById(R.id.tv_task_name_holder);
            checkBox=itemView.findViewById(R.id.cb_task_checked_holder);
        }
    }

    public void clearSelected(){
        mIsSelectMode =false;
        for(TaskModel model: mData) model.setSelected(false);
        notifyDataSetChanged();
    }

    public List<Long> getSelected(){
        List<Long> res=new ArrayList<>();

        for(TaskModel model: mData){
            if(model.isSelected()){
                res.add(model.getId());
            }
        }

        return res;
    }

    public void removeTasks(List<Long> taskIds){
        for(long id: taskIds){
            int toBeRemoved=-1;
            for(int i=0;i<getItemCount();i++){
                if(mData.get(i).getId()==id){
                    toBeRemoved=i;
                    break;
                }
            }
            mData.remove(toBeRemoved);
            notifyItemRemoved(toBeRemoved);
        }
    }

    public void setData(List<TaskModel> datas){
        this.mData =datas;
        notifyDataSetChanged();
    }

    public interface TaskListAdapterListener {
        void onTaskClicked(TaskModel taskModel);
        void setSelectMode(boolean sm);
    }
}
