package com.example.aamir.meetingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chat extends AppCompatActivity {

    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabAccessorAdapter myTabAccessorAdapter;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        RootRef = FirebaseDatabase.getInstance().getReference();

        myViewPager =(ViewPager) findViewById(R.id.main_tabs_pager);
        myTabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabAccessorAdapter);
        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
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
           SendUserToSettingsActivity();
       }


       if(item.getItemId()==R.id.main_find_friends_option){
            SendUserToFindUserActivity();
       }
       if(item.getItemId()==R.id.main_create_group_option){
            RequestNewGroup();
       }

/*
        if(item.getItemId()==R.id.prefSetBtn){
            Intent createMeetingIntent = new Intent(Chat.this, SetPreferences.class);
            Chat.this.startActivity(createMeetingIntent);
        }

        if(item.getItemId()==R.id.ExclusionSetBtn){
            Intent createMeetingIntent = new Intent(Chat.this, SetExclusions.class);
            Chat.this.startActivity(createMeetingIntent);
        }
*/

        return true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this, R.style.AlertDialog);
        builder.setTitle("Enter name of the group: ");
        final EditText groupNameField = new EditText(Chat.this);
        groupNameField.setHint("e.g group1");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName)){

                    Toast.makeText(Chat.this, "Please write a group name", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Chat.this, groupName + " group is created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendUserToSettingsActivity(){
        Intent settingsIntent = new Intent (Chat.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void SendUserToFindUserActivity(){
        Intent findUsersIntent = new Intent (Chat.this, FindUsersActivity.class);
        startActivity(findUsersIntent);
    }

}
