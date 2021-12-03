package com.example.testmichelle.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.testmichelle.R;
import androidx.fragment.app.Fragment;
import org.ta4j.core.TradingRecord;

public class DisplayBackTestingResults extends Fragment {

    public static TextView tvResults;
    public static TradingRecord tradingRecord;
    
    public DisplayBackTestingResults(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backtesingresults, container, false);
        tvResults = (TextView) view.findViewById(R.id.tvResult);
        return view;
    }

    public void updateTextView(String message, TradingRecord record){
        tradingRecord = record;
    }
}
