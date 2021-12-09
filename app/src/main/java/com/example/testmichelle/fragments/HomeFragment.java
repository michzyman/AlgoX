package com.example.testmichelle.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.testmichelle.R;
import com.example.testmichelle.activities.FragmentListener;
import com.example.testmichelle.activities.cancelAlgorithmPopUp;
import com.example.testmichelle.model.Algorithm;
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
import org.threeten.bp.ZonedDateTime;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    TextView text_name;
    TextView text_balance;
    TextView text_cash;
    TextView text_buying_rule;
    TextView text_selling_rule;
    TextView text_algorithm_results;
    Spinner SpinnerOfAlgorithms;
    GraphView graphAlgorithms;
    HashMap<String, ArrayList<Object>> Algorithms;
    HashMap<String, ArrayList<Object>> AlgorithmsRan;
    private FragmentListener FL;
    Button btnCancelAlgorithm;
    Integer amountWeStartedWith;

    public HomeFragment() {
        // Required empty public constructor
    }

    //Override onAttach to gain access to the fragment listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FL = (FragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        text_name = (TextView) view.findViewById(R.id.text_username);
        text_name.setVisibility(View.INVISIBLE);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile name = snapshot.getValue(UserProfile.class);
                text_name.setText("Hello," + " " + name.getName());
                text_name.setTextSize(34);
                text_name.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FL.passDataToHomeFragment();
        text_balance = (TextView) view.findViewById(R.id.text_balance);
        text_balance.setText("Portfolio Value " + "\n" + "$" + getTotalPortfolioValue());
        text_balance.setTextSize(20);
        text_balance.setGravity(Gravity.CENTER);
        text_balance.setVisibility(View.VISIBLE);
        text_cash = (TextView) view.findViewById(R.id.text_cash);
        text_cash.setVisibility(View.INVISIBLE);
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserMoney money = snapshot.getValue(UserMoney.class);
                text_cash.setText("Cash Remaining " + "\n" + "$"+ money.getFreecash());
                text_cash.setTextSize(20);
                text_cash.setGravity(Gravity.CENTER);
                text_cash.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        graphAlgorithms = (GraphView) view.findViewById(R.id.graphAlgorithms);
        graphAlgorithms.setVisibility(View.INVISIBLE);

        text_algorithm_results = (TextView) view.findViewById(R.id.text_algorithm_results);
        text_algorithm_results.setVisibility(View.INVISIBLE);
        text_buying_rule = (TextView) view.findViewById(R.id.text_buying_rule);
        text_buying_rule.setVisibility(View.INVISIBLE);
        text_selling_rule = (TextView) view.findViewById(R.id.text_selling_rule);
        text_selling_rule.setVisibility(View.INVISIBLE);
        SpinnerOfAlgorithms = (Spinner) view.findViewById(R.id.SpinnerOfAlgorithms);

        // CHeck if algorithms is null to make sure that the data was passed in correctly
        if (Algorithms != null) {
            if (!Algorithms.isEmpty()) {
                // Add each algorithm name to an Array list
                ArrayList<String> SpinnerValues = new ArrayList<String>();
                for (String key : Algorithms.keySet()) {
                    SpinnerValues.add(key);
                }
                //Initlize the spinner to have the names of each algorithm.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SpinnerValues);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerOfAlgorithms.setAdapter(adapter);
                SpinnerOfAlgorithms.setOnItemSelectedListener(this);
            }
        }
        btnCancelAlgorithm = view.findViewById(R.id.btnCancelButton);
        // Cancel Button functionality
        btnCancelAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String algoToCancel = SpinnerOfAlgorithms.getSelectedItem().toString();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("Algorithms");
                databaseReference.orderByChild("algoname").equalTo(algoToCancel).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Algorithm algo = child.getValue(Algorithm.class);
                            boolean currently = algo.status;
                            if (currently) {
                                Intent intent = new Intent(getContext(), cancelAlgorithmPopUp.class);
                                intent.putExtra("algorithmName", SpinnerOfAlgorithms.getSelectedItem().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "This algorithm was already canceled", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }

    // This method returns the string represenation of a ZonedDataTime Object
    public String ZonedDateTimetoString(ZonedDateTime date){
        String result = "";
        result += date.getMonth().getValue() + "/";
        result += date.getDayOfMonth() + "/";
        result += date.getYear();
        return result;
    }

    /*
    Given the name of the algorithm, this method will graph its performance
     */
    public void graphAlgorithm(String Algorithm){
        // get the stock prices of the algorithm
        ArrayList<Double> stockPrices = createListOfAlgorithmValues(Algorithm);
        // make to remove all the data in the graph before graphing new data
        graphAlgorithms.removeAllSeries();
        // Access the start data of the algorithm to graph the dates on the graph
        String startDateString = (String) Algorithms.get(Algorithm).get(5);
        ZonedDateTime startDate = ZonedDateTime.parse(startDateString);
        final Boolean[] print = {true};
        // overwrite label function to inlcude dates and round decimals before displaying on the graph
        graphAlgorithms.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX){
                    // outputing dates on the x axis
                    // we skip every other x coordinate to avoid the dates colliding
                    String date = ZonedDateTimetoString(startDate.plusDays(Double.valueOf(value).longValue()));
                    if(print[0]){
                        print[0] = false;
                        return date;
                    } else {
                        print[0] = true;
                        return "";
                    }
                } else {
                    // rounding decimals
                    return "$" + (Math.round(value * 100.0) / 100.0);
                }
            }
        });
        // add the data to a series
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        for (int x = 0; x < stockPrices.size() - 1; x++){
            Double y = stockPrices.get(x);
            series.appendData(new DataPoint(x, y), true, stockPrices.size());
        }
        // styling the graphs
        series.setColor(getResources().getColor(R.color.green));
        series.setThickness(10);
        // add series to graph to display the new graph
        graphAlgorithms.addSeries(series);
        graphAlgorithms.setVisibility(View.VISIBLE);
        // set Scalable and Scrollable to true to see all of the data
        graphAlgorithms.getViewport().setScalable(true);
        graphAlgorithms.getViewport().setScrollable(true);
        // Set new of the graph
        graphAlgorithms.setTitle(Algorithm + "'s performance so far");
    }

    /*
    Override spinner method to display data and graph alggorithm when the algorithm is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        graphAlgorithm(parent.getItemAtPosition(pos).toString());
        displayData(parent.getItemAtPosition(pos).toString());
    }

    /*
    Include this method to successfully implement interface.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /*
    This method is used to display the statistics of the chosen algorithmm
     */
    public void displayData(String AlgorithmName){

        // Collecting statistics of current algorithm
        ArrayList algorithmData = Algorithms.get(AlgorithmName);
        ArrayList<String> buyingRuleData = (ArrayList<String>) algorithmData.get(0);
        ArrayList<String> sellingRuleData = (ArrayList<String>) algorithmData.get(1);
        String buyingRulePar1 = buyingRuleData.get(0);
        String buyingRulePar2 = buyingRuleData.get(1);
        String buyingRule = buyingRuleData.get(2);
        String sellingRulePar1 = sellingRuleData.get(0);
        String sellingRulePar2 = sellingRuleData.get(1);
        String sellingRule = sellingRuleData.get(2);
        Double initialValue = getAlgorithmStartingValue(AlgorithmName);
        Double finalValue = getAlgorithmValue(AlgorithmName);
        Double profit = getAlgorithmProfit(AlgorithmName);
        String buyingRuleText = generateStringForAlgorithmText("Buying rule", buyingRule, buyingRulePar1, buyingRulePar2);
        String sellingRuleText = generateStringForAlgorithmText("Selling rule", sellingRule, sellingRulePar1, sellingRulePar2);
        String resultText = "Initial Value: " + initialValue + "\n";
        resultText += "Final Value: " + Math.round(finalValue * 100.0) / 100.0 + "\n";
        resultText += "Profit: " + Math.round(profit * 100.0) / 100.0 + "\n";

        // Displaying States
        text_buying_rule.setText(buyingRuleText);
        text_buying_rule.setTextSize(13);
        text_buying_rule.setVisibility(View.VISIBLE);
        text_selling_rule.setText(sellingRuleText);
        text_selling_rule.setTextSize(13);
        text_selling_rule.setVisibility(View.VISIBLE);
        text_algorithm_results.setText(resultText);
        text_algorithm_results.setTextSize(13);
        text_algorithm_results.setVisibility(View.VISIBLE);
    }

    /*
    This method will generate the string on how the algorithm will be displayed on display text boces
     */
    public String generateStringForAlgorithmText(String rule, String Algorithmtype, String par1, String par2){
        String result = rule + ": " + Algorithmtype + "\n";
        if (Algorithmtype.equals("Price Above")  || Algorithmtype.equals("Price Below")){
            result += "Cutoff: " + par1;
        } else if (Algorithmtype.equals("SMA")  || Algorithmtype.equals("EMA")){
            result += "Duration 1: " + par1 + "\n";
            result += "Duration 2: " + par2 + "\n";
        } else if (Algorithmtype.equals("Rising")  || Algorithmtype.equals("Falling")){
            result += "Bar count: " + par1 + "\n";
            result += "Min Strength: " + par2 + "\n";
        }
        return result;
    }

    /*
    Fragment lister will use this method to pass the data into the home fragment.
     */
    public void setAlgorithms(HashMap<String, ArrayList<Object>> algorithms, HashMap<String, ArrayList<Object>> algorithmsRan) {
        Algorithms = algorithms;
        AlgorithmsRan = algorithmsRan;
    }

    public ArrayList<Double> createListOfAlgorithmValues(String algorithmName) {
        ArrayList algorithmData = AlgorithmsRan.get(algorithmName);
        if (algorithmData!=null) {
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
            if (resultingList.size() == 0 || resultingList.size() == 1) {
                finalList.add(startingValue);
                return finalList;
            }

            for (int i = 0; i < resultingList.size(); i++) {
                startingValue *= resultingList.get(i);
                finalList.add(startingValue);
            }

            return finalList;
        } else {
            ArrayList defaultList = new ArrayList();
            Double startingValue = ((Integer) Algorithms.get(algorithmName).get(2)).doubleValue();
            defaultList.add(startingValue);
            return defaultList;
        }
    }

    public Double roundToTwoDecimal(Double unrounded) {
        return Math.round(unrounded * 100.0) / 100.0;
    }

    /**
     * Calculates the sum of the profit made by the algorithms in the user's portfolio
     * @return total current value of your portfolio of algorithms
     */
    public Double getTotalPortfolioValue() {
        Double totalValue = 0.;
        for (Map.Entry<String, ArrayList<Object>> entry : Algorithms.entrySet()) {
            totalValue += getAlgorithmValue(entry.getKey());
        }
        Double roundedVal = roundToTwoDecimal(totalValue);
        return roundedVal;
    }

    /**
     *
     * @param algorithmName Name of the algorithm to get the profit for
     * @return the profit of each algorithm running to update the total profit
     */
    public Double getAlgorithmValue(String algorithmName) {
        ArrayList<Double> values = createListOfAlgorithmValues(algorithmName);
        Double finalValue = values.get(values.size() - 1);
        System.out.println("Algorithm Value for " + algorithmName + ": " + finalValue);
        return finalValue;
    }

    /**
     * Set the current portfolio value on the home page to the total portfolio value
     * @param totalProfit calculation given from the getTotalProfit function
     */
    public void updateCurrentBalance(Double totalProfit){
        text_balance.setText("Portfolio Value " + "\n" + "$"+ getTotalPortfolioValue());
        text_balance.setTextSize(20);
        text_balance.setGravity(Gravity.CENTER);
        text_balance.setVisibility(View.VISIBLE);
    }
    /**
     * Used to set the profit under the statistics of the algo
     * @param algorithmName Name of the algorithm to get the profit for
     * @return profit of algorithm
     */
    public Double getAlgorithmProfit(String algorithmName) {
        ArrayList<Double> values = createListOfAlgorithmValues(algorithmName);
        Double finalValue = values.get(values.size() - 1);
        Double startingValue = getAlgorithmStartingValue(algorithmName);
        Double profit = finalValue - startingValue;
        return profit;
    }
    /**
     *
     * @param algorithmName Name of the algorithm
     * @return initial amount invested into the algorithm
     */
    public Double getAlgorithmStartingValue(String algorithmName) {
        return ((Integer) Algorithms.get(algorithmName).get(2)).doubleValue();
    }
}