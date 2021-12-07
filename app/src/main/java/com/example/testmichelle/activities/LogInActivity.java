package com.example.testmichelle.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testmichelle.R;
import com.example.testmichelle.model.UserMoney;
import com.example.testmichelle.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    //UI Reference
    private Button btn_SignIn;
    private Button btn_SignUp;
    private EditText edt_Email;
    private EditText edt_Password;
    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edt_Email = (EditText) findViewById(R.id.edt_Email);
        edt_Password = (EditText) findViewById(R.id.edt_Password);
        btn_SignIn = (Button) findViewById(R.id.btn_SignIn);
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogIn();
            }
        });

        btn_SignUp = (Button) findViewById(R.id.btn_SignUp);
        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
            }
        });
    }
    private void userLogIn() {

        String email = edt_Email.getText().toString().trim();
        final String password = edt_Password.getText().toString().trim();

        if (email.isEmpty() && password.isEmpty()) { //FIX THIS
            edt_Email.setError("Please enter your email address");
            edt_Email.requestFocus();
            edt_Password.setError("Please enter your password");
            edt_Password.requestFocus();
            Toast.makeText(LogInActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
        }
        if (email.isEmpty()) {
            edt_Email.setError("Please enter a valid email address");
            edt_Email.requestFocus();
        }

        if (password.isEmpty()) {
            edt_Password.setError("Please enter your password");
            edt_Password.requestFocus();
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent_home = new Intent(LogInActivity.this, BasicActivity.class);
                    startActivity(intent_home);
                    finish();

                } else {
                    Toast.makeText(LogInActivity.this, "Login Error, Please Login Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void userSignUp(){
        Intent intent_signup = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent_signup);
    }
}








