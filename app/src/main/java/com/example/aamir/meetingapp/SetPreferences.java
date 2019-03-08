package com.example.aamir.meetingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SetPreferences extends AppCompatActivity {

    private ListView prefListView;
    private Button addBtn, doneBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private List pList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);

        pList = new ArrayList<>();
        firebaseAuth = firebaseAuth.getInstance();
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("Preference Set");

        prefListView = (ListView) findViewById(R.id.listOfPref);

        doneBtn = (Button) findViewById(R.id.doneButtonPref);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exIntent = new Intent(SetPreferences.this, SetExclusions.class);
                startActivity(exIntent);
                finish();
            }
        });

        addBtn = (Button) findViewById(R.id.AddPrefBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createMeetingIntent = new Intent(SetPreferences.this, AddPreference.class);
                SetPreferences.this.startActivity(createMeetingIntent);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            pList.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (s != null) {
                        //TimeSlot tm = s.getValue(new TimeSlot());
                        String one = s.child("day").getValue().toString();
                        String two = s.child("time").getValue().toString();

                        TimeSlot hello =  new TimeSlot(one,two);

                        pList.add(hello);
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
            PreferenceSetAdapter pAdapter = new PreferenceSetAdapter(SetPreferences.this,R.layout.preference_list, pList);
            prefListView.setAdapter(pAdapter);
        }

}

