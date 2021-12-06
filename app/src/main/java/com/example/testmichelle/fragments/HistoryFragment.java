package com.example.testmichelle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testmichelle.R;
import com.example.testmichelle.activities.BasicActivity;
import com.example.testmichelle.model.Algorithm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.threeten.bp.ZonedDateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ArrayList masterList = createDataFromAlgorithms();
        System.out.println("MASTERLIST:" + masterList);

        return view;
    }


    /**
     * Function to get the relevant data to render in the history fragment.
     * @return 2d array where each row will be a specific recyclerview unit in the form [Algoname, Ticker, Buy/Sell, Amount, Price, Date]
     */
    public ArrayList<ArrayList<Object>> createDataFromAlgorithms() {

        ArrayList MasterList = new ArrayList<ArrayList<Object>>();
        for (Map.Entry<String, ArrayList<Object>> entry : BasicActivity.algorithmsRan.entrySet()) {
            System.out.println("CALLING createDataFromSingleAlgorithm FOR " + entry.getKey());
            ArrayList dataFromSpecificAlgorithm = createDataFromSingleAlgorithm(entry);
            MasterList.addAll(dataFromSpecificAlgorithm);
        }
        return MasterList;
    }

    /**
     * Function to get the relevant data to render in the history fragment for a specific algorithm
     * @return 2d array where each row will be a specific recyclerview unit in the form [Algoname, Ticker, Buy/Sell, Amount, Price, Date]
     */
    public ArrayList<ArrayList<Object>> createDataFromSingleAlgorithm(Map.Entry<String, ArrayList<Object>> entry) {
        TradingRecord tradingRecord = (TradingRecord) entry.getValue().get(0);
        TimeSeries series = (TimeSeries) entry.getValue().get(1);
        String ticker = (String) entry.getValue().get(2);

        List<Trade> trades = tradingRecord.getTrades();

        ArrayList finalArray = new ArrayList();

        for (int i = 0; i < trades.size(); i++) {
            Trade trade = trades.get(i);

            // Get entry Order variables
            Order entryTrade = trade.getEntry();

            String algName = (String) entry.getKey();
            String type = "Buy";
            double amountTraded = entryTrade.getAmount().doubleValue();
            double priceValue = entryTrade.getPrice().doubleValue();
            int index = entryTrade.getIndex();
            ZonedDateTime date = series.getBar(index).getBeginTime();

            // Append entry Order variables to entry order list
            ArrayList entryOrderList = new ArrayList<>(Arrays.asList(algName, ticker, type, amountTraded, priceValue, date));

            // Append entry Order List to final Array
            finalArray.add(entryOrderList);

//            series.getSubSeries(); USE FOR ALGORITHMS WHICH ARENT STILL RUNNING

            // Check if the trade is closed yet so know if should append sell order
            if (trade.isClosed()) {

                // Get exit order variables
                Order exitTrade = trade.getExit();
                type = "Sell";
                amountTraded = exitTrade.getAmount().doubleValue();
                priceValue = exitTrade.getPrice().doubleValue();
                index = exitTrade.getIndex();
                date = series.getBar(index).getBeginTime();

                // Append exit order variables to exit order list
                ArrayList exitOrderList = new ArrayList<>(Arrays.asList(algName, ticker, type, amountTraded, priceValue, date));

                // Append exit Order List to final Array
                finalArray.add(exitOrderList);
            }
        }
        return finalArray;
    }

}