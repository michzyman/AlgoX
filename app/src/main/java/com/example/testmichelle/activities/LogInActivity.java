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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        //ADD PROGRESS BAR HERE
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
                if (!task.isSuccessful()) {
                    //There was an Error
                    Toast.makeText(LogInActivity.this, "Login Error, Please Login Again!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent_home = new Intent(LogInActivity.this, BasicActivity.class);
                    startActivity(intent_home);
                    finish();
                }
            }
        });
    }
    public void userSignUp(){
        Intent intent_signup = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent_signup);
    }

}


    /*
    private void attemptLogin(){
        if (mAuthTask != null){
            return;
        }

        boolean cancel = false;
        View focusView = null;

        //Store values at the time of the login attempt
        String email = edt_Email.getText().toString();
        String password = edt_Password.getText().toString();


        //Check for a valid email address
        if(TextUtils.isEmpty(email)){
            edt_Email.setError("Invalid Email");
            focusView = edt_Email;
            cancel = true;
        }else if(!isEmailValid(email)){
            edt_Email.setError("Email is invalid");
            focusView = edt_Email;
            cancel = true;
        }

        //Check for a valid password
        if (!isPasswordValid(password)) {
            edt_Password.setError("Password does not match the email");
            focusView = edt_Password;
            cancel = true;
        }

        //If there was an error do not attempt login
        //Else perform the user login attempt
        if(cancel){
            focusView.requestFocus();
        }
        else {
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void)null);
        }

    }


    private boolean isEmailValid(String email){
        CharSequence s = email;
        return !TextUtils.isEmpty(s) && Patterns.EMAIL_ADDRESS.matcher(s).matches();

    }
    private boolean isPasswordValid (String password){
        return password.length() > 6;
    }
    public class UserLoginTask extends AsyncTask<Void, Void, Void> {
        //Asunchronous Login that authenticates the user
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }
        @Override
        protected Void doInBackground(Void... params) {
            boolean loggedIn = false;
            account = UserAccount.getInstance();
            account.auth.signInWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener(LogInActivity.this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LogInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent_home = new Intent(LogInActivity.this, BasicActivity.class);
                    startActivity(intent_home);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mAuthTask = null;
                    UserAccount.signOut();
                    Toast.makeText(getApplicationContext(), "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mAuthTask = null;
                }
            });
            return null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            UserAccount.signOut();
        }
    }
    public void saveLoginInfo(Context context, String username, String password) {
        SharedPreferences sharedPre = context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

     */






