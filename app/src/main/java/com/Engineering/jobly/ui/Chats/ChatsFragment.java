package com.Engineering.jobly.ui.Chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.Engineering.jobly.ChatActivity;
import com.Engineering.jobly.FirebaseMessage;
import com.Engineering.jobly.JobOffer;
import com.Engineering.jobly.ListAdapter;
import com.Engineering.jobly.R;
import com.Engineering.jobly.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ChatsFragment extends Fragment {

    ArrayList<JobOffer> Items;
    ListAdapter ad;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        ListView list = root.findViewById(R.id.listViewChatsID);
        Items = new ArrayList<JobOffer>();
        ad = new ListAdapter(this.getContext(), Items);
        SetLiveItems();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                JobOffer o = Items.get(i);
                Intent Chat = new Intent(view.getContext(), ChatActivity.class);

                Chat.putExtra("ChatID", o.OfferID);

                String OtherUserID = o.OfferID.replace(FirebaseAuth.getInstance().getUid(), "");
                FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserID)
                        .addValueEventListener(new ValueEventListener() {
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

                                Chat.putExtra("TO", mUser);
                                startActivity(Chat);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });
        list.setAdapter(ad);
        return root;
    }

    private void SetLiveItems() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // I: ChatID
                        for (DataSnapshot IM : snapshot.getChildren()) {
                            String I = IM.getValue(String.class);
                            if (I.equals("0")) {
                                continue;
                            }
                            JobOffer Chat = new JobOffer();
                            Chat.OfferID = I;
                            FirebaseDatabase.getInstance().getReference().child("Chats").child(I).orderByChild("id")
                                    .limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot O : snapshot.getChildren()) {
                                                O.getRef().addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (!snapshot.hasChildren()) {
                                                            return;
                                                        }
                                                        FirebaseMessage M = new FirebaseMessage();

                                                        for (DataSnapshot Item : snapshot.getChildren()) {
                                                            if (Item.getKey().toLowerCase().equals("id")) {
                                                                M.setId(Item.getValue(String.class));
                                                                continue;
                                                            }
                                                            if (Item.getKey().toLowerCase().equals("text")) {
                                                                M.setText(Item.getValue(String.class));
                                                                continue;
                                                            }
                                                            if (Item.getKey().toLowerCase().equals("senderuser")) {
                                                                User mUser = new User();
                                                                for (DataSnapshot I : Item.getChildren()) {
                                                                    if (I.getKey().toLowerCase().equals("displayname")) {
                                                                        mUser.setDisplayName(I.getValue(String.class));
                                                                        continue;
                                                                    }
                                                                    if (I.getKey().toLowerCase().equals("email")) {
                                                                        mUser.setEmail(I.getValue(String.class));
                                                                        continue;
                                                                    }
                                                                    if (I.getKey().toLowerCase().equals("phone")) {
                                                                        mUser.setPhone(I.getValue(String.class));
                                                                        continue;
                                                                    }
                                                                    if (I.getKey().toLowerCase().equals("location")) {
                                                                        mUser.setLocation(I.getValue(String.class));
                                                                        continue;
                                                                    }
                                                                    if (I.getKey().toLowerCase().equals("username")) {
                                                                        mUser.setUsername(I.getValue(String.class));
                                                                        continue;
                                                                    }
                                                                    if (I.getKey().toLowerCase().equals("userid")) {
                                                                        mUser.setUserId(I.getValue(String.class));
                                                                        continue;
                                                                    }
                                                                    if (I.getKey().toLowerCase().equals("chats")) {
                                                                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                                                                        };
                                                                        mUser.setChats(I.getValue(t));
                                                                        continue;
                                                                    }

                                                                }
                                                                M.setSenderUser(mUser);
                                                                continue;
                                                            }
                                                        }
                                                        if(M.getSenderUser().getUserId() != FirebaseAuth.getInstance().getUid()) {
                                                            Chat.Description =M.getSenderUser().getDisplayName() +" : " + M.getText();
                                                        }
                                                        else{
                                                            Chat.Description ="You : " + M.getText();
                                                        }


                                                        FirebaseDatabase.getInstance().getReference().child("Users")
                                                                .child(I.replace(FirebaseAuth.getInstance().getUid(), ""))
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        for(DataSnapshot Item: snapshot.getChildren()) {
                                                                            if(Item.getKey().toLowerCase().equals("displayname")) {
                                                                                Chat.Title = Item.getValue(String.class);
                                                                                Items.add(Chat);
                                                                                ad.notifyDataSetChanged();
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public static <T> Collector<T, ?, ArrayList<T>> toArrayList() {
        return Collectors.toCollection(ArrayList::new);
    }
}
