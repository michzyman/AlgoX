package com.example.testmichelle.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.testmichelle.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Hashtable;

public class TransactionFragment extends Fragment {
    
    GraphView stock_graph;
    Button btnSearch;
    EditText edtStock;
    TextView txtEsg;
    TextView txtKeyStatistics;
    TextView txtFinancialData1;
    TextView txtFinancialData2;
    Spinner stock_period_spinner;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        stock_graph = (GraphView) view.findViewById(R.id.stock_graph);
        edtStock = (EditText) view.findViewById(R.id.edtStock);
        txtEsg = (TextView)view.findViewById(R.id.txtEsg);
        txtKeyStatistics = (TextView) view.findViewById(R.id.txtKeyStatistics);
        txtFinancialData1 = (TextView) view.findViewById(R.id.txtFinancialData1);
        txtFinancialData2 = (TextView) view.findViewById(R.id.txtFinancialData2);
        btnSearch = (Button) view.findViewById(R.id.btn_search);

        stock_period_spinner = (Spinner) view.findViewById(R.id.SpinnerOfAlgorithms);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.stockPeriods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stock_period_spinner.setAdapter(adapter);

        btnSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                search(v);
            }
        });
        txtEsg.setText(getString(R.string.esgData, "-", "-", "-", "-", "-", "-"));
        txtKeyStatistics.setText(getString(R.string.keyStatistics, "-", "-", "-", "-", "-", "-"));
        txtFinancialData1.setText(getString(R.string.financialData1, "-", "-", "-"));
        txtFinancialData2.setText(getString(R.string.financialData2, "-", "-", "-"));
        return view;
    }

    /*
    This method searches for the stock prices of the stock that is inputted
     */
    public void search(View view) {
        // Access the stock name and period we need
        String ticker = edtStock.getText().toString();
        String currentPeriod = stock_period_spinner.getSelectedItem().toString();

        // with the given period, we determine what is the range and interval for the yahoo finance API call
        String range = "";
        String interval = "";
        if(currentPeriod.equals("day")){
            range = "1d";
            interval = "5m";
        } else if (currentPeriod.equals("week")){
            range = "5d";
            interval = "15m";
        } else if (currentPeriod.equals("month")){
            range = "1mo";
            interval = "1d";
        } else if (currentPeriod.equals("year")){
            range = "1y";
            interval = "1wk";
        }
        // Make the yahoo finance API calls
        if (!ticker.equals("")) {
            YahooFinance.requestSummary(ticker, getContext(), this);
            YahooFinance.requestSearchFragmentSpark(ticker, range, interval, getContext(), this);
        }
    }

    /*
    The function graphs the stock prices
    This function is called when yahoo finance api is done making the call
     */
    public void graph(String ticker, double[] stockPrices, String range){
        // determine the period that will be outputted on the graph
        String period = "day";
        if (range.equals("5d")){
            period = "week";
        } else if (range.equals("1mo")){
            period = "month";
        } else if (range.equals("1y")){
            period = "year";
        }
        // make sure to remove all data that is already in the graph
        stock_graph.removeAllSeries();
        // Overwrote the label formal
        stock_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX){
                    return "";
                } else {
                    return value + "$";
                }
            }
        });
        // created a series to add the stock price
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < stockPrices.length; i++){
            DataPoint currentDataPoint = new DataPoint(i, stockPrices[i]);
            series.appendData(currentDataPoint, true, stockPrices.length);
        }
        // some styling
        series.setColor(getResources().getColor(R.color.green));
        series.setThickness(10);
        // add series to graph to display data
        stock_graph.addSeries(series);
        // set new title of the graph
        stock_graph.setTitle("Growth of " + ticker + "'s stock over the last " + period);
    }

    public void displayData(Hashtable<String, String> data){
        String[] keysToCheck = {"esgRaw", "esgPercentile", "esgPerformance", "environmentScore", "socialScore", "governanceScore",
                "marketCap", "forwardPE", "profitMargins", "priceToBook", "trailingEPS", "pegRatio", "currentPrice", "recommendationKey", "numberOfAnalystOpinions",
                "ebitda", "earningsGrowth", "revenueGrowth"};
        for (String key : keysToCheck){
            if (data.get(key) == null){
                data.put(key, "-");
            }
        }
        txtEsg.setText(getString(R.string.esgData, data.get("esgRaw"), data.get("esgPercentile"), data.get("esgPerformance"),
                data.get("environmentScore"), data.get("socialScore"), data.get("governanceScore")));
        txtKeyStatistics.setText(getString(R.string.keyStatistics, data.get("marketCap"), data.get("forwardPE"), data.get("profitMargins"),
                data.get("priceToBook"), data.get("trailingEPS"), data.get("pegRatio")));
        txtFinancialData1.setText(getString(R.string.financialData1, data.get("currentPrice"), data.get("recommendationKey"), data.get("numberOfAnalystOpinions")));
        txtFinancialData2.setText(getString(R.string.financialData2, data.get("ebitda"), data.get("earningsGrowth"), data.get("revenueGrowth")));
    }
}