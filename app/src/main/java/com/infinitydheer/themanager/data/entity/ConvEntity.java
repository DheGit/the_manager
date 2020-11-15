package com.infinitydheer.themanager.data.entity;

/**
 * Class to encapsulate a razRoom in TheManager lingo for the data layer
 */
public class ConvEntity {
    private String partnerName;
    private String lastMsg;
    private String lastTime;
    private long convid;
    private int k;
    private String lastSender;

    public ConvEntity(){}

    public ConvEntity(String partnerNam, long convi){
        this.partnerName=partnerNam;
        this.convid=convi;
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
        return new StringBuilder().append("ConvEntity: [")
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
