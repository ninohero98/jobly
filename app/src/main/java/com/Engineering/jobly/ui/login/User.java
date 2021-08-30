package com.Engineering.jobly.ui.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    String DisplayName ="N/A";
    String Email ="N/A";
    String Phone ="N/A";
    String Location ="N/A";
    String Username ="N/A";
    String UserId;
    String Bio ="N/A";

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    private ArrayList<String> Chats = new ArrayList<String>();

    public ArrayList<String> getChats() {
        return Chats;
    }

    public void setChats(ArrayList chats) {
        Chats = chats;
    }



    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void AddChat(String ID){
        if(Chats.contains(ID)){return;}
        Chats.add(ID);
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
public User(){

}
    public User(String displayname, String email, String Phone, String Loc, String User, String UserId){
        this.DisplayName=displayname;
        this.Email=email;
        this.Phone = Phone;
        this.Location = Loc;
        this.Username = User;
        this.UserId = UserId;
        Chats.add("0");

       ;
    }






}
