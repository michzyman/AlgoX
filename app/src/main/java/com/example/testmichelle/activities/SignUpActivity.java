package com.example.testmichelle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testmichelle.R;
import com.example.testmichelle.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    // UI references.
    private EditText signup_Name;
    private EditText signup_Email;
    private EditText signup_Password;
    private EditText signup_Lastname;
    private Button btn_Done;
    Integer currentbalance = 20000;
    Integer freecash = 20000;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Getting all the UI ID's
        signup_Email = (EditText) findViewById(R.id.signup_Email);
        signup_Name = (EditText) findViewById(R.id.signup_Name);
        signup_Password = (EditText) findViewById(R.id.signup_Password);
        signup_Lastname = (EditText) findViewById(R.id.signup_Lastname);
        btn_Done = (Button) findViewById(R.id.btn_Done);


        //Instance to the Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Calling userSignUp when the user clicks the button btn_Done to
        // create a new id for that user in the database and stored the user's account
        // information under that reference
        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
            }
        });

    }


    private void userSignUp() {

        // Store values at the time of the login attempt.
        String email = signup_Email.getText().toString().trim();
        String password = signup_Password.getText().toString().trim();
        String name = signup_Name.getText().toString().trim();
        String lastname = signup_Lastname.getText().toString().trim();


        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            signup_Password.setError("Invalid Password, Must be 6 characters long");
            return;
        }

        // Check for a valid email address.
        // 1. Checks if it is empty 2. If it is not empty it checks if it is valid
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            signup_Email.setError("Valid Email is required");
            return;
        }

        // Register the NEW USER in Firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            //Create a new User using email and password
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    UserProfile newUser = new UserProfile(name,lastname, email, password);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
                    databaseReference.child(firebaseUser.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Set the data to the Real Time Data Base
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), BasicActivity.class));
                                Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, "ERROR: Failed to create a new user", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //Creates a new child under the user that stores the currentbalance
                    databaseReference.child(firebaseUser.getUid()).child("currentbalance").setValue(currentbalance);
                    //Creates a new child under the user that stores the freecash
                    databaseReference.child(firebaseUser.getUid()).child("freecash").setValue(freecash);
                    //Creates a new child under the user that will store the Algorithms
                    databaseReference.child(firebaseUser.getUid()).child("Algorithms");

                } else {
                    //ERROR HANDELING
                    Toast.makeText(SignUpActivity.this, "ERROR" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Checks if the user is entering an email address
    private boolean isEmailValid(String email) {
        CharSequence s = email;
        return !TextUtils.isEmpty(s) && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }


    //Checks if the password that the user entered is 6 characters or more
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}



