package com.infinitydheer.themanager.presentation.model;

public class ConvModel {
    private String partnerName;
    private String lastMsg;
    private String lastTime;
    private String lastSender;
    private long convid;

    public ConvModel(){}

    public ConvModel(String partnerName, long convid){
        this.partnerName=partnerName;
        this.convid=convid;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public long getConvid() {
        return convid;
    }

    public void setConvid(long convid) {
        this.convid = convid;
    }

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("ConvModel: [")
                .append("partnerName: ").append(getPartnerName())
                .append(", convid: ").append(getConvid())
                .append(", lastTime: ").append(getLastTime())
                .append(", lastSender: ").append(getLastSender())
                .append(", lastMsg: ").append(getLastMsg())
                .append("]")
                .toString();
    }
}
