package com.example.testmichelle.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testmichelle.R;
import androidx.fragment.app.Fragment;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;

public class DisplayBackTestingResults extends Fragment {

    public static TextView tvResults;
    public static TradingRecord tradingRecord;

    public DisplayBackTestingResults(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backtesingresults, container, false);
        tvResults = (TextView) view.findViewById(R.id.tvResult);
        Integer tradeCount = tradingRecord.getTradeCount();
        String tradeCountString = tradeCount.toString();
        tvResults.setText(tradeCountString);

        tradingRecord.getTrades();


        // Getting the number of profitable trades
//        AnalysisCriterion profitTradesRatio = new AverageProfitableTradesCriterion();
//        double profit_trades_ratio = profitTradesRatio.calculate(series, tradingRecord);
//        // Getting the average profit
//        AnalysisCriterion averageProfit = new AverageProfitCriterion();
//        double avgProfit = averageProfit.calculate(series, tradingRecord);
//        // Getting the total profit
//        AnalysisCriterion totalProfit = new TotalProfitCriterion();
//        double totProfit = totalProfit.calculate(series,tradingRecord);
//
//        // Getting the reward-risk ratio
//        AnalysisCriterion rewardRiskRatio = new RewardRiskRatioCriterion();
//        double reward_risk_ratio= rewardRiskRatio.calculate(series, tradingRecord);
//
//        // Getting the maximum drawdown ratio
//        AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();
////        System.out.println(maxDrawdown.calculate(series,tradingRecord));
//
//        // Total profit of our strategy
//        // vs total profit of a buy-and-hold strategy
//        AnalysisCriterion vsBuyAndHold = new VersusBuyAndHoldCriterion(new TotalProfitCriterion());
//        double buy_hold = vsBuyAndHold.calculate(series, tradingRecord);



        return view;
    }

    public void updateTextView(String message, TradingRecord record){
        tradingRecord = record;
    }
}
