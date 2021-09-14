package com.Engineering.jobly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Engineering.jobly.ui.login.User;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PublisherProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        final Button B = findViewById(R.id.ProfileContactSection);
        final TextView Name = findViewById(R.id.ProfileNameSection);
        final TextView UserName = findViewById(R.id.ProfileUserNameSection);
        final TextView Phone = findViewById(R.id.ProfilePhoneSection);
        final TextView Email = findViewById(R.id.ProfileEmailSection);
        final TextView Location = findViewById(R.id.ProfileLocationSection);
        final TextView Bio = findViewById(R.id.ProfileBio);
        ((Button)findViewById(R.id.ProfileEditSection)).setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("UserID")).addValueEventListener(new ValueEventListener() {
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
                    }if(I.getKey().toLowerCase().equals("bio")) {
                        mUser.setBio(I.getValue(String.class));
                        continue;
                    }if(I.getKey().toLowerCase().equals("chats")) {
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                        mUser.setChats(I.getValue(t));
                        continue;
                    }

                }
                Name.setText(mUser.getDisplayName());
                UserName.setText(mUser.getUsername());
                Phone.setText(mUser.getPhone());
                Email.setText(mUser.getEmail());
                Location.setText(mUser.getLocation());
                Bio.setText(mUser.getBio());

                B.setText("Contact Us");
                B.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String ChatID =FirebaseAuth.getInstance().getUid() + mUser.getUserId();
                        Intent Chat = new Intent(view.getContext(), ChatActivity.class);

                        //Add Chat to Other User
                        mUser.AddChat(ChatID);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("UserID")).setValue(mUser);

                        //Add Chat to Me
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


                                mUser.AddChat(ChatID);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(mUser);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Chat.putExtra("ChatID", ChatID);

                        Chat.putExtra("TO", mUser);

                        startActivity(Chat);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}
