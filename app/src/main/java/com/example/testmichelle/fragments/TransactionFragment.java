package com.example.testmichelle.fragments;

import static java.util.Collections.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testmichelle.R;

import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnSearch;
    EditText edtStock;
    TextView txtEsg;
    TextView txtKeyStatistics;
    TextView txtFinancialData1;
    TextView txtFinancialData2;



    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        edtStock = (EditText) view.findViewById(R.id.edt_Stock);
        txtEsg = (TextView)view.findViewById(R.id.txtEsg);
        txtKeyStatistics = (TextView) view.findViewById(R.id.txtKeyStatistics);
        txtFinancialData1 = (TextView) view.findViewById(R.id.txtFinancialData1);
        txtFinancialData2 = (TextView) view.findViewById(R.id.txtFinancialData2);
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.d("onClickCalled", "yay");
                search(v);
            }
        });

        txtEsg.setText(getString(R.string.esgData, "-", "-", "-", "-", "-", "-"));
        txtKeyStatistics.setText(getString(R.string.keyStatistics, "-", "-", "-", "-", "-", "-"));
        txtFinancialData1.setText(getString(R.string.financialData1, "-", "-", "-"));
        txtFinancialData2.setText(getString(R.string.financialData2, "-", "-", "-"));
        return view;
    }

    public void search(View view) {
        Log.d("searchCalled", "yay");
        String ticker = edtStock.getText().toString();
        Log.d("tickerGotten", ticker);
        if (!ticker.equals("")) {
            YahooFinance.requestSummary(ticker, getContext(), this);
            YahooFinance.requestSearchFragmentSpark(ticker, "5d", "1d", getContext(), this);
        }
    }

    public void displayData(Hashtable<String, String> data){

        for (String key : data.keySet()){
            if (data.get(key).equals("null") || data.get(key).equals(null)){
                data.put(key, "-");
            }
        }

        txtEsg.setText(getString(R.string.esgData, data.get("esgRaw"), data.get("esgPercentile"), data.get("esgPerformance"),
                data.get("environmentScore"), data.get("socialScore"), data.get("governanceScore")));
        txtKeyStatistics.setText(getString(R.string.keyStatistics, data.get("marketCap"), data.get("forwardPE"), data.get("profitMargin"),
                data.get("priceBook"), data.get("trailingEPS"), data.get("pegRatio")));
        txtFinancialData1.setText(getString(R.string.financialData1, data.get("currentPrice"), data.get("recommendationKey"), data.get("numberOfAnalystOpinions")));
        txtFinancialData2.setText(getString(R.string.financialData2, data.get("ebitda"), data.get("earningsGrowth"), data.get("revenueGrowth")));

    }
}