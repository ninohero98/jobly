package com.Engineering.jobly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Engineering.jobly.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_edit);
        User Data = (User) getIntent().getSerializableExtra("Data");
        EditText DisplayName = findViewById(R.id.ProfileNameSectionEdit);
        EditText ProfileUserName = findViewById(R.id.ProfileUserNameSectionEdit); //Don't Change
        EditText Phone = findViewById(R.id.ProfilePhoneSectionEdit);
        EditText Email = findViewById(R.id.ProfileEmailSectionEdit); //Don't Change
        EditText Location = findViewById(R.id.ProfileLocationSectionEdit);
        EditText ProfileBioSection = findViewById(R.id.ProfileBioSectionEdit);

        DisplayName.setText(Data.getDisplayName());
        ProfileUserName.setText(Data.getUsername());
        Phone.setText(Data.getPhone());
        Email.setText(Data.getEmail());
        Location.setText(Data.getLocation());
        ProfileBioSection.setText(Data.getBio());

        Button Submit = findViewById(R.id.ProfileEditSectionSubmit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.setDisplayName(DisplayName.getText().toString());
                Data.setPhone(Phone.getText().toString());
                Data.setLocation(Location.getText().toString());
                Data.setBio(ProfileBioSection.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(Data);
                finish();
            }
        });


    }
}
