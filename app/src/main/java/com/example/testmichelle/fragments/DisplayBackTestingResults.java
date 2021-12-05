package com.example.testmichelle.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.testmichelle.R;
import com.example.testmichelle.model.Algorithm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayBackTestingResults extends Fragment {

    private TextView tvResults;
    private TextView tvResults2;
    private TextView tvResults3;
    private TextView tvResults4;
    private TextView tv_algo;
    private Button btn_useAlg;
    private EditText et_money;
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

    
    public DisplayBackTestingResults(){

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
        et_money = (EditText) view.findViewById(R.id.et_money);
        et_money.setText("");

        Integer tradeCount = tradingRecord.getTradeCount();
        String tradeCountString = tradeCount.toString();
        String response = "In total, " + tradeCountString + " trades were made by your algorithm.\n"+
                "\nIf you would like to use this algorithm on this stock, enter the amount to invest below.";
        tv_algo.setText(response);

        btn_useAlg.setText("SET ALGORITHM!");

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
                algorithmToUse();
            }
        });
        return view;
    }

    public void collectData(TradingRecord record, Rule Buying_rule, Rule Selling_Rule, TimeSeries Series, String p1, String p2, String p3, String p4,String ticker, String buyingRuleName, String sellingRuleName){
        tradingRecord = record;
        buying_rule = Buying_rule;
        selling_rule = Selling_Rule;
        series = Series;
        p1=p1;
        p2=p2;
        p3=p3;
        p4=p4;
        ticker = ticker;
        buyingRuleName = buyingRuleName;
        sellingRuleName = sellingRuleName;
//        Log.i("MyTag","FIRST PAR: " + p1);
//        Log.i("MyTag","SECOND PAR: " + p2);
//        Log.i("MyTag","THIRD PAR: " + p3);
//        Log.i("MyTag","FOURTH PAR: " + p4);

    }

    public void setResultsData(){

    }

    public Double round(double d){
        return (double)Math.round(d * 1000000d) / 1000000d;

    }

    private void algorithmToUse() {
        boolean status = true;
        String stockname = ticker;
        Integer initialamount = Integer.parseInt(et_money.getText().toString());
        String[] list = {p1,p2,buyingRuleName};
        String[] list2 = {p3,p4,sellingRuleName};
        ArrayList<String> buyingrule = new ArrayList<String>(Arrays.asList(list));
        ArrayList<String> sellingrule = new ArrayList<String>(Arrays.asList(list2));
        String start_date = "12/05/2021";
        String end_date = "12/15/2021";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Algorithm algorithm = new Algorithm(status, stockname, initialamount, buyingrule, sellingrule, start_date, end_date);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).child("Algorithms").setValue(algorithm);
    }
}
