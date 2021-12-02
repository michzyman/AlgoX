package com.example.testmichelle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testmichelle.R;

import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public static ArrayList<Double> createListOfAlgorithmValues(TimeSeries series, TradingRecord tradingRecord, double startingValue) {

        ArrayList<Double> resultingList = new ArrayList<Double>();
        for (int i = 0; i < series.getBarCount(); i++) {
            resultingList.add(1.);
        }

        int numberOfProfitable = 0;
        for (Trade trade : tradingRecord.getTrades()) {
            int entryIndex = trade.getEntry().getIndex();
            int exitIndex = trade.getExit().getIndex();

            double result;
            if (trade.getEntry().isBuy()) {
                // buy-then-sell trade
                result = series.getBar(exitIndex).getClosePrice().dividedBy(series.getBar(entryIndex).getClosePrice()).doubleValue();
            } else {
                // sell-then-buy trade
                result = series.getBar(entryIndex).getClosePrice().dividedBy(series.getBar(exitIndex).getClosePrice()).doubleValue();
            }

            resultingList.set(exitIndex, result);
        }

        ArrayList<Double> finalList = new ArrayList<Double>();

        for (int i = 0; i < resultingList.size(); i++) {
            startingValue *= resultingList.get(i);
            finalList.add(startingValue);
        }
        return finalList;
    }
}