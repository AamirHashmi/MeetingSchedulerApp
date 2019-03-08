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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignIn extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button signIn;
    private ProgressDialog prog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){ //if the user is already logged in start home activity.
            finish();
            Intent home = new Intent(SignIn.this, Home.class);
            SignIn.this.startActivity(home);
        }

        signIn = (Button) findViewById(R.id.signInBtn) ;

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        emailInput = (EditText) findViewById(R.id.emailInputSignIn);
        passwordInput = (EditText) findViewById(R.id.passwordInputSignIn);
        prog = new ProgressDialog(this);
        TextView registerTextLink = (TextView) findViewById(R.id.registerTextLink);
        registerTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(SignIn.this, Register.class);
                SignIn.this.startActivity(register);
            }
        });

    }

    private void signIn(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"please enter an email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"please enter a password",Toast.LENGTH_SHORT).show();
            return;
        }

        prog.setMessage("Signing in...");
        prog.show();
        
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                prog.dismiss();

                if(task.isSuccessful()){
                    finish();
                    Intent home = new Intent(SignIn.this, Home.class);
                    SignIn.this.startActivity(home);
                }
                else{
                    Toast.makeText(SignIn.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
