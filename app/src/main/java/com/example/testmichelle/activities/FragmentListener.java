package com.example.testmichelle.activities;

import org.ta4j.core.Rule;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;

public interface FragmentListener {
    public void passDataToBackTestingResults(TradingRecord tradingRecord, Rule Buying_rule, Rule Selling_Rule, TimeSeries series,String p1, String p2, String p3, String p4,String ticker);
    public void goToMoreInfoFragment();
    public void goToBackTestingFragment();
}
