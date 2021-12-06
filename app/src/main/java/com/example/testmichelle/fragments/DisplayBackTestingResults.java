package com.example.testmichelle.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmichelle.R;
import com.example.testmichelle.activities.FragmentListener;
import com.example.testmichelle.model.Algorithm;
import com.example.testmichelle.model.UserMoney;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.TemporalAccessor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DisplayBackTestingResults extends Fragment {
    private TextView tvResults;
    private TextView tvResults2;
    private TextView tvResults3;
    private TextView tvResults4;
    private TextView tv_algo;
    private Button btn_useAlg;
    private Button btn_newAlg;
    private EditText et_money;
    private EditText et_algoName;
    public static TradingRecord tradingRecord;
    public static TimeSeries series;
    public static Rule buying_rule;
    public static Rule selling_rule;
    public String buyingRuleName;
    public String sellingRuleName;

    double buy_hold;
    double profit_trades_ratio;
    double avgProfit;
    double totProfit;
    double reward_risk_ratio;

    String p1;
    String p2;
    String p3;
    String p4;
    String ticker;

    boolean algSet;
    int amountToInvest;

    private FragmentListener FL;

    public DisplayBackTestingResults(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FL = (FragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backtesingresults, container, false);
        tvResults = (TextView) view.findViewById(R.id.tvResults);
        tvResults2 = (TextView) view.findViewById(R.id.tvResults2);
        tvResults3 = (TextView) view.findViewById(R.id.tvResults3);
        tvResults4 = (TextView) view.findViewById(R.id.tvResults4);
        tv_algo = (TextView) view.findViewById(R.id.tv_algo);
        btn_useAlg = (Button) view.findViewById(R.id.btn_useAlg);
        btn_newAlg = (Button) view.findViewById(R.id.btn_newAlg);
        et_money = (EditText) view.findViewById(R.id.et_money);
//        et_money.setText("");
        et_algoName = (EditText) view.findViewById(R.id.et_algoName);

        Integer tradeCount = tradingRecord.getTradeCount();
        String tradeCountString = tradeCount.toString();
        String response = "In total, " + tradeCountString + " trades were made by your algorithm.\n"+
                "\nIf you would like to use this algorithm on this stock, enter the amount to invest below.";
        tv_algo.setText(response);

        btn_useAlg.setText("SET ALGORITHM!");
        algSet=false;

        btn_newAlg.setText("Go back");

        tradingRecord.getTrades();
        // Getting the number of profitable trades
        AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
        profit_trades_ratio = profitTradesRatio.calculate(series, tradingRecord);
        // Getting the average profit
        AnalysisCriterion averageProfit = new AverageProfitCriterion();
        avgProfit = averageProfit.calculate(series, tradingRecord);
        // Getting the total profit
        AnalysisCriterion totalProfit = new TotalProfitCriterion();
        totProfit = totalProfit.calculate(series,tradingRecord);

        // Getting the reward-risk ratio
        AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();
        reward_risk_ratio= rewardRiskRatio.calculate(series, tradingRecord);

        // Getting the maximum drawdown ratio
        AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();
//        System.out.println(maxDrawdown.calculate(series,tradingRecord));

        // Total profit of our strategy
        // vs total profit of a buy-and-hold strategy
        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
        buy_hold = vsBuyAndHold.calculate(series, tradingRecord);

        tvResults.setText(Double.toString(round(totProfit)));
        tvResults2.setText(Double.toString(round(reward_risk_ratio)));
        tvResults3.setText(Double.toString(round(buy_hold)));
        tvResults4.setText(Double.toString(round(profit_trades_ratio)));

        btn_useAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_money.getText().toString().equals("") && !et_algoName.getText().toString().equals("") && backTestingFragment.isNumeric(et_money.getText().toString())) {
                    if(!algSet) {
                        amountToInvest = Integer.parseInt(et_money.getText().toString());
//                        updateFreeCash();
                        algorithmToUse();
                        FL.goToHistory();
                    }
                    else{
                        Toast.makeText(getContext(),"Algorithm was already set",Toast.LENGTH_LONG).show();
                    }
                }
                else if (et_algoName.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Enter Algorithm Name", Toast.LENGTH_LONG).show();
                }
                else if (et_money.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Enter Amount to invest", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),"Enter parameters for your algo!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_newAlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FL.goToBackTestingFragment();
            }
        });
        return view;
    }

    public void collectData(TradingRecord record, Rule Buying_rule, Rule Selling_Rule, TimeSeries Series, String parameter1, String parameter2, String parameter3, String parameter4,String tickername, String buyingrulename, String sellingrulename){
        tradingRecord = record;
        buying_rule = Buying_rule;
        selling_rule = Selling_Rule;
        series = Series;
        p1=parameter1;
        p2=parameter2;
        p3=parameter3;
        p4=parameter4;
        ticker = tickername;
        buyingRuleName = buyingrulename;
        sellingRuleName = sellingrulename;
    }

    public void setResultsData(){

    }

    public Double round(double d){
        return (double)Math.round(d * 1000000d) / 1000000d;

    }
    @SuppressLint("NewApi")
    private void algorithmToUse() {
        boolean status = true;
        String stockname = ticker;
        Log.d("gettext.tostring", et_money.getText().toString());
        Log.d("parse(gettext.tostring)", String.valueOf(Integer.parseInt(et_money.getText().toString())));
        Integer initialamount = Integer.parseInt(et_money.getText().toString());
        String algoName = et_algoName.getText().toString();
        System.out.println("THE PARAMETERS ARE : " + p1 +" AND " + p2 + " AND "+buyingRuleName);
        String[] list = {p1,p2,buyingRuleName};
        String[] list2 = {p3,p4,sellingRuleName};
        ArrayList<String> buyingrule = new ArrayList<String>(Arrays.asList(list));
        ArrayList<String> sellingrule = new ArrayList<String>(Arrays.asList(list2));
        AndroidThreeTen.init(getContext());
        ZonedDateTime startZoned = ZonedDateTime.now();
        String start_date = startZoned.toString();
        String end_date = null;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Algorithm algorithm = new Algorithm(status, stockname, initialamount, buyingrule, sellingrule, start_date, end_date, algoName);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).child("Algorithms").push().setValue(algorithm);
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserMoney money = snapshot.getValue(UserMoney.class);
                Integer current_freecash = money.getFreecash();
                databaseReference.child(firebaseUser.getUid()).child("freecash").setValue(current_freecash-amountToInvest);
//                money.setFreecash(current_freecash-amountToInvest);
                System.out.println(money.freecash + " should be " + (current_freecash-amountToInvest));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        UserMoney money = new UserMoney();
//        Integer money1 = money.getFreecash();

//        databaseReference.child(firebaseUser.getUid()).child("freecash").push().setValue()
        algSet = true;
//        et_money.setText("");
//        et_algoName.setText("");
    }

    private void updateFreeCash(){
        UserMoney userMoney = new UserMoney();
        Integer userFreeCash = userMoney.getFreecash();
        System.out.println(userFreeCash);
        userMoney.setFreecash(userFreeCash-amountToInvest);
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
//        assert firebaseUser != null;
//        databaseReference.child(firebaseUser.getUid()).child("freecash").setValue(userFreeCash-amountToInvest);



    }

}
