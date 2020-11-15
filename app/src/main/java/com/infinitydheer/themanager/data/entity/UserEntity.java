package com.infinitydheer.themanager.data.entity;

public class UserEntity {
    private String userId;
    private String userPass;

    public UserEntity(String id, String pass){
        this.userId=id;
        this.userPass=pass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
