package com.Engineering.jobly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddJob extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        ((Button)findViewById(R.id.Upload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ProjectTitle = ((EditText)findViewById(R.id.ProjectTitle)).getText().toString();
                final String BudgetAdd = ((EditText)findViewById(R.id.BudgetAdd)).getText().toString();
                final String TimeTobeDoneAt = ((EditText)findViewById(R.id.TimeTobeDoneAt)).getText().toString();
                final String Location = ((EditText)findViewById(R.id.Location)).getText().toString();
                final String Details = ((EditText)findViewById(R.id.Details)).getText().toString();

                JobOffer NewJob = new JobOffer();
                NewJob.Budget = BudgetAdd;
                NewJob.WorkTime = TimeTobeDoneAt;
                NewJob.Location =Location;
                NewJob.Description = Details;
                NewJob.Title= ProjectTitle;
                NewJob.PublisherID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                NewJob.PublishDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())).format(Calendar.getInstance().getTime());
                NewJob.OfferID =  NewJob.PublisherID + NewJob.PublishDate;

                FirebaseDatabase.getInstance().getReference().child("Jobs").child(NewJob.OfferID).setValue(NewJob);
                finish();



            }
        });
    }
}
