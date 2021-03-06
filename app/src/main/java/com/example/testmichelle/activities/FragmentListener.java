package com.example.testmichelle.activities;

import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;

/*
Fragment Listener interface to communicate between fragments
 */
public interface FragmentListener {
    public void passDataToBackTestingResults(TradingRecord tradingRecord, Rule Buying_rule, Rule Selling_Rule, TimeSeries series,String p1, String p2, String p3, String p4,String ticker, String buyingRuleName, String sellingRuleName);
    public void goToBackTestingFragment();
    public void passDataToHomeFragment();
    public void goToHistory();
}
