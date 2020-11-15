package com.infinitydheer.themanager.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.presentation.model.ConvModel;

import java.util.ArrayList;
import java.util.List;

public class MasterConvListAdapter extends RecyclerView.Adapter<MasterConvListAdapter.MasterConvHolder>{
    private List<ConvModel> mData;

    private MasterConvListListener mListener;

    public MasterConvListAdapter(){
        mData=new ArrayList<>();
    }

    @NonNull
    @Override
    public MasterConvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        final View view=inflater.inflate(R.layout.holder_master_conv, parent, false);

        return new MasterConvHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterConvHolder holder, int position) {
        ConvModel model=mData.get(position);

        holder.partnerText.setText(String.format("With %s", model.getPartnerName()));

        holder.idText.setText(String.format("Conversation ID: %d", model.getConvid()));

        holder.itemView.setOnClickListener(view -> MasterConvListAdapter.this.mListener.onConvClicked(model));
    }

    public void addConvModel(ConvModel model){
        mData.add(model);
        notifyItemInserted(getItemCount()-1);
    }

    public void setData(List<ConvModel> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void setListener(MasterConvListListener listener){
        mListener=listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MasterConvHolder extends RecyclerView.ViewHolder{

        private TextView idText, partnerText;

        public MasterConvHolder(View itemView) {
            super(itemView);

            idText=itemView.findViewById(R.id.tv_masterconv_convid);
            partnerText=itemView.findViewById(R.id.tv_masterconv_conv_partner);
        }
    }

    public interface MasterConvListListener{
        void onConvClicked(ConvModel model);
    }
}
