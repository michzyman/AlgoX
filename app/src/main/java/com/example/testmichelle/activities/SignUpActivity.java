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

public class SignUpActivity extends AppCompatActivity {
    // UI references.
    private EditText signup_Name;
    private EditText signup_Email;
    private EditText signup_Password;
    private Button btn_Done;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //FireBase
        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference databaseReference = database.getReference("Registered Users");

        signup_Email = findViewById(R.id.signup_Email);
        signup_Name = findViewById(R.id.signup_Name);
        signup_Password = findViewById(R.id.signup_Password);
        mAuth = FirebaseAuth.getInstance();

        btn_Done = (Button) findViewById(R.id.btn_Done);
        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  mAuth.getInstance();
                userSignUp();
            }
        });

    }


    private void userSignUp() {
        /*
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        signup_Email.setError(null);
        signup_Name.setError(null);
        signup_Password.setError(null);
    */
        // Store values at the time of the login attempt.
        String email = signup_Email.getText().toString().trim();
        String password = signup_Password.getText().toString().trim();
        String name = signup_Name.getText().toString().trim();

        //boolean cancel = false;
       // View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            signup_Password.setError("Invalid Password, Must be 6 characters long");
            return;
            //focusView = signup_Password;
            //cancel = true;
        }
        // Check for a valid email address.
        // 1. Checks if it is empty 2. If it is not empty it checks if it is valid
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            signup_Email.setError("Valid Email is required");
            return;
            //focusView = signup_Email;
            //cancel = true;
        }

        /*
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
        */


        // Register the NEW USER in Firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            //Create a new User
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    UserProfile newUser = new UserProfile(name, email, password);
                    //FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
                    databaseReference.child(firebaseUser.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Set the data to the Real Time Data Base
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), BasicActivity.class));
                                Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();
                            }
                            else{
                                    Toast.makeText(SignUpActivity.this, "ERROR: Failed to create a new user", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    databaseReference.child(firebaseUser.getUid()).child("Stocks").child("StockName").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("Stocks").child("NumShares").setValue("");

                    databaseReference.child(firebaseUser.getUid()).child("CurrentMoney").setValue("");

                    databaseReference.child(firebaseUser.getUid()).child("CurrentAlgorithms").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("CurrentAlgorithms").child("Algorithm").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("CurrentAlgorithms").child("StockName").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("CurrentAlgorithms").child("Algorithm").child("AlgorithmName").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("CurrentAlgorithms").child("Algorithm").child("AlgorithmType").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("CurrentAlgorithms").child("Algorithm").child("AlgorithmParameter").setValue("");

                    databaseReference.child(firebaseUser.getUid()).child("HistoryOfTransaction").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("HistoryOfTransaction").child("StockName").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("HistoryOfTransaction").child("TransactionType").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("HistoryOfTransaction").child("Money").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("HistoryOfTransaction").child("DateOfTransaction").setValue("");
                    databaseReference.child(firebaseUser.getUid()).child("HistoryOfTransaction").child("Algorithm").setValue("");


                }
                    else{
                        Toast.makeText(SignUpActivity.this, "ERROR" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
            }
        });

    }


    private boolean isEmailValid(String email) {
        CharSequence s = email;
        return !TextUtils.isEmpty(s) && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }


    /*
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mName;

        private boolean registerSuccess = false;
        private String registerResponose = null;

        UserLoginTask(String email, String password, String name) {
            mEmail = email;
            mPassword = password;
            mName = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                auth.createUserWithEmailAndPassword(mEmail, mPassword).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        registerResponose = e.getMessage();
                        registerSuccess = false;
                        Toast.makeText(getApplicationContext(), registerResponose, Toast.LENGTH_LONG).show();
                        //USER IS NOW LOGGED IN!
                        //We could redirect to main page if needed
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG).show();
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        User account = new User(mName, mEmail, mPassword);
                        database.getReference().child(firebaseUser.getUid()).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                   // startActivity(new Intent(getApplicationContext(), BasicActivity.class));
                                    registerSuccess = true;
                                    Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //User writeUserDetails = new User(mName, mEmail, mPassword);
                       // database.getReference().child(firebaseUser.getUid()).setValue(account);
                    }
                });
                Thread.sleep(1000);

            } catch (InterruptedException exception) {
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

     */
}

