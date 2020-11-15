package com.infinitydheer.themanager.presentation.model;

public class RazgoModel {
    private String content;
    private String datetime;
    private String sender;
    private long id;
    private boolean sent;

    public RazgoModel(){
        this.id=-1;
        sent=true;
    }

    public void setContent(String content){
        this.content=content;
    }

    public String getContent() {
        return content;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("RazgoModel: [")
                .append("id: ").append(getId())
                .append(", content: ").append(getContent())
                .append(", sender: ").append(getSender())
                .append(", datetime: ").append(getDatetime())
                .toString();
    }
}
