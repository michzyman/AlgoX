package com.example.testmichelle.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.testmichelle.R;
import com.example.testmichelle.fragments.AccountFragment;
import com.example.testmichelle.fragments.DisplayBackTestingResults;
import com.example.testmichelle.fragments.HistoryFragment;
import com.example.testmichelle.fragments.HomeFragment;
import com.example.testmichelle.fragments.MoreInfoFragment;
import com.example.testmichelle.fragments.TechnicalAnalysis;
import com.example.testmichelle.fragments.TransactionFragment;
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

import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicActivity extends AppCompatActivity implements FragmentListener {
    FirebaseUser firebaseUser;
    HashMap<String, ArrayList<Object>> algorithms = new HashMap<String, ArrayList<Object>>();


    private DisplayBackTestingResults backTestingResults;
    private MoreInfoFragment moreInfoFragment;
    private backTestingFragment backTestingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        HomeFragment homeFragment = new HomeFragment();
        TransactionFragment transactionFragment = new TransactionFragment();
        HistoryFragment historyFragment = new HistoryFragment();
        AccountFragment accountFragment = new AccountFragment();
        backTestingFragment = new backTestingFragment();
        backTestingResults = new DisplayBackTestingResults();
        moreInfoFragment = new MoreInfoFragment();
        makeCurrentFragment(homeFragment);

        getAlgorithmsFromDatabase();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
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
                        makeCurrentFragment(accountFragment);
                        break;
                }
                return true;
            }
        });



    }
    //Changes the fragment that corresponds to the button that is clicked
    private void makeCurrentFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper, fragment).commit();
    }

    public void passDataToBackTestingResults(TradingRecord tradingRecord, Rule Buying_rule, Rule Selling_Rule, TimeSeries series, String p1, String p2, String p3, String p4,String ticker, String buyingRuleName, String sellingRuleName){
        makeCurrentFragment(backTestingResults);
        backTestingResults.collectData(tradingRecord, Buying_rule,Selling_Rule, series, p1,p2,p3,p4,ticker,buyingRuleName, sellingRuleName);
        backTestingResults.setResultsData();

    }

    public void goToMoreInfoFragment(){
        makeCurrentFragment(moreInfoFragment);
    }

    public void goToBackTestingFragment(){
        makeCurrentFragment(backTestingFragment);
    }

    public void getAlgorithmsFromDatabase(){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
            databaseReference.child(firebaseUser.getUid()).child("Algorithm").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());
                    int i = 0;
                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                        i+=1;
                        Algorithm algorithm = dataSnap.getValue(Algorithm.class);
                        ArrayList<Object> val = new ArrayList<Object>();
                        val.add(algorithm.buyingrule);
                        val.add(algorithm.sellingrule);
                        val.add(algorithm.currentbalance);
                        val.add(algorithm.stockname);
                        val.add(algorithm.status);
                        algorithms.put(Integer.toString(i),val); // KEY --> NAME OF ALG
                    }
                    Log.e("ALGO","these are my algorithms: " + algorithms.keySet()); // ACTUAL FIELD HERE SHOULD BE NAME
                    Log.e("ALGO","these are the values: "+ algorithms.values());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void updateAlgorithms(){
        for (Map.Entry<String, ArrayList<Object>> entry : algorithms.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());

            ArrayList<String> buyingRuleList = (ArrayList<String>) entry.getValue().get(0);
            ArrayList<String> sellingRuleList = (ArrayList<String>) entry.getValue().get(1);
            String currentBalance = (String) entry.getValue().get(2);
            String ticker = (String) entry.getValue().get(3);
            boolean isRunning = (boolean) entry.getValue().get(4);

            String buyingRuleType = buyingRuleList.get(2);
            String sellingRuleType = sellingRuleList.get(2);

            String par1 = buyingRuleList.get(0);
            String par2 = buyingRuleList.get(1);

            String par3 = sellingRuleList.get(0);
            String par4 = sellingRuleList.get(1);


            Rule buying_rule;
            Rule selling_rule;
            double[][] data = new double[0][];
            TechnicalAnalysis.loadData(ticker, this, data);

            switch(buyingRuleType){
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
                    buying_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par1), Integer.parseInt(par2));
                    break;
                case "Falling":
                    buying_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par1), Integer.parseInt(par2));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + buyingRuleType);
            }
            switch(sellingRuleType){
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
                    selling_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par3), Integer.parseInt(par4));
                    break;
                case "Falling":
                    selling_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par3), Integer.parseInt(par4));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sellingRuleType);
            }

            TradingRecord tradingRecord = TechnicalAnalysis.triggerTa4j(buying_rule, selling_rule);

        }

    }
}

