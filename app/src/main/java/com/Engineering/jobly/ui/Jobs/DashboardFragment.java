package com.Engineering.jobly.ui.Jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Engineering.jobly.AddJob;
import com.Engineering.jobly.JobOffer;
import com.Engineering.jobly.ListAdapter;
import com.Engineering.jobly.OfferDetails;
import com.Engineering.jobly.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {


    ArrayList<JobOffer> Items;
    ListAdapter ad;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_jobs, container, false);
        FloatingActionButton Fab = root.findViewById(R.id.floating_action_button);
        SetLiveItems();
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(), AddJob.class));
            }
        });

        ListView list = root.findViewById(R.id.listViewID);
        Items = new ArrayList<JobOffer>();
        ad = new ListAdapter(this.getContext(), Items);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JobOffer o = Items.get(i);
                Intent Ne = new Intent(view.getContext(), OfferDetails.class);
                Ne.putExtra("Data", o);
                startActivity(Ne);

            }
        });
        list.setAdapter(ad);

        return root;
    }

    void SetLiveItems() {

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Jobs");
        mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                JobOffer Job = snapshot.getValue(JobOffer.class);

                if (!Items.contains(Job) && Items.stream().filter(c -> c.OfferID == Job.OfferID).count() == 0 && Job.OfferID != null) {
                    Items.add(Job);
                    ad.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                JobOffer Job = snapshot.getValue(JobOffer.class);


                if (Items.contains(Job)) {
                    Items.remove(Job);
                    ad.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mdatabase.orderByChild("PublishDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                JobOffer Job = snapshot.getValue(JobOffer.class);


                if (!Items.contains(Job) && Items.stream().filter(c -> c.OfferID == Job.OfferID).count() == 0 && Job.OfferID != null) {
                    Items.add(Job);
                    ad.notifyDataSetChanged();
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
            // ...
        });


    }
}
