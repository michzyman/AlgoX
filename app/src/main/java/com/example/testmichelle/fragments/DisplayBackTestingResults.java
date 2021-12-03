package com.example.testmichelle.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testmichelle.R;
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

public class DisplayBackTestingResults extends Fragment {

    private TextView tvResults;
    public static TradingRecord tradingRecord;
    public static TimeSeries series;
    public static Rule buying_rule;
    public static Rule selling_rule;

    double buy_hold;
    double profit_trades_ratio;
    double avgProfit;
    double totProfit;
    double reward_risk_ratio;

    
    public DisplayBackTestingResults(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backtesingresults, container, false);
        tvResults = (TextView) view.findViewById(R.id.tvResults);
        Integer tradeCount = tradingRecord.getTradeCount();
        String tradeCountString = tradeCount.toString();


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

        tvResults.setText(Double.toString(buy_hold));
        Log.i("MyTag", Double.toString(buy_hold));
        return view;
    }

    public void collectData(TradingRecord record, Rule Buying_rule, Rule Selling_Rule, TimeSeries Series){
        tradingRecord = record;
        buying_rule = Buying_rule;
        selling_rule = Selling_Rule;
        series = Series;
    }

    public void setResultsData(){

    }
}
