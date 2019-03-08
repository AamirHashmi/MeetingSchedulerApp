package com.example.aamir.meetingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {


    private EditText emailInput;
    private EditText passwordInput;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference RootRef;
    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = firebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();


        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        Button registerBtn = (Button) findViewById(R.id.submitBtn);

        prog = new ProgressDialog(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });
    }

    private void register(){

        String email = emailInput.getText().toString().trim(); //gets email from edit text puts it into string var.
        String password = passwordInput.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        prog.setMessage("Validating...");
        prog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                prog.dismiss();
                if(task.isSuccessful()){ //if the registration is successful ->
                    String currentUserID = firebaseAuth.getCurrentUser().getUid();
                    RootRef.child("Users").child(currentUserID).setValue("");
                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent settingsActivity = new Intent(Register.this, SettingsActivity.class);
                    Register.this.startActivity(settingsActivity);
                    
                    //finish(); //exits register page.
                }
                else{
                    Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
