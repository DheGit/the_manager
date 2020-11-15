package com.infinitydheer.themanager.domain.data;

public class RazgoDomain {
    private int k;
    private String content;
    private String datetime;
    private String sender;
    private long id;
    private boolean sent;

    public RazgoDomain(){
        id=-1;
        sent=true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("RazgoDomain: [")
                .append("id: ").append(getId())
                .append(", content: ").append(getContent())
                .append(", sender: ").append(getSender())
                .append(", datetime: ").append(getDatetime())
                .append(", k: ").append(getK())
                .toString();
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
