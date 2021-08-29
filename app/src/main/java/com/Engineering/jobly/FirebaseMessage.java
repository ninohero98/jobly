package com.Engineering.jobly;

import com.Engineering.jobly.ui.login.User;

import java.io.Serializable;

public class FirebaseMessage implements Serializable {
    String id;
    String text;

    public User getSenderUser() {
        return SenderUser;
    }

    public void setSenderUser(User senderUser) {
        SenderUser = senderUser;
    }

    User SenderUser;

    public FirebaseMessage(){

    }
    public FirebaseMessage(String id, String text, User SenderUser) {
        this.id = id;
        this.text = text;
        this.SenderUser = SenderUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
