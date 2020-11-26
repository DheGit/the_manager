package com.infinitydheer.themanager.presentation.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.utils.CalendarUtils;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;
import com.infinitydheer.themanager.presentation.model.RazgoModel;

import java.util.ArrayList;
import java.util.List;

public class RazgoListAdapter extends RecyclerView.Adapter<RazgoListAdapter.RazgoHolder> {
    private List<RazgoModel> mData;
    private String mSelfId;

    private long minId;

    private RazgoListAdapterListener listener;

    public RazgoListAdapter(Context context) {
        this.mData = new ArrayList<>();
        this.mSelfId = ApplicationGlobals.SELF_ID_UP;
        this.minId=-1;
    }

    @NonNull
    @Override
    public RazgoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.holder_razgo,parent,false);
        return new RazgoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RazgoHolder holder, int position) {
        final RazgoModel model= mData.get(position);
        boolean askForMore = (position == 3);
        long id=model.getId();
        boolean self=false;

        String prevDate="";
        if(position!=0) prevDate= mData.get(position-1).getDatetime();

        if(model.getSender().equals(mSelfId)){
            self=true;
            holder.completeLayout.setGravity(Gravity.END);
            holder.razgoBox.setBackgroundResource(R.drawable.razgo_shape_self);
        }else{
            holder.completeLayout.setGravity(Gravity.START);
            holder.razgoBox.setBackgroundResource(R.drawable.razgo_shape_other);
        }

        holder.razgoStatus_unsent.setVisibility(View.INVISIBLE);
        holder.razgoStatus_sent.setVisibility(View.INVISIBLE);

        if(self){
            if(model.isSent()) holder.razgoStatus_sent.setVisibility(View.VISIBLE);
            else holder.razgoStatus_unsent.setVisibility(View.VISIBLE);
        }

        holder.contentBox.setText(model.getContent());

        holder.senderBox.setText(model.getSender());

        holder.timeBox.setText(model.getDatetime().substring(10)); //Improve this

        if(prevDate.equals("")){
            holder.dateBubble.setVisibility(View.VISIBLE);
            holder.dateBubble.setText(CalendarUtils.convertToReadableDate(model.getDatetime()));
        }else {
            if (CalendarUtils.isSameDay(model.getDatetime(), prevDate)) {
                holder.dateBubble.setVisibility(View.GONE);
            } else {
                holder.dateBubble.setVisibility(View.VISIBLE);
                holder.dateBubble.setText(CalendarUtils.convertToReadableDate(model.getDatetime()));
            }
        }

        if(askForMore) listener.onRequestRazgos(this.minId-1);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addRazgoToEnd(RazgoModel model){
        this.mData.add(model);
        notifyItemInserted(getItemCount()-1);
    }
    public void addRazgosToStart(List<RazgoModel> razgoModels){
        this.minId=razgoModels.get(0).getId();

        razgoModels.addAll(mData);
        mData.clear();
//        mData =razgoModels;
        mData.addAll(razgoModels);

        notifyDataSetChanged();
    }

    public void setSent(long oldId, long newId){
        int curInd=mData.size()-1;

        for(int cntr=0;cntr<20&&curInd>=0;cntr++,curInd--){
            if(mData.get(curInd).getId()==oldId){
                mData.get(curInd).setId(newId);

                mData.get(curInd).setSent(true);
                notifyItemChanged(curInd);
            }
        }
    }

    public void setListener(RazgoListAdapterListener listener){
        this.listener=listener;
    }

    public class RazgoHolder extends RecyclerView.ViewHolder{
        private TextView contentBox, timeBox, senderBox, dateBubble;
        private RelativeLayout completeLayout, razgoBox;
        private ImageView razgoStatus_sent, razgoStatus_unsent;

        public RazgoHolder(View itemView) {
            super(itemView);

            completeLayout=itemView.findViewById(R.id.rl_razgo_main_container);

            dateBubble=itemView.findViewById(R.id.tv_date_bubble);

            razgoBox=itemView.findViewById(R.id.rl_razgo_sub_container);

            senderBox=itemView.findViewById(R.id.tv_razgo_sender);
            contentBox=itemView.findViewById(R.id.tv_razgo_content);
            timeBox=itemView.findViewById(R.id.tv_razgo_time);

            razgoStatus_sent=itemView.findViewById(R.id.iv_razgo_status_sent);
            razgoStatus_unsent=itemView.findViewById(R.id.iv_razgo_status_unsent);
        }
    }

    public interface RazgoListAdapterListener{
        void onRequestRazgos(long end);
    }
}
