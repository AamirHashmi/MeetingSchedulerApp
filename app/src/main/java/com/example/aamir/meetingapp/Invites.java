package com.example.aamir.meetingapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Invites extends AppCompatActivity {

    private DatabaseReference userMeetings;
   private FirebaseAuth firebaseAuth;
    private DatabaseReference meetingRef;

    private List<Meeting> meetinglist;
    private List<Meeting> invitelist;
    private ListView meetingListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        meetingListView = (ListView) findViewById(R.id.meetList);

        invitelist = new ArrayList<Meeting>();
        meetinglist = new ArrayList<Meeting>();



        firebaseAuth = firebaseAuth.getInstance();
     String currentUserID = firebaseAuth.getCurrentUser().getUid();

        Log.d("myTag", currentUserID);
        userMeetings = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("invites");//.child("meetings");
        meetingRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("meetings");

        meetingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                meetinglist.clear();

                for(DataSnapshot s : dataSnapshot.getChildren()){


                    //Meeting newMeeting = s.getValue(Meeting.class);
                    String desc = s.child("description").getValue().toString();
                    String name = s.child("name").getValue().toString();
                    String day = s.child("timeSlot").child("day").getValue().toString();
                    String time = s.child("timeSlot").child("time").getValue().toString();
                    TimeSlot tm = new TimeSlot(day, time);


                    Meeting newMeeting = new Meeting(name, desc, tm);
                    meetinglist.add(newMeeting);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        userMeetings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {

                        String desc = s.child("description").getValue().toString();
                        String name = s.child("name").getValue().toString();
                        String day = s.child("timeSlot").child("day").getValue().toString();
                        String time = s.child("timeSlot").child("time").getValue().toString();
                        // String spref = s.child("timeSlot").child("spref").getValue().toString();
                        TimeSlot tm = new TimeSlot(day, time);
                        Meeting newMeeting = new Meeting(name, desc, tm);
                        invitelist.add(newMeeting);
                    }


                    adapt();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                for(Meeting m : meetinglist){
                    if(m.getTimeSlot().getDay().equalsIgnoreCase(invitelist.get(position).getTimeSlot().getDay())){

                            Toast.makeText(Invites.this, "Meeting collision", Toast.LENGTH_SHORT).show();
                            return;

                    }
                }

                Toast.makeText(Invites.this, invitelist.get(position).getName() + " Accepted", Toast.LENGTH_SHORT).show();
                String currentUserID = firebaseAuth.getCurrentUser().getUid();
                meetingRef.child(invitelist.get(position).getName()).setValue(invitelist.get(position));
                userMeetings.child(invitelist.get(position).getName()).removeValue();
                finish();
            }

        });


    }

    private void adapt(){
        MeetingArrayAdapter maa = new MeetingArrayAdapter(Invites.this,R.layout.meeting_list,invitelist);
        meetingListView.setAdapter(maa);
    }


}


