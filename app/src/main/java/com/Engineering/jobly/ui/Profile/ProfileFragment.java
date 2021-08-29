package com.Engineering.jobly.ui.Profile;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.Engineering.jobly.EditProfile;
import com.Engineering.jobly.MainFrame;
import com.Engineering.jobly.R;
import com.Engineering.jobly.ui.login.LoginActivity;
import com.Engineering.jobly.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_profile, container, false);
         final TextView Name = root.findViewById(R.id.ProfileNameSection);
         final TextView UserName = root.findViewById(R.id.ProfileUserNameSection);
         final TextView Phone = root.findViewById(R.id.ProfilePhoneSection);
         final TextView Email = root.findViewById(R.id.ProfileEmailSection);
         final TextView Location = root.findViewById(R.id.ProfileLocationSection);
         final Button Contact = root.findViewById(R.id.ProfileContactSection);
         final Button EditContact = root.findViewById(R.id.ProfileEditSection);
        final TextView Bio = root.findViewById(R.id.ProfileBio);

         EditContact.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User mUser = new User();
                        for(DataSnapshot I : snapshot.getChildren()) {
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
                            if(I.getKey().toLowerCase().equals("bio")) {
                                mUser.setBio(I.getValue(String.class));
                                continue;
                            }
                        }

                        Intent intent = new Intent(getContext(),EditProfile.class);
                        intent.putExtra("Data",mUser);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
             }
         });

         Contact.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FirebaseAuth.getInstance().signOut();
                 SharedPreferences sharedPreferences = root.getContext().getSharedPreferences("SaveData.save", MODE_PRIVATE);
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putString("Email", "");
                 editor.putString("Password","");
                 editor.commit();
                 getActivity().finish();
                 startActivity(new Intent(root.getContext(), LoginActivity.class));


             }
         });
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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
                    }
                    if(I.getKey().toLowerCase().equals("bio")) {
                        mUser.setBio(I.getValue(String.class));
                        continue;
                    }
                }
                Name.setText(mUser.getDisplayName());
                UserName.setText(mUser.getUsername());
                Phone.setText(mUser.getPhone());
                Email.setText(mUser.getEmail());
                Location.setText(mUser.getLocation());
                Contact.setText("Sign out");
                Bio.setText(mUser.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }
}
