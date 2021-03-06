package com.example.testmichelle.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.testmichelle.R;
import com.example.testmichelle.fragments.AccountFragment;
import com.example.testmichelle.fragments.DisplayBackTestingResults;
import com.example.testmichelle.fragments.HistoryFragment;
import com.example.testmichelle.fragments.HomeFragment;
import com.example.testmichelle.fragments.TechnicalAnalysis;
import com.example.testmichelle.fragments.TransactionFragment;
import com.example.testmichelle.fragments.YahooFinance;
import com.example.testmichelle.fragments.backTestingFragment;
import com.example.testmichelle.model.Algorithm;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;
import org.threeten.bp.ZonedDateTime;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BasicActivity extends AppCompatActivity implements FragmentListener {
    FirebaseUser firebaseUser;

    // Key: Name of Algorithm
    // Value: [ArrayList buyingrule, ArrayList sellingrule, Integer initialAmount, String stockname, boolean status, String start_date, String end_date]
    // ArrayList buyingrule / sellingrule: [par1, par2, type]
    public static HashMap<String, ArrayList<Object>> algorithms = new HashMap<String, ArrayList<Object>>();

    // Key: Name of Algorithm
    // Value: [TradingRecord record, TimeSeries series, String Ticker, ZonedDateTime start, ZonedDateTime end]
    public static HashMap<String, ArrayList<Object>> algorithmsRan = new HashMap<String, ArrayList<Object>>();

    //Initializes Fragments
    private DisplayBackTestingResults backTestingResults;
    private backTestingFragment backTestingFragment;
    private HomeFragment homeFragment;
    private TransactionFragment transactionFragment;
    private HistoryFragment historyFragment;
    private AccountFragment accountFragment;
    private LoadingScreenFragment loadingFragment;
    boolean done = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the hashmaps that will store the algorithms from DB to empty every time basic activity is created
        algorithms = new HashMap<String,ArrayList<Object>>();
        algorithmsRan = new HashMap<String,ArrayList<Object>>();

        setContentView(R.layout.activity_basic);

/*-------------------FRAGMENTS - BUTTON NAV ----------------------------*/
        homeFragment = new HomeFragment();
        transactionFragment = new TransactionFragment();
        historyFragment = new HistoryFragment();
        accountFragment = new AccountFragment();
        backTestingFragment = new backTestingFragment();
        backTestingResults = new DisplayBackTestingResults();
        loadingFragment = new LoadingScreenFragment();
        // When the user logs in, first fragment that appears is the loading fragment
        makeCurrentFragment(loadingFragment);
        YahooFinance.initRequestQueue(this);
        /* get current user info from database and then extract all of the algorithms stored
        * for that user to fill the algorithms hashmap  */
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).child("Algorithms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnap : snapshot.getChildren()) {
                    Algorithm algo = dataSnap.getValue(Algorithm.class);
                    Log.i("DATABASE", "Adding "+algo.algoname);
                    ArrayList<Object> vals = new ArrayList<>();
                    vals.add(algo.buyingrule);
                    vals.add(algo.sellingrule);
                    vals.add(algo.initialamount);
                    vals.add(algo.stockname);
                    vals.add(algo.status);
                    vals.add(algo.start_date);
                    vals.add(algo.end_date);
                    i++;
                    algorithms.put(algo.algoname, vals);
                }
                Log.i("DATABASE", "got em all");
                Log.i("DATABASE", algorithms.toString());

                /* ONCE ALL ALGORITHMS ARE EXTRACTED & THE HASHMAP IS FILLED,
                *  WE CALL THE FINANCE API IN ORDER TO START UPDATING THE ALGORITHMS
                */
                for (Map.Entry<String, ArrayList<Object>> entry : algorithms.entrySet()) {
                    System.out.println("yay, next entry! it's " + entry.toString());
                    callAPItoUpdateAlgorithm(entry);
                }
//                    done = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /* TIMER FOR LOADING THE HOME FRAGMENT UPON LOGIN
        *  (gives time for the algorithms to run and update before home page is shown) */
        new CountDownTimer(8000, 1000) {
            public void onFinish() {
                homeFragment = new HomeFragment();
                makeCurrentFragment(homeFragment);
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();

        /* BOTTOM NAVIGATION LISTENER */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_home:
                        makeCurrentFragment(homeFragment);
                        break;
                    case R.id.btn_transaction:
                        makeCurrentFragment(transactionFragment);
                        break;
                    case R.id.btn_algo:
                        makeCurrentFragment(backTestingFragment);
                        break;
                    case R.id.btn_history:
                        makeCurrentFragment(historyFragment);
                        break;
                    case R.id.btn_account:
                        Intent intent_sign_in = new Intent(BasicActivity.this, LogInActivity.class);
                        startActivity(intent_sign_in);
                        break;
                }
                return true;
            }
        });
    }

    //Changes the fragment that corresponds to the button that is clicked
    private void makeCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper, fragment).commit();
    }

    /*
    This method of the fragment listener is used to passed data from the backTesting fragment to the backTestingResults Fragment
     */
    public void passDataToBackTestingResults(TradingRecord tradingRecord, Rule Buying_rule, Rule Selling_Rule, TimeSeries series, String p1, String p2, String p3, String p4, String ticker, String buyingRuleName, String sellingRuleName) {
        makeCurrentFragment(backTestingResults);
        backTestingResults.collectData(tradingRecord, Buying_rule, Selling_Rule, series, p1, p2, p3, p4, ticker, buyingRuleName, sellingRuleName);
        backTestingResults.setResultsData();
    }

    /**
     *     Method for the backTestingResults fragment to go back to the backtesting fragment
     */
    public void goToBackTestingFragment() {
        makeCurrentFragment(backTestingFragment);
    }

    /*
    Method to go the history fragment
     */
    public void goToHistory() {
        makeCurrentFragment(historyFragment);
    }

    /**
     *     This function was used to enter test data for algorithms in order to see how
     *     algorithms that have been running for a longer period of time would be graphed/stored/presented
     */
    public void getAlgorithmsFromDatabaseTest() {

        AndroidThreeTen.init(getApplicationContext());
        ArrayList<String> buyingRule = new ArrayList<>(Arrays.asList("10", "7", "SMA"));
        ArrayList<String> sellingRule = new ArrayList<>(Arrays.asList("5", "10", "SMA"));
        Integer initialAmount = 1500;
        String stockName = "AAPL";
        boolean status = true;
        String startDate = ZonedDateTime.now().minusDays(100).toString();
        String endDate = "null";

        // FIX: status was boolean ||| currentBalance was Integer

        ArrayList<Object> val = new ArrayList<Object>();
        val.add(buyingRule);
        val.add(sellingRule);
        val.add(initialAmount);
        val.add(stockName);
        val.add(status);
        val.add(startDate);
        val.add(endDate);
        algorithms.put("Algo1",val); // KEY --> NAME OF ALG
        algorithms.put("Algo2", val);

    }

    /**
     * Calls the YahooFinance API in order to get callback data that we can pass to UpdateAlgorithms
     * @param entry
     */
    public void callAPItoUpdateAlgorithm(Map.Entry<String, ArrayList<Object>> entry) {
        System.out.println("calling finance api to get this chart!");
        String ticker = (String) entry.getValue().get(3);
        Context context = this;
        BasicActivity thisObj = this;
        YahooFinance.basicActivityRequestChart(ticker, context, thisObj, entry);
    }

    /**
     * Takes in hashmap entry and data (from api call), and updates the corresponding entry
     * in the algorithmsRun hashmap
     * @param entry
     */
    public void updateAlgorithms(Map.Entry<String, ArrayList<Object>> entry, double [][] data){
        AndroidThreeTen.init(getApplicationContext());

        System.out.println(entry.getKey() + "/" + entry.getValue());
        System.out.println(Arrays.deepToString(data));

        ArrayList<String> buyingRuleList = (ArrayList<String>) entry.getValue().get(0);
        System.out.println("buying rule list: " + buyingRuleList.toString());

        ArrayList<String> sellingRuleList = (ArrayList<String>) entry.getValue().get(1);
        System.out.println("selling rule list: " + sellingRuleList.toString());

        Integer initialAmount = (Integer) entry.getValue().get(2);
        System.out.println("initial amount: " + initialAmount.toString());

        String ticker = (String) entry.getValue().get(3);
        System.out.println("ticker: " + ticker);

        boolean isRunning = (boolean) entry.getValue().get(4);
        System.out.println("is running: " + isRunning);

        String buyingRuleType = buyingRuleList.get(2);
        String sellingRuleType = sellingRuleList.get(2);

        String par1 = buyingRuleList.get(0);
        String par2 = buyingRuleList.get(1);

        String par3 = sellingRuleList.get(0);
        String par4 = sellingRuleList.get(1);

        Rule buying_rule;
        Rule selling_rule;


        ZonedDateTime startDate;
        ZonedDateTime endDate;
        try {
            String start = (String) entry.getValue().get(5);
            startDate = ZonedDateTime.parse(start);
            System.out.println("got the start date: "+ startDate.toString());

            if(isRunning) {
                endDate = ZonedDateTime.now();
                System.out.println("got the end date: "+ endDate.toString());
            }
            else{
                String end = (String) entry.getValue().get(6);
                System.out.println(end);
                endDate = ZonedDateTime.parse(end);
                System.out.println("got the end date: "+ endDate.toString());
            }

            System.out.println("ok got all the data for " + entry.getKey());
        } catch (Exception e) {
            startDate = null;
            endDate = null;
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }

        ZonedDateTime fakeStart = ZonedDateTime.now();
        ZonedDateTime fakeEnd = ZonedDateTime.now();


        try{
            System.out.println("trying to load data from basic activity");

            // TechnicalAnalysis.loadData(ticker, getApplicationContext(), data, ZonedDateTime.now().minusMonths(1), ZonedDateTime.now());
            TechnicalAnalysis.loadData(ticker, getApplicationContext(), data, startDate, endDate);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            switch (buyingRuleType) {
                case "Price Above":
                    buying_rule = TechnicalAnalysis.triggerAbove(Double.parseDouble(par1));
                    break;
                case "Price Below":
                    buying_rule = TechnicalAnalysis.triggerBelow(Double.parseDouble(par1));
                    break;
                case "SMA":
                    buying_rule = TechnicalAnalysis.SMARule(Integer.parseInt(par1), Integer.parseInt(par2));
                    break;
                case "EMA":
                    buying_rule = TechnicalAnalysis.EMARule(Integer.parseInt(par1), Integer.parseInt(par2));
                    break;
                case "Rising":
                    buying_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par1), Double.parseDouble(par2));
                    break;
                case "Falling":
                    buying_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par1), Double.parseDouble(par2));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + buyingRuleType);
            }
            switch (sellingRuleType) {
                case "Price Above":
                    selling_rule = TechnicalAnalysis.triggerAbove(Double.parseDouble(par3));
                    break;
                case "Price Below":
                    selling_rule = TechnicalAnalysis.triggerBelow(Double.parseDouble(par3));
                    break;
                case "SMA":
                    selling_rule = TechnicalAnalysis.SMARule(Integer.parseInt(par3), Integer.parseInt(par4));
                    break;
                case "EMA":
                    selling_rule = TechnicalAnalysis.EMARule(Integer.parseInt(par3), Integer.parseInt(par4));
                    break;
                case "Rising":
                    selling_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par3), Double.parseDouble(par4));
                    break;
                case "Falling":
                    selling_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par3), Double.parseDouble(par4));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sellingRuleType);
            }

            TradingRecord tradingRecord = TechnicalAnalysis.triggerTa4j(buying_rule, selling_rule);

            ArrayList<Object> ran = new ArrayList<>();
            ran.add(tradingRecord);
            ran.add(TechnicalAnalysis.series);
            ran.add(ticker);

            algorithmsRan.put(entry.getKey(), ran);
            System.out.println("ALGORITHMS RAN! " + entry.getKey());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * pass the algorithms and algorithmsRan hashmaps from the basic activity to the home fragment
     */
    public void passDataToHomeFragment() {
        homeFragment.setAlgorithms(algorithms, algorithmsRan);
    }
}

