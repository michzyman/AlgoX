package com.example.testmichelle.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testmichelle.R;
import com.example.testmichelle.activities.LogInActivity;
import com.example.testmichelle.model.UserMoney;
import com.example.testmichelle.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;

import org.w3c.dom.Text;


public class AccountFragment extends Fragment {
    Button btn_LogOut;
    TextView text_username2;
    TextView text_balance2;
    GraphView graphPortofolio;



    public AccountFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context){

        super.onAttach(context);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        btn_LogOut = (Button) view.findViewById(R.id.btn_LogOut);
        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_signin = new Intent(getActivity(),
                        LogInActivity.class);
                startActivity(intent_signin);
            }
        });
        text_username2 = (TextView) view.findViewById(R.id.text_username2);
        text_username2.setVisibility(View.INVISIBLE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile name = snapshot.getValue(UserProfile.class);
                text_username2.setText(name.getName());
                text_username2.setTextSize(34);
                text_username2.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        text_balance2 = (TextView) view.findViewById(R.id.text_balance2);
        text_balance2.setVisibility(View.INVISIBLE);
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserMoney money = snapshot.getValue(UserMoney.class);
                text_balance2.setText("Your Balance " + "\n" + "$"+ money.getCurrentbalance());
                text_balance2.setTextSize(34);
                text_balance2.setGravity(Gravity.CENTER);
                text_balance2.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        graphPortofolio = (GraphView) view.findViewById(R.id.graphPortofolio);
        return view;

    }



}