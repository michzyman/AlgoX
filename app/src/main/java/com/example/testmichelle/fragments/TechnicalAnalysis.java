package com.example.testmichelle.fragments;

import static com.android.volley.toolbox.Volley.newRequestQueue;
import static com.example.testmichelle.fragments.callYahooFinance.requestChart;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TimeSeriesManager;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.IsFallingRule;
import org.ta4j.core.trading.rules.IsRisingRule;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

public class TechnicalAnalysis {

    public static TimeSeries series;

    static int num_trades;
    static double close_price;
    static double totProfit;
    static boolean programSet = false;


    /**
     * Calls the Yahoo Finance API to send data to proper functions
     * @param ticker
     * @param range
     * @param act
     * @return
     */
    public static double[][] callChart(String ticker, String range, Context act) {
        System.out.println("CALLING CALLCHART");
        System.out.println("PARAMETERS: " + ticker + " " + range);
        RequestQueue requestQueue = newRequestQueue(act);

        double[][] temp = requestChart(ticker, range, "1d", act, requestQueue);
        Log.d("log", temp.toString());
        return temp;
    }
    //

    /**
     * Determines if a string is able to be parsed as a Double
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


    /**
     * Creates the strategy and runs the rule given parameters
     * @param buying
     * @param selling
     * @return
     */
    public static TradingRecord triggerTa4j(Rule buying, Rule selling){
        try {
            Num firstClosePrice = series.getBar(0).getClosePrice();
            returnClosePrice(firstClosePrice);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Strategy strategy = createStrategy(buying, selling);

        // Running our juicy trading strategy...
        TradingRecord tradingRecord = runStrategy(strategy);
        programSet = true;
        return tradingRecord;




    }

    private void returnTradeCount(TradingRecord tradingRecord) {
        num_trades = tradingRecord.getTradeCount();
    }

    private static void returnClosePrice(Num firstClosePrice) {
        close_price =  firstClosePrice.doubleValue();
    }

    // LOAD DATA

    /**
     * Loads data into the class given information on the data
     * @param ticker
     * @param context
     * @param data
     */
    public static void loadData(String ticker, Context context, double[][] data) {
        // Initiate the android three ten library to get the context for zoned date time
        AndroidThreeTen.init(context);

        // create a new series using the base time series builder
        series = new BaseTimeSeries.SeriesBuilder().withName(ticker).build();

        // set the start time to now minus amount of days
        ZonedDateTime endTime = ZonedDateTime.now().minusDays(data.length);

        for (int i = 0; i < data.length; i++) {
            series.addBar(endTime.plusDays(i), data[i][0], data[i][1], data[i][2], data[i][3]);
        }

    }

    /**
     * Loads data into the class given information on the data
     * @param ticker
     * @param context
     * @param data
     * @param startDate
     * @param endDate
     */
    public static void loadData(String ticker, Context context, double[][] data, ZonedDateTime startDate, ZonedDateTime endDate) {
        try {
            System.out.println("loading data");
            // Initiate the android three ten library to get the context for zoned date time
            AndroidThreeTen.init(context);

            // create a new series using the base time series builder
            series = new BaseTimeSeries.SeriesBuilder().withName(ticker).build();
            int numDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
            System.out.println("startDate=" + startDate + " endDate=" + endDate + " numDays=" + numDays);

            int counter = 0;
            for (int i = data.length - numDays; i < data.length; i++) {
                series.addBar(startDate.plusDays(counter), data[i][0], data[i][1], data[i][2], data[i][3]);
                counter++;
            }
            Log.i("TA4J", "making the series");
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
    }

    // RUN A STRATEGY

    public static TradingRecord runStrategy(Strategy strategy){
        TimeSeriesManager manager = new TimeSeriesManager(series);
        TradingRecord tradingRecord = manager.run(strategy);
        return tradingRecord;
    }

    // CREATE A STRATEGY

    public static Strategy createStrategy(Rule buyingRule, Rule sellingRule) {
        Strategy strategy = new BaseStrategy(buyingRule, sellingRule);
        return strategy;
    }


    // RULES

    /**
     * triggers when price goes below input
     */
    public static Rule triggerBelow(double Price) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        Rule rule = new CrossedDownIndicatorRule(closePrice, Price);
        return rule;
    }

    /**
     * triggers when price goes above input
     */
    public static Rule triggerAbove(double Price) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        Rule rule = new CrossedUpIndicatorRule(closePrice, Price);
        return rule;
    }

    /**
     * triggers when SMADuration1 goes above SMADuration2
     */
    public static Rule SMARule(int SMADuration1, int SMADuration2) {

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator SMA1 = new SMAIndicator(closePrice, SMADuration1);
        SMAIndicator SMA2 = new SMAIndicator(closePrice, SMADuration2);
        Rule rule = new CrossedUpIndicatorRule(SMA1, SMA2);
        return rule;
    }

    /**
     * triggers when EMADuration1 goes above EMADuration2
     */
    public static Rule EMARule(int EMADuration1, int EMADuration2) {

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        EMAIndicator EMA1 = new EMAIndicator(closePrice, EMADuration1);
        EMAIndicator EMA2 = new EMAIndicator(closePrice, EMADuration2);
        Rule rule = new CrossedUpIndicatorRule(EMA1, EMA2);
        return rule;
    }

    /**
     * triggers when the closingPrice decreases minStrength percentage of the time
     * in the timeframe barCount
     */
    public static Rule fallingRule(int barCount, double minStrength) {

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        Rule rule = new IsFallingRule(closePrice, barCount, minStrength);
        return rule;
    }

    /**
     * triggers when the closingPrice increases minStrength percentage of the time
     * in the timeframe barCount
     */
    public static Rule risingRule(int barCount, double minStrength) {

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        Rule rule = new IsRisingRule(closePrice, barCount, minStrength);
        return rule;
    }

}
