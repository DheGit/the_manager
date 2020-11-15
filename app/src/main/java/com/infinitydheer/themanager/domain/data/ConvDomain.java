package com.infinitydheer.themanager.domain.data;

public class ConvDomain {
    private String partnerName;
    private String lastMsg;
    private String lastTime;
    private String lastSender;
    private long convid;
    private int k;

    public ConvDomain(){}


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

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("ConvDomain: [")
                .append("partnerName: ").append(getPartnerName())
                .append(", convid: ").append(getConvid())
                .append(", lastTime: ").append(getLastTime())
                .append(", lastSender: ").append(getLastSender())
                .append(", lastMsg: ").append(getLastMsg())
                .append(", k: ").append(getK())
                .append("]")
                .toString();
    }
}
