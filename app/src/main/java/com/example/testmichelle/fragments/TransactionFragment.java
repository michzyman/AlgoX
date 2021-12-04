package com.example.testmichelle.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testmichelle.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.Hashtable;
public class TransactionFragment extends Fragment {

    EditText edt_Stock_Name;
    Button btn_search;
    TextView tv_Stock_results;
    GraphView stock_graph;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        edt_Stock_Name = (EditText) view.findViewById(R.id.edt_Stock_Name);
        btn_search = (Button) view.findViewById(R.id.btn_search);
        tv_Stock_results = (TextView) view.findViewById(R.id.tv_Stock_results);
        stock_graph = (GraphView) view.findViewById(R.id.stock_graph);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticker = edt_Stock_Name.getText().toString();
                search(ticker);
            }
        });
        return view;
    }

    public void search(String ticker) {
        if (!ticker.equals("")) {
            YahooFinance.requestSearchFragmentSpark(ticker, "5d", "1d", getContext(), this);
        }
    }

    public void displayData(String ticker, double[] stockPrices){
        stock_graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < stockPrices.length; i++){
            DataPoint currentDataPoint = new DataPoint(i, stockPrices[i]);
            series.appendData(currentDataPoint, true, stockPrices.length);
        }
        stock_graph.addSeries(series);
        stock_graph.setTitle("Growth of " + ticker + "'s stock last week");
    }

    public void setResults(String ticker, double[] stockPrices){
        double currentStockPrice = stockPrices[stockPrices.length - 1];
        tv_Stock_results.setText(ticker + "'s current price: " + Double.toString(currentStockPrice));
    }
}