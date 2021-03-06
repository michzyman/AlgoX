/*
  The MIT License (MIT)

  Copyright (c) 2014-2017 Marc de Verdelhan, Ta4j Organization & respective authors (see AUTHORS)

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.analysis.criteria;

import org.ta4j.core.TimeSeries;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Total profit criterion.
 * <p></p>
 * The total profit of the provided {@link Trade trade(s)} over the provided {@link TimeSeries series}.
 */
public class TotalProfitCriterion extends AbstractAnalysisCriterion {

    @Override
    public double calculate(TimeSeries series, TradingRecord tradingRecord) {
        double value = 1d;
        for (Trade trade : tradingRecord.getTrades()) {
            value *= calculateProfit(series, trade);
        }
        return value;
    }

    public double calculate(TimeSeries series, TradingRecord tradingRecord, double d) {
        double value = d;
        for (Trade trade : tradingRecord.getTrades()) {
            value *= calculateProfit(series, trade);
        }
        return value;
    }

    @Override
    public double calculate(TimeSeries series, Trade trade) {
        return calculateProfit(series, trade);
    }

    @Override
    public boolean betterThan(double criterionValue1, double criterionValue2) {
        return criterionValue1 > criterionValue2;
    }

    /**
     * Calculates the profit of a trade (Buy and sell).
     * @param series a time series
     * @param trade a trade
     * @return the profit of the trade
     */
    private double calculateProfit(TimeSeries series, Trade trade) {
        Num profit = series.numOf(1);
        if (trade.isClosed()) {
            // use price of entry/exit order, if NaN use close price of underlying time series
            Num exitClosePrice = trade.getExit().getPrice().isNaN() ?
                    series.getBar(trade.getExit().getIndex()).getClosePrice() : trade.getExit().getPrice();
            Num entryClosePrice = trade.getEntry().getPrice().isNaN() ?
                    series.getBar(trade.getEntry().getIndex()).getClosePrice() : trade.getEntry().getPrice();


            if (trade.getEntry().isBuy()) {
                profit = exitClosePrice.dividedBy(entryClosePrice);
            } else {
                profit = entryClosePrice.dividedBy(exitClosePrice);
            }
        }
        return profit.doubleValue();
    }
}
