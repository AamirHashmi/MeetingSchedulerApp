package com.example.aamir.meetingapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddExclusion extends AppCompatActivity {

    private Button addEx;
    private DatabaseReference ref;
    private DatabaseReference pRef;
    private FirebaseAuth firebaseAuth;
    private Random rd;

    private List<TimeSlot> prefList;
    private List<TimeSlot> eList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exclusion);

        rd = new Random();
        prefList = new ArrayList<TimeSlot>();
        eList = new ArrayList<TimeSlot>();

        firebaseAuth = firebaseAuth.getInstance();
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("Exclusion Set");
        pRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("Preference Set");
        String[] times = {"9:00 - 10:00", "10:00 - 11:00", "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
                "15:00 - 16:00", "16:00 - 17:00"};
        ArrayAdapter<String> timeList = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, times);
        final Spinner timesListSpinner = (Spinner) findViewById(R.id.setExTime);
        timesListSpinner.setAdapter(timeList);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
                "Saturday", "Sunday"};
        final ArrayAdapter<String> dayList = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, days);
        final Spinner dayListSpinner = (Spinner) findViewById(R.id.setExDay);
        dayListSpinner.setAdapter(dayList);

        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prefList.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (s != null) {
                        //TimeSlot tm = s.getValue(new TimeSlot());
                        String one = s.child("day").getValue().toString();
                        String two = s.child("time").getValue().toString();

                        TimeSlot hello =  new TimeSlot(one,two);
                        prefList.add(hello);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prefList.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (s != null) {
                        //TimeSlot tm = s.getValue(new TimeSlot());
                        String one = s.child("day").getValue().toString();
                        String two = s.child("time").getValue().toString();

                        TimeSlot hello =  new TimeSlot(one,two);
                        eList.add(hello);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        addEx = (Button) findViewById(R.id.submitExBtn);
        addEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimeSlot tm = new TimeSlot(dayListSpinner.getSelectedItem().toString(), timesListSpinner.getSelectedItem().toString());
                for(TimeSlot temp: prefList){
                    if((temp.getDay().equalsIgnoreCase(tm.getDay())) && (temp.getTime().equalsIgnoreCase(tm.getTime()))){
                        Toast.makeText(AddExclusion.this, "Error exclusion matches preference", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                for(TimeSlot temp: eList){
                    if((temp.getDay().equalsIgnoreCase(tm.getDay())) && (temp.getTime().equalsIgnoreCase(tm.getTime()))){
                        Toast.makeText(AddExclusion.this, "Error exclusion already made", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String randName = Integer.toString( rd.nextInt());
                ref.child(randName).setValue(tm);

                finish();
            }
        });
    }
}
