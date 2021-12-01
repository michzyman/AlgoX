/*
package com.example.testmichelle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testmichelle.activities.BasicActivity;
import com.example.testmichelle.model.ReadWriteData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button btn_Done;
    EditText signup_Name;
    EditText signup_Email;
    EditText signup_Password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup_Email = findViewById(R.id.signup_Email);
        signup_Name = findViewById(R.id.signup_Name);
        signup_Password = findViewById(R.id.signup_Password);
        mAuth = FirebaseAuth.getInstance();

        btn_Done = (Button) findViewById(R.id.btn_Done) ;
        btn_Done.setOnClickListener(this);
    }
    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btn_Done:
                String email = signup_Email.getText().toString().trim();
                String password = signup_Password.getText().toString().trim();
                String name = signup_Name.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    signup_Email.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    signup_Password.setError("Password is required");
                    return;
                }
                if(password.length()<6){
                    signup_Password.setError("Password must be 6 characters long");
                }
                //Register the user in firebase
               mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   //Create the User
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           FirebaseUser firebaseUser = mAuth.getCurrentUser();
                           //Enter User Data into the Firebase Realtime Database
                           ReadWriteData writeUserDetails = new ReadWriteData(name, email, password);

                           DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registerd Users");

                           referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {

                               @Override
                               // Set the data to the database Real Time
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       startActivity(new Intent(getApplicationContext(), BasicActivity.class));
                                       Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_LONG).show();
                                   }
                                   else{
                                       Toast.makeText(SignUp.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                   }
                               }
                           });
                       }
                       else{
                           Toast.makeText(SignUp.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                       }
                   }
               });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }

}
*/