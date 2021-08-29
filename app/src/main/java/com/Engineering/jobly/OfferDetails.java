package com.Engineering.jobly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Engineering.jobly.ui.login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OfferDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewdatafromoffer);
        JobOffer offer = (JobOffer) getIntent().getSerializableExtra("Data");
        final TextView Title = findViewById(R.id.TitleofProject);
        final TextView Description = findViewById(R.id.DescriptionSection);
        final TextView Budget = findViewById(R.id.BudgetDescription);
        final TextView Location = findViewById(R.id.LocationDescrition);


        if(offer.PublisherID.equals(FirebaseAuth.getInstance().getUid())){
            ((Button)findViewById(R.id.ContactButton)).setText("Remove");
        }
        findViewById(R.id.ContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Button)findViewById(R.id.ContactButton)).getText().equals("Remove"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Jobs").child(offer.OfferID).removeValue();
                    finish();
                    return;
                }
                Intent n = new Intent(view.getContext(),PublisherProfile.class);
                n.putExtra("UserID",offer.PublisherID);
                startActivity(n);
            }
        });


        Title.setText(offer.Title);
        Description.setText(offer.Description);
        Budget.setText("Budget: " + offer.Budget);
        Location.setText(offer.Location);

    }
}
