package com.example.testmichelle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testmichelle.R;
import com.google.firebase.database.DatabaseReference;

import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.threeten.bp.ZonedDateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        return view;
    }

    /**
     *
     * @param algoName
     * @param par1
     * @param par2
     * @param par3
     * @param par4
     * @param ticker
     * @param amount
     * @param buyingRuleType
     * @param sellingRuleType
     * @return 2-D array. Each row is [Algoname, Buy/Sell, TransactionType, Amount, Date]
     */
    public ArrayList[][] createDataFromAlgorithm(String algoName, String par1, String par2, String par3, String par4, String ticker, String amount, String buyingRuleType, String sellingRuleType, double[][] data, TimeSeries series) {

        TechnicalAnalysis.loadData(ticker, getContext(), data);

        Rule buying_rule;
        switch (buyingRuleType) {
            case "Price Above":
                Double.parseDouble(par1);
                buying_rule = TechnicalAnalysis.triggerAbove(Double.parseDouble(par1));
                break;
            case "Price Below":
                Double.parseDouble(par1);
                buying_rule = TechnicalAnalysis.triggerBelow(Double.parseDouble(par1));
                break;
            case "SMA":
                Integer.parseInt(par1);
                Integer.parseInt(par2);
                buying_rule = TechnicalAnalysis.SMARule(Integer.parseInt(par1), Integer.parseInt(par2));
                break;
            case "EMA":
                Integer.parseInt(par1);
                Integer.parseInt(par2);
                buying_rule = TechnicalAnalysis.EMARule(Integer.parseInt(par1), Integer.parseInt(par2));
                break;
            case "Rising":
                Integer.parseInt(par1);
                Integer.parseInt(par2);
                buying_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par1), Double.parseDouble(par2));
                break;
            case "Falling":
                Integer.parseInt(par1);
                Integer.parseInt(par2);
                buying_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par1), Double.parseDouble(par2));
                break;
            default:
                buying_rule = null;
        }

        // Setting Selling Rule
        Rule selling_rule;

        switch (sellingRuleType) {
            case "Price Above":
                Double.parseDouble(par3);
                selling_rule = TechnicalAnalysis.triggerAbove(Double.parseDouble(par3));
                break;
            case "Price Below":
                Double.parseDouble(par3);
                selling_rule = TechnicalAnalysis.triggerBelow(Double.parseDouble(par3));
                break;
            case "SMA":
                Integer.parseInt(par3);
                Integer.parseInt(par4);
                selling_rule = TechnicalAnalysis.SMARule(Integer.parseInt(par3), Integer.parseInt(par4));
                break;
            case "EMA":
                Integer.parseInt(par3);
                Integer.parseInt(par4);
                selling_rule = TechnicalAnalysis.EMARule(Integer.parseInt(par3), Integer.parseInt(par4));
                break;
            case "Rising":
                Integer.parseInt(par3);
                Integer.parseInt(par4);
                selling_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par3), Double.parseDouble(par4));
                break;
            case "Falling":
                Integer.parseInt(par3);
                Integer.parseInt(par4);
                selling_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par3), Double.parseDouble(par4));
                break;
            default:
                selling_rule = null;
                break;
        }

        TradingRecord tradingRecord = TechnicalAnalysis.triggerTa4j(buying_rule, selling_rule);

        List<Trade> trades = tradingRecord.getTrades();

        ArrayList[][] finalArray = new ArrayList[trades.size()*2][6];

        for (int i = 0; i < trades.size();i++) {
            Trade trade = trades.get(i);
            [Algoname, Buy/Sell, Amount, Price, Date]

            // Append buy trade to list
            Order entryTrade = trade.getEntry();
            String algName = algoName;
            String type = "Buy";
            double amountTraded = entryTrade.getAmount().doubleValue();
            double priceValue = entryTrade.getPrice().doubleValue();
            int index = entryTrade.getIndex();
            ZonedDateTime date = series.getBar(index).getBeginTime();

            // add it to the finalArray
            append([algoname, ticker, type, amountTraded, priceValue, date]);

//            series.getSubSeries(); USE FOR ALGORITHMS WHICH ARENT STILL RUNNING

            // Append buy trade to list
            Order exitTrade = trade.getEntry();
            type = "Sell";
            amountTraded = exitTrade.getAmount().doubleValue();
            priceValue = exitTrade.getPrice().doubleValue();
            index = exitTrade.getIndex();
            date = series.getBar(index).getBeginTime();

            append([algoname, ticker, type, amountTraded, priceValue, date])

        }
        return finalArray;
}