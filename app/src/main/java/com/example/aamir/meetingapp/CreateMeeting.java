package com.example.aamir.meetingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class CreateMeeting extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DatabaseReference usersDatabase;
    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuth;
    private ListView availableTimesList;
    private ListView prefTimes;

    private EditText nameInput;
    private EditText descInput;
    private CalendarView minCalendar;
    private CalendarView maxCalendar;
    private Date maxDate;
    private Date minDate;
    private Button makeMeetingBtn;
    private Button inviteBtn;
    private Button confirmMeetingButton;
    private List<String>invitees;
    private DatabaseReference checkRef;
    private DatabaseReference checkRef2;
    public List<TimeSlot> preferenceList;
    public List<TimeSlot> exclusionList;
    private List<TimeSlot> allSlots;
    private List<String> days;
    private List<String> timesar;
    private List<TimeSlot> useableSlots;
    private ProgressDialog pd;
    private TimeSlot clickedTime;

    private List<TimeSlot> test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        pd = new ProgressDialog(this);


        days = new ArrayList<String>();
        timesar = new ArrayList<String>();
        preferenceList = new ArrayList<TimeSlot>();
        exclusionList = new ArrayList<TimeSlot>();
        allSlots = new ArrayList<TimeSlot>();
        invitees = new ArrayList<>();
        useableSlots = new ArrayList<TimeSlot>();
        test = new ArrayList<TimeSlot>();

        firebaseAuth = firebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("invites");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        nameInput = (EditText) findViewById(R.id.nameInput);
        descInput = (EditText) findViewById(R.id.descInput);
        availableTimesList = (ListView) findViewById(R.id.availableTimes);

        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");

        timesar.add("9:00 - 10:00");
        timesar.add("10:00 - 11:00");
        timesar.add("11:00 - 12:00");
        timesar.add("12:00 - 13:00");
        timesar.add("13:00 - 14:00");
        timesar.add("15:00 - 16:00");
        timesar.add("16:00 - 17:00");

        for(int i = 0; i<7; i++){
            for (int j=0; j<7; j++){
                allSlots.add(new TimeSlot(days.get(i),timesar.get(j)));
            }
        }

        inviteBtn = (Button) findViewById(R.id.selectUsersBtn);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inviteIntent = new Intent(CreateMeeting.this, SelectUsers.class);
                CreateMeeting.this.startActivityForResult(inviteIntent,1);
            }
        });



        makeMeetingBtn = (Button) findViewById(R.id.makeMeetingBtn);
        makeMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPreference();
                checkExclusions();

                for(TimeSlot tmo : preferenceList){
                    Log.d("myTag", tmo.getDay());
                }
                //create();

                fillAllSlots();
                for(TimeSlot f : allSlots){
                    Log.d("myTag", f.getDay());
                    Log.d("myTag", f.getTime());
                }

            }
        });

        confirmMeetingButton = (Button) findViewById(R.id.confirmMeetingBtn);
        confirmMeetingButton.setEnabled(false);


        confirmMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                create(clickedTime);
            }
        });


        availableTimesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                confirmMeetingButton.setEnabled(true);
                confirmMeetingButton.setBackgroundColor(Color.GREEN);

                        Toast.makeText(CreateMeeting.this, allSlots.get(position).getDay(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(CreateMeeting.this, allSlots.get(position).getTime(), Toast.LENGTH_SHORT).show();
                        clickedTime = new TimeSlot(allSlots.get(position).getDay(),allSlots.get(position).getTime());

                    }



        });


     /*   prefTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                confirmMeetingButton.setEnabled(true);
                confirmMeetingButton.setBackgroundColor(Color.GREEN);

                Toast.makeText(CreateMeeting.this, preferenceList.get(position).getDay(), Toast.LENGTH_SHORT).show();
                clickedTime = new TimeSlot(preferenceList.get(position).getDay(),preferenceList.get(position).getTime());

                //  create(clickedTime);
                //   create();
            }



        });*/

    }





    private void fillAllSlots(){


        List<TimeSlot> allSlots2 = new ArrayList<TimeSlot>(); ;
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        for(int i = 0 ; i < allSlots.size(); i++){
            for(int j = 0; j < exclusionList.size(); j++){
                if(allSlots.get(i).getDay().equalsIgnoreCase(exclusionList.get(j).getDay())){
                    if(allSlots.get(i).getTime().equalsIgnoreCase(exclusionList.get(j).getTime())) {
                        indexes.add(i);
                    }
                }
            }
        }
        for(int i = 0; i < allSlots.size(); i ++){
            if(!indexes.contains(i)){
                allSlots2.add(allSlots.get(i));
            }
        }
        allSlots = allSlots2;



      List<TimeSlot> allSlots3 = new ArrayList<TimeSlot>(); ;
        ArrayList<Integer> indexes2 = new ArrayList<Integer>();
        for(int i = 0 ; i < allSlots.size(); i++){
            for(int j = 0; j < preferenceList.size(); j++){
                if(allSlots.get(i).getDay().equalsIgnoreCase(preferenceList.get(j).getDay())){
                    if(allSlots.get(i).getTime().equalsIgnoreCase(preferenceList.get(j).getTime())) {
                        //indexes2.add(i);
                        allSlots.get(i).setPref(true);
                    }
                }
            }
        }
       /* for(int i = 0; i < allSlots.size(); i ++){
            if(!indexes2.contains(i)){
                allSlots3.add(allSlots.get(i));
            }
        }*/
       // allSlots = allSlots3;

        adapt();
    }


    private void adapt(){

        Set<TimeSlot> set = new HashSet<>(preferenceList);
        preferenceList.clear();
        preferenceList.addAll(set);
        test = preferenceList;


       // PreferenceSetAdapter pAdapter = new PreferenceSetAdapter(CreateMeeting.this,R.layout.preference_list, test);
        //prefTimes.setAdapter(pAdapter);

        PreferenceSetAdapter AllAdapter = new PreferenceSetAdapter(CreateMeeting.this,R.layout.preference_list, allSlots);
        availableTimesList.setAdapter(AllAdapter);
    }
    public void create(TimeSlot tm) {
        String name = nameInput.getText().toString().trim();
        String desc = descInput.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tm == null){
            Toast.makeText(this, "Click a valid time", Toast.LENGTH_SHORT).show();
            return;
        }



        Meeting newMeeting = new Meeting(name, desc, clickedTime);


        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        userRef.child(currentUserID).child("invites").child(newMeeting.getName()).setValue(newMeeting);


        for(String s:invitees){
            userRef.child(s).child("invites").child(newMeeting.getName()).setValue(newMeeting);
        }

        Toast.makeText(this, "Meeting has been created", Toast.LENGTH_SHORT).show();


        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
               invitees.add(result);
                checkPreference();
                checkExclusions();

            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }


    private void checkPreference() {
        Log.d("myTag", "preference");
        for (String id : invitees) {

            checkRef = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Preference Set");

            checkRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // preferenceList.clear();
                    //int i =0;

                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        String tempDay = temp.child("day").getValue().toString();
                        String tempTime = temp.child("time").getValue().toString();
                        TimeSlot tempPref = new TimeSlot(tempDay, tempTime);
                        preferenceList.add(tempPref);
                        // Log.d("myTag", preferenceList.get(i).getDay());
                        // i++;
                    }



                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        Log.d("myTag", "preference end");
    }

    private void checkExclusions() {
        Log.d("myTag", "exclusion");
        for (String id : invitees) {

            checkRef2 = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Exclusion Set");

            checkRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // preferenceList.clear();
                    //int i =0;

                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        String tempDay = temp.child("day").getValue().toString();
                        String tempTime = temp.child("time").getValue().toString();
                        TimeSlot tempPref = new TimeSlot(tempDay, tempTime);

                        exclusionList.add(tempPref);
                        // Log.d("myTag", preferenceList.get(i).getDay());
                        // i++;
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        Log.d("myTag", "exclusion end");
    }
}
