package com.Engineering.jobly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;

import com.Engineering.jobly.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    MessageInput messageInput;
    MessagesList messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        final String ChatID = getIntent().getStringExtra("ChatID");
        final User OtherUser = (User) getIntent().getSerializableExtra("TO");

        messageList = findViewById(R.id.messagesList);
        messageInput = findViewById(R.id.Messageinput);

        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
              //  adapter.addToStart(message, true);
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        User mUser = new User();
        for(DataSnapshot I : snapshot.getChildren()){
            if(I.getKey().toLowerCase().equals("displayname")) {
                mUser.setDisplayName(I.getValue(String.class));
                continue;
            }if(I.getKey().toLowerCase().equals("email")) {
                mUser.setEmail(I.getValue(String.class));
                continue;
            }if(I.getKey().toLowerCase().equals("phone")) {
                mUser.setPhone(I.getValue(String.class));
                continue;
            }if(I.getKey().toLowerCase().equals("location")) {
                mUser.setLocation(I.getValue(String.class));
                continue;
            }if(I.getKey().toLowerCase().equals("username")) {
                mUser.setUsername(I.getValue(String.class));
                continue;
            }if(I.getKey().toLowerCase().equals("userid")) {
                mUser.setUserId(I.getValue(String.class));
                continue;
            }if(I.getKey().toLowerCase().equals("chats")) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                mUser.setChats(I.getValue(t));
                continue;
            }
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        SendMessageToChat(new FirebaseMessage(dtf.format(now),input.toString(),mUser),OtherUser,ChatID);
        messageInput.getInputEditText().setText("");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

                //Sends Message To Firebase
                return true;
            }
        });
        MessagesListAdapter<Message> adapter = new MessagesListAdapter<Message>(FirebaseAuth.getInstance().getUid(),null);
        messageList.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("Chats").child(ChatID).orderByChild("id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FirebaseMessage M = new FirebaseMessage();
                for(DataSnapshot Item : snapshot.getChildren())
                {
                    if(Item.getKey().toLowerCase().equals("id")) {
                        M.setId(Item.getValue(String.class));
                        continue;
                    }if(Item.getKey().toLowerCase().equals("text")) {
                        M.setText(Item.getValue(String.class));
                        continue;
                    }if(Item.getKey().toLowerCase().equals("senderuser")) {
                        User mUser = new User();
                        for(DataSnapshot I : Item.getChildren())
                        {
                            if(I.getKey().toLowerCase().equals("displayname")) {
                                mUser.setDisplayName(I.getValue(String.class));
                                continue;
                            }if(I.getKey().toLowerCase().equals("email")) {
                                mUser.setEmail(I.getValue(String.class));
                                continue;
                            }if(I.getKey().toLowerCase().equals("phone")) {
                                mUser.setPhone(I.getValue(String.class));
                                continue;
                            }if(I.getKey().toLowerCase().equals("location")) {
                                mUser.setLocation(I.getValue(String.class));
                                continue;
                            }if(I.getKey().toLowerCase().equals("username")) {
                                mUser.setUsername(I.getValue(String.class));
                                continue;
                            }if(I.getKey().toLowerCase().equals("userid")) {
                                mUser.setUserId(I.getValue(String.class));
                                continue;
                            }if(I.getKey().toLowerCase().equals("chats")) {
                                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                                mUser.setChats(I.getValue(t));
                                continue;
                            }

                        }
                        M.setSenderUser(mUser);
                        continue;
                    }
                }

                try {
                    Message Me = new Message(M.id,M.text,new Author(M.SenderUser),new SimpleDateFormat("yyyyMMddHHmmss").parse(M.id));
                    adapter.addToStart(Me,true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void SendMessageToChat(FirebaseMessage message, User ReciverUser,String ChatID){
        FirebaseDatabase.getInstance().getReference().child("Chats").child(ChatID).child(message.id).setValue(message);
    }
}


 class Message implements IMessage, Serializable {

    /*...*/

String id;
String text;
Author author;
Date createdAt;

     public Message(String id, String text, Author author, Date createdAt) {
         this.id = id;
         this.text = text;
         this.author = author;
         this.createdAt = createdAt;
     }

     @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Author getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}

class Author implements IUser {

    /*...*/
User User;

    public Author(com.Engineering.jobly.ui.login.User user)
    {
        User = user;
    }

    @Override
    public String getId() {
        return User.getUserId();
    }

    @Override
    public String getName() {
        return User.getDisplayName();
    }

    @Override
    public String getAvatar() {
        return null;
    }
}