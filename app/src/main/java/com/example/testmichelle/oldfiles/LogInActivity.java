/*
package com.example.testmichelle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private Button btn_SignIn;
    private Button btn_SignUp;
    private EditText edt_Email;
    private EditText edt_Password;
    //FirebaseAuth mAuth;
   // private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_Email = (EditText) findViewById(R.id.edt_Email);
        edt_Password = (EditText) findViewById(R.id.edt_Password);
        btn_SignIn = (Button) findViewById(R.id.btn_SignIn);
        btn_SignUp = (Button) findViewById(R.id.btn_SignUp);




        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LogInActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LogInActivity.this, BasicActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LogInActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };


        // btn_SignIn.setOnClickListener(this);
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_Email.getText().toString();
                String password = edt_Password.getText().toString();
                if (email.isEmpty() && password.isEmpty()) { //FIX THIS
                    edt_Email.setError("Please enter your email address");
                    edt_Email.requestFocus();
                    edt_Password.setError("Please enter your password");
                    edt_Password.requestFocus();
                    Toast.makeText(LogInActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if (email.isEmpty()) {
                    edt_Email.setError("Please enter a valid email address");
                    edt_Email.requestFocus();
                } else if (password.isEmpty()) {
                    edt_Password.setError("Please enter your password");
                    edt_Password.requestFocus();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Login Error, Please Login Again!", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent_home = new Intent(LogInActivity.this, BasicActivity.class);
                                startActivity(intent_home);
                            }

                        }
                    });
                } else {
                    Toast.makeText(LogInActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        btn_SignUp = (Button) findViewById(R.id.btn_SignUp);
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_signup = new Intent(LogInActivity.this, SignUp.class);
                startActivity(intent_signup);
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.signOut();
    }
}
*/