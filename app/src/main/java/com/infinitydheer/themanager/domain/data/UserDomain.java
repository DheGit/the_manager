package com.infinitydheer.themanager.domain.data;

public class UserDomain {
    private String userId;
    private String userPass;

    public UserDomain(String id, String pass){
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
