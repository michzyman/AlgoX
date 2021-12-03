package com.example.testmichelle.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testmichelle.R;
import com.example.testmichelle.activities.FragmentListener;

public class MoreInfoFragment extends Fragment {

    private FragmentListener FL;
    private Button btn_to_backtesting;

    public MoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FL = (FragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moreinfo, container, false);
        btn_to_backtesting = (Button) view.findViewById(R.id.btn_to_backtesting);

        btn_to_backtesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FL.goToBackTestingFragment();
            }
        });
        return view;
    }

    public void giveInformation(String algorithmType, String par1, String par2, String buyOrSell) {
        switch (algorithmType) {
            case "SMA":
                String s = "This algorithm takes the Average of the closing prices in the past";
                break;
            case "EMA":
                break;
            case "Falling Rule":
                break;
            case "Rising Rule":
                break;
            case "Trigger Above":
                break;
            case "Trigger Below":
                break;
        }
    }
}
