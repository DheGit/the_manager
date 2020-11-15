package com.infinitydheer.themanager.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.domain.utils.CalendarUtils;
import com.infinitydheer.themanager.domain.utils.CommonUtils;
import com.infinitydheer.themanager.presentation.ApplicationGlobals;
import com.infinitydheer.themanager.presentation.model.ConvModel;

import java.util.Calendar;
import java.util.List;

public class ConvListAdapter extends RecyclerView.Adapter<ConvListAdapter.ConvHolder> {
    private List<ConvModel> mData;
    private OnConvClickedListener mListener;

    public ConvListAdapter(List<ConvModel> d, OnConvClickedListener liste){
        this.mData =d;
        this.mListener =liste;
    }

    @NonNull
    @Override
    public ConvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.holder_conv,parent,false);
        return new ConvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConvHolder holder, int position) {
        final ConvModel model= mData.get(position);
        String senderName=model.getLastSender(), lastMessage;

        if(senderName.equals(ApplicationGlobals.SELF_ID_UP)) lastMessage="You: "+model.getLastMsg();
        else lastMessage=senderName+": "+model.getLastMsg();

        lastMessage= CommonUtils.removeNewLines(lastMessage);
        if(lastMessage.length()>28) lastMessage=lastMessage.substring(0,28)+"…";

        String lastTime=model.getLastTime();
        Calendar c=CalendarUtils.convertToCalendar(lastTime,true);
        Calendar today=Calendar.getInstance();
        if(CalendarUtils.isSameDay(c,today)) lastTime=lastTime.substring(10);
        else lastTime=lastTime.substring(0,10);
        holder.lastTime.setText(lastTime);

        holder.lastMessage.setText(lastMessage);

        holder.partnerName.setText(model.getPartnerName());

        holder.itemView.setOnClickListener(view -> ConvListAdapter.this.mListener.onConvClicked(model.getConvid()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addConv(ConvModel model){
        int index=findModel(model.getConvid());

        if(index==-1){
            mData.add(model);
            notifyItemInserted(getItemCount()-1);

        }else{
            mData.set(index,model);
            notifyItemChanged(index);
        }
    }

    private int findModel(long id){
        for(int i=0;i<getItemCount();i++){
            if(mData.get(i).getConvid()==id) return i;
        }
        return -1;
    }

    public void setData(List<ConvModel> datalist){
        this.mData =datalist;
        notifyDataSetChanged();
    }

    public class ConvHolder extends RecyclerView.ViewHolder{
        private TextView partnerName, lastMessage, lastTime;

        public ConvHolder(View itemView) {
            super(itemView);
            partnerName=itemView.findViewById(R.id.tv_partner_name);
            lastMessage=itemView.findViewById(R.id.tv_last_razgo);
            lastTime=itemView.findViewById(R.id.tv_last_time);
        }
    }

    public interface OnConvClickedListener{
        void onConvClicked(long convid);
    }
}
