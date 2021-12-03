package com.example.testmichelle.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.testmichelle.R;
import com.example.testmichelle.fragments.AccountFragment;
import com.example.testmichelle.fragments.AlgoFragment;
import com.example.testmichelle.fragments.DisplayBackTestingResults;
import com.example.testmichelle.fragments.HistoryFragment;
import com.example.testmichelle.fragments.HomeFragment;
import com.example.testmichelle.fragments.TransactionFragment;
import com.example.testmichelle.fragments.backTestingFragment;
import com.example.testmichelle.model.UserMoney;
import com.example.testmichelle.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;

public class BasicActivity extends AppCompatActivity implements FragmentListener {
    FirebaseUser firebaseUser;

    private DisplayBackTestingResults backTestingResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        HomeFragment homeFragment = new HomeFragment();
        TransactionFragment transactionFragment = new TransactionFragment();
        AlgoFragment algoFragment = new AlgoFragment();
        HistoryFragment historyFragment = new HistoryFragment();
        AccountFragment accountFragment = new AccountFragment();
        backTestingFragment backTestingFragment = new backTestingFragment();
        backTestingResults = new DisplayBackTestingResults();
        makeCurrentFragment(homeFragment);

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

    public void passDataToBackTestingResults(TradingRecord tradingRecord, Rule Buying_rule, Rule Selling_Rule, TimeSeries series){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper, backTestingResults).commit();
        backTestingResults.collectData(tradingRecord, Buying_rule,Selling_Rule, series);
        backTestingResults.setResultsData();
    }



}


