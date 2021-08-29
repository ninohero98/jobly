package com.Engineering.jobly.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Engineering.jobly.JobOffer;
import com.Engineering.jobly.ListAdapter;
import com.Engineering.jobly.OfferDetails;
import com.Engineering.jobly.PublisherProfile;
import com.Engineering.jobly.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    ArrayList<JobOffer> SearchItems;
    ListAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        //Set Spiner
        final Spinner spinner = (Spinner) root.findViewById(R.id.search_Option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.SerchOption, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        //Set Views
        final EditText Content = root.findViewById(R.id.search_content);
        final Button SearchBTN = root.findViewById(R.id.search_button);
        final ListView mListView = root.findViewById(R.id.SearchList);
        SearchItems = new ArrayList<>();
        mAdapter = new ListAdapter(getContext(),SearchItems);
        mListView.setAdapter(mAdapter);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JobOffer Offer = SearchItems.get(i);
                if (spinner.getSelectedItem().equals("User")){
                    //Open User Profile
                    Intent n = new Intent(view.getContext(), PublisherProfile.class);
                    n.putExtra("UserID",Offer.OfferID);
                    startActivity(n);
                }
                else if (spinner.getSelectedItem().equals("Job")) {
                    //Open Job Offer
                    Intent Ne = new Intent(view.getContext(), OfferDetails.class);
                    Ne.putExtra("Data", Offer);
                    startActivity(Ne);
                }


            }
        });

        SearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchItems.clear();
                mAdapter.notifyDataSetChanged();

                if (spinner.getSelectedItem().equals("User")) {
                    FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("DisplayName").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            JobOffer O = new JobOffer();
                            for (DataSnapshot I : snapshot.getChildren()) {
                                if (I.getKey().toLowerCase().equals("displayname")) {
                                    O.Title = I.getValue(String.class);

                                }if (I.getKey().toLowerCase().equals("userid")) {
                                    O.OfferID = I.getValue(String.class);

                                }if (I.getKey().toLowerCase().equals("username")) {
                                    O.PublisherID = I.getValue(String.class);

                                }

                            }

                            if (O.Title.toLowerCase().contains(Content.getText().toString().toLowerCase()) || O.PublisherID.toLowerCase().contains(Content.getText().toString().toLowerCase())) {

                                //Add to Search View
                                SearchItems.add(O);
                                mAdapter.notifyDataSetChanged();

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
                } else if (spinner.getSelectedItem().equals("Job")) {
                    FirebaseDatabase.getInstance().getReference().child("Jobs").orderByChild("PublisherID").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            JobOffer O = snapshot.getValue(JobOffer.class);

                            if (O.Title.toLowerCase().contains(Content.getText().toString().toLowerCase())) {

                                //Add to Search View
                                SearchItems.add(O);
                                mAdapter.notifyDataSetChanged();

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
            }
        });


        return root;
    }
}
