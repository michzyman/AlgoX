package com.example.testmichelle.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testmichelle.R;
import com.example.testmichelle.model.Algorithm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.ZonedDateTime;

public class cancelAlgorithmPopUp extends AppCompatActivity {

    TextView tv_canceltext;
    Button btn_cancel;
    String idToCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String algoToCancel = (String) extras.get("algorithmName");
        setContentView(R.layout.activity_cancelalgorithmpopup);
        AndroidThreeTen.init(this);

        tv_canceltext = (TextView) findViewById(R.id.tv_canceltext);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        tv_canceltext.setText(R.string.cancel_text_view);
        btn_cancel.setText(R.string.btn_popup_cancel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.75), (int) (height*.75));


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("Algorithms");
                databaseReference.orderByChild("algoname").equalTo(algoToCancel).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Log.d("User key", child.getKey());
                            idToCancel = child.getKey();
                            Log.d("User ref", child.getRef().toString());
                            Log.d("User val", child.getValue().toString());
//                            Algorithm algo = child.getValue(Algorithm.class);
//                            boolean currently = algo.status;
//                            if(currently) {
                                databaseReference.child(idToCancel).child("status").setValue(false);
                                String ending = String.valueOf(ZonedDateTime.now());
                                databaseReference.child(idToCancel).child("end_date").setValue(ending);
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(), "This algorithm was already canceled",Toast.LENGTH_LONG).show();
//                            }
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
