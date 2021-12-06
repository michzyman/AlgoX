package com.example.testmichelle.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmichelle.R;
import com.example.testmichelle.activities.FragmentListener;
import com.example.testmichelle.activities.BasicActivity;
import com.example.testmichelle.model.UserMoney;
import com.example.testmichelle.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TextView text_name;
    TextView text_balance;
    TextView text_cash;
    TextView text_algorithm_results;
    Spinner SpinnerOfAlgorithms;
    GraphView graphAlgorithms;
    HashMap<String, ArrayList<Object>> Algorithms;
    HashMap<String, ArrayList<Object>> AlgorithmsRan;
    private FragmentListener FL;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FL = (FragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        text_name = (TextView) view.findViewById(R.id.text_username);
        text_name.setVisibility(View.INVISIBLE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile name = snapshot.getValue(UserProfile.class);
                text_name.setText("Hello," +" "+ name.getName());
                text_name.setTextSize(34);
                text_name.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        text_balance = (TextView) view.findViewById(R.id.text_balance);
        text_balance.setVisibility(View.INVISIBLE);
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserMoney money = snapshot.getValue(UserMoney.class);
                text_balance.setText("Your Balance " + "\n" + "$"+ money.getCurrentbalance());
                text_balance.setTextSize(24);
                text_balance.setGravity(Gravity.CENTER);
                text_balance.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        text_cash = (TextView) view.findViewById(R.id.text_cash);
        text_cash.setVisibility(View.INVISIBLE);
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserMoney money = snapshot.getValue(UserMoney.class);
                text_cash.setText("Cash Remaining " + "\n" + "$"+ money.getCurrentbalance());
                text_cash.setTextSize(24);
                text_cash.setGravity(Gravity.CENTER);
                text_cash.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        graphAlgorithms = (GraphView) view.findViewById(R.id.graphAlgorithms);
        text_algorithm_results = (TextView) view.findViewById(R.id.text_algorithm_results);
        SpinnerOfAlgorithms = (Spinner) view.findViewById(R.id.SpinnerOfAlgorithms);
        FL.passDataToHomeFragment();
        if (Algorithms != null) {
            if (!Algorithms.isEmpty()){
                ArrayList<String> SpinnerValues = new ArrayList<String>();
                for(String key: Algorithms.keySet()){
                    SpinnerValues.add(key);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SpinnerValues);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerOfAlgorithms.setAdapter(adapter);
                SpinnerOfAlgorithms.setOnItemSelectedListener(this);
            }
        }
        return view;
    }

    public void graphAlgorithm(String Algorithm){
        ArrayList<Double> stockPrices = createListOfAlgorithmValues(Algorithm);
        graphAlgorithms.removeAllSeries();
        graphAlgorithms.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX){
                    return "";
                } else {
                    return value + "$";
                }
            }
        });
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < stockPrices.size(); i++){
            DataPoint currentDataPoint = new DataPoint(i, stockPrices.get(i));
            series.appendData(currentDataPoint, true, stockPrices.size());
        }
        graphAlgorithms.addSeries(series);
        graphAlgorithms.setTitle(Algorithm + "'s performance so far");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(getContext(),
                parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG)
                .show();
        graphAlgorithm(parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    public void setAlgorithms(HashMap<String, ArrayList<Object>> algorithms, HashMap<String, ArrayList<Object>> algorithmsRan) {

        Algorithms = algorithms;
        AlgorithmsRan = algorithmsRan;
    }

    public ArrayList<Double> createListOfAlgorithmValues(String algorithmName) {
        ArrayList algorithmData = AlgorithmsRan.get(algorithmName);
        Log.e("whoops", algorithmData.toString());

        TimeSeries series = (TimeSeries) algorithmData.get(1);
        TradingRecord tradingRecord = (TradingRecord) algorithmData.get(0);
        String ticker = (String) algorithmData.get(2);

        ArrayList<Double> resultingList = new ArrayList<Double>();

        for (int i = 0; i < series.getBarCount(); i++) {
            resultingList.add(1.);
        }

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

        Double startingValue = ((Integer) Algorithms.get(algorithmName).get(2)).doubleValue();

        ArrayList<Double> finalList = new ArrayList<Double>();

        for (int i = 0; i < resultingList.size(); i++) {
            startingValue *= resultingList.get(i);
            finalList.add(startingValue);
        }
        return finalList;
    }

//    public static ArrayList<Double> createListOfAlgorithmValues(TimeSeries series, TradingRecord tradingRecord, double startingValue) {
//
//        ArrayList<Double> resultingList = new ArrayList<Double>();
//        for (int i = 0; i < series.getBarCount(); i++) {
//            resultingList.add(1.);
//        }
//
//        int numberOfProfitable = 0;
//        for (Trade trade : tradingRecord.getTrades()) {
//            int entryIndex = trade.getEntry().getIndex();
//            int exitIndex = trade.getExit().getIndex();
//
//            double result;
//            if (trade.getEntry().isBuy()) {
//                // buy-then-sell trade
//                result = series.getBar(exitIndex).getClosePrice().dividedBy(series.getBar(entryIndex).getClosePrice()).doubleValue();
//            } else {
//                // sell-then-buy trade
//                result = series.getBar(entryIndex).getClosePrice().dividedBy(series.getBar(exitIndex).getClosePrice()).doubleValue();
//            }
//
//            resultingList.set(exitIndex, result);
//        }
//
//        ArrayList<Double> finalList = new ArrayList<Double>();
//
//        for (int i = 0; i < resultingList.size(); i++) {
//            startingValue *= resultingList.get(i);
//            finalList.add(startingValue);
//        }
//        return finalList;
//    }


}