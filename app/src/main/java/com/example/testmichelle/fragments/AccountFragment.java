package com.example.testmichelle.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.testmichelle.R;
import com.example.testmichelle.activities.LogInActivity;


public class AccountFragment extends Fragment {
    Button btn_LogOut;
    EditText test;



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
        test = view.findViewById(R.id.test);
        String Test = test.getText().toString().trim();



        btn_LogOut = (Button) view.findViewById(R.id.btn_LogOut);
        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_signin = new Intent(getActivity(),
                        LogInActivity.class);
                startActivity(intent_signin);
            }
        });
        return view;

    }



}