package com.example.aamir.meetingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Home extends AppCompatActivity {

    private Button signOutBtn;
    private Button chatBtn;
    private Button inviteBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference meetingReference;
    private DatabaseReference RootRef;
    private List<Meeting> meetingList;
    private ListView meetingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        meetingList = new ArrayList<>();
        meetingListView = (ListView) findViewById(R.id.meetingListViewHome);
        firebaseAuth = firebaseAuth.getInstance(); // get instance method uses a shared instance of the fb auth rather than creating a new one, this good in singleton stuff.
        String currentUserID =firebaseAuth.getCurrentUser().getUid();
        meetingReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID).child("meetings");

        meetingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                meetingList.clear();

                for(DataSnapshot s : dataSnapshot.getChildren()){


                    //Meeting newMeeting = s.getValue(Meeting.class);
                    String desc = s.child("description").getValue().toString();
                        String name = s.child("name").getValue().toString();

                        String day = s.child("timeSlot").child("day").getValue().toString();
                        String time = s.child("timeSlot").child("time").getValue().toString();
                        TimeSlot tm = new TimeSlot(day, time);


                        Meeting newMeeting = new Meeting(name, desc, tm);
                        meetingList.add(newMeeting);
                }


               adapt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button createMeeting = (Button) findViewById(R.id.createMeetingBtn);
        createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createMeetingIntent = new Intent(Home.this, CreateMeeting.class);
                Home.this.startActivity(createMeetingIntent);
            }
        });

        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                Intent signIn = new Intent(Home.this, SignIn.class);
                startActivity(signIn);
            }
        });

        chatBtn = (Button) findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(Home.this, Chat.class);
                startActivity(chat);
            }
        });

        inviteBtn = (Button) findViewById(R.id.invitesBtn);
        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inv = new Intent(Home.this, Invites.class);
                startActivity(inv);
            }
        });





    }

    private void adapt(){
        MeetingArrayAdapter maa = new MeetingArrayAdapter(Home.this,R.layout.meeting_list,meetingList);
        meetingListView.setAdapter(maa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId()==R.id.main_settings_option){
            Intent settingsIntent = new Intent (Home.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId()==R.id.main_find_friends_option){
            Intent findUsersIntent = new Intent (Home.this, FindUsersActivity.class);
            startActivity(findUsersIntent);
        }
        if(item.getItemId()==R.id.main_create_group_option){
            RequestNewGroup();
        }

/*        if(item.getItemId()==R.id.prefSetBtn){
            Intent createMeetingIntent = new Intent(Chat.this, SetPreferences.class);
            Chat.this.startActivity(createMeetingIntent);
        }

        if(item.getItemId()==R.id.ExclusionSetBtn){
            Intent createMeetingIntent = new Intent(Chat.this, SetExclusions.class);
            Chat.this.startActivity(createMeetingIntent);
        }*/

        return true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, R.style.AlertDialog);
        builder.setTitle("Enter name of the group: ");
        final EditText groupNameField = new EditText(Home.this);
        groupNameField.setHint("e.g group1");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){

                    Toast.makeText(Home.this, "Please write a group name", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNewGroup(groupName);
                }
            }
        });
        builder.setNegativeButton ("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        builder.show();

    }
    private void CreateNewGroup(final String groupName){

        RootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task){
                if (task.isSuccessful()){
                    Toast.makeText(Home.this, groupName + " group is created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
