package com.example.aamir.meetingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SetExclusions extends AppCompatActivity {


    private ListView ExListView;
    private Button addBtn, doneBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private List eList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exclusions);

        eList = new ArrayList<>();
        firebaseAuth = firebaseAuth.getInstance();
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("Exclusion Set");

        ExListView = (ListView) findViewById(R.id.listOfEx);


        doneBtn = (Button) findViewById(R.id.doneButtonEx);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(SetExclusions.this, Home.class);
                startActivity(homeIntent);
                finish();
            }
        });


        addBtn = (Button) findViewById(R.id.AddExBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createMeetingIntent = new Intent(SetExclusions.this, AddExclusion.class);
                SetExclusions.this.startActivity(createMeetingIntent);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eList.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (s != null) {
                        //TimeSlot tm = s.getValue(new TimeSlot());
                        String one = s.child("day").getValue().toString();
                        String two = s.child("time").getValue().toString();

                        TimeSlot hello =  new TimeSlot(one,two);
                        eList.add(hello);
                    }
                }
                adapt();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void adapt(){
        PreferenceSetAdapter eAdapter = new PreferenceSetAdapter(SetExclusions.this,R.layout.preference_list, eList);
        ExListView.setAdapter(eAdapter);
    }

}
