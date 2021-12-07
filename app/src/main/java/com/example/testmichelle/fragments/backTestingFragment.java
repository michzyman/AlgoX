package com.example.testmichelle.fragments;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.testmichelle.R;
import com.example.testmichelle.activities.AlgorithmPopUp;
import com.example.testmichelle.activities.FragmentListener;
import com.example.testmichelle.activities.MainActivity;
import com.example.testmichelle.model.Algorithm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;

import java.util.ArrayList;
import java.util.Arrays;

public class backTestingFragment extends Fragment {

    private TextView tv_select;
    private TextView tv_p1;
    private TextView tv_p2;
    private EditText et_p1;
    private EditText et_p2;
    private Button btn_set;
    private Button btnToMoreInfoPage;
    private boolean both_param;
    private String rule;
    private Rule buying_rule;

    String par1;
    String par2;

    private TextView tv_select2;
    private TextView tv_p3;
    private TextView tv_p4;
    private EditText et_p3;
    private EditText et_p4;
    private boolean both_param_sell;

    private String rule2;
    private Rule selling_rule;

    String par3;
    String par4;

    private TextView tv_etc;
    private TextView tv_p5;
    private TextView tv_p6;
    private EditText et_p5;
    String et_p6;
//    private EditText et_p6;
    String par5;
    String par6;

    public boolean buyingRuleSet = false;
    public boolean sellingRuleSet = false;
    public boolean backtestingParamsSet = false;
    public boolean error = false;

    private FragmentListener FL;


    public backTestingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FL = (FragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backtesting, container, false);  //separate me from return statement.
        tv_select = (TextView) view.findViewById(R.id.tv_select);      //need a chance to do this other stuff,
        tv_select.setText("Buying Rule");
        tv_p1 = (TextView) view.findViewById(R.id.tv_p1);    //before returning the inflated view.
        tv_p2 = (TextView) view.findViewById(R.id.tv_p2);
        et_p1 = (EditText) view.findViewById(R.id.et_p1);
        et_p2 = (EditText) view.findViewById(R.id.et_p2);
        btn_set = (Button) view.findViewById(R.id.btn_runTesting);
        btnToMoreInfoPage = (Button) view.findViewById(R.id.btnToMoreInfoPage);
        tv_p1.setVisibility(INVISIBLE);
        tv_p2.setVisibility(INVISIBLE);
        et_p1.setVisibility(INVISIBLE);
        et_p2.setVisibility(INVISIBLE);

        tv_select2 = (TextView) view.findViewById(R.id.tv_select2);      //need a chance to do this other stuff,
        tv_select2.setText("Selling Rule");
        tv_p3 = (TextView) view.findViewById(R.id.tv_p3);    //before returning the inflated view.
        tv_p4 = (TextView) view.findViewById(R.id.tv_p4);
        et_p3 = (EditText) view.findViewById(R.id.et_p3);
        et_p4 = (EditText) view.findViewById(R.id.et_p4);
        tv_p3.setVisibility(INVISIBLE);
        tv_p4.setVisibility(INVISIBLE);
        et_p3.setVisibility(INVISIBLE);
        et_p4.setVisibility(INVISIBLE);

        tv_etc = (TextView) view.findViewById(R.id.tv_etc);
        tv_etc.setText("Backtesting Parameters");
        tv_p5 = (TextView) view.findViewById(R.id.tv_p5);
        tv_p5.setText("Ticker:");
        tv_p6 = (TextView) view.findViewById(R.id.tv_p6);
        tv_p6.setText("Timeframe:");
        et_p5 = (EditText) view.findViewById(R.id.et_p5);
//        et_p6 = (EditText) view.findViewById(R.id.et_p6);


        btnToMoreInfoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("TEXT SET TO: " + et_p5.getText().toString());
                if((!et_p5.getText().toString().equals(""))) {
                    par5 = et_p5.getText().toString();
                    par6 = et_p6;
                    backtestingParamsSet = true;
                }
                    if (both_param) {
                        if (isNumeric(et_p1.getText().toString()) & isNumeric(et_p2.getText().toString())) {
                            par1 = et_p1.getText().toString();
                            par2 = et_p2.getText().toString();
                            buyingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error =true;
                        }
                    } else {
                        if (isNumeric(et_p1.getText().toString())) {
                            par1 = et_p1.getText().toString();
                            buyingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    }
                    if (both_param_sell) {
                        if (isNumeric(et_p3.getText().toString()) & isNumeric(et_p4.getText().toString())) {
                            par3 = et_p3.getText().toString();
                            par4 = et_p4.getText().toString();
                            sellingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    } else {
                        if (isNumeric(et_p3.getText().toString())) {
                            par3 = et_p3.getText().toString();
                            sellingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    }


//                else{
//                    System.out.println("NO PARAMETERS!");
//                    Toast.makeText(getContext(), "Please enter parameters", Toast.LENGTH_LONG).show();
//                }

                if (!error && sellingRuleSet && buyingRuleSet) {
                    // DISPLAY POPUP
                    Intent i = new Intent(getContext(), AlgorithmPopUp.class);

                    i.putExtra("par1", par1);
                    i.putExtra("par2", par2);
                    i.putExtra("par3", par3);
                    i.putExtra("par4", par4);
                    i.putExtra("rule", rule);
                    i.putExtra("rule2", rule2);

                    startActivity(i);
//                    algorithmToUse();

                }

                error = false;
                sellingRuleSet = false;
                buyingRuleSet = false;
                backtestingParamsSet = false;

            }

        });


        Spinner spinner = (Spinner) view.findViewById(R.id.dropdown_buy);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.rules, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        ;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner.getSelectedItem().toString();
                Log.i("Selected item : ", items);
                rule = items;
                both_param = true;
//                tv_select.setText("You've selected " + items + " as your buying rule. \n Please enter parameters.");

                tv_p1.setVisibility(View.VISIBLE);

                switch (position) {
                    case 0:
                        tv_p1.setVisibility(INVISIBLE);
                        tv_p2.setVisibility(INVISIBLE);
                        et_p1.setVisibility(INVISIBLE);
                        et_p2.setVisibility(INVISIBLE);
                        both_param=false;
                        break;
                    case 1:
                        et_p1.setVisibility(View.VISIBLE);
                        et_p2.setVisibility(View.INVISIBLE);
                        tv_p1.setVisibility(View.VISIBLE);
                        tv_p2.setVisibility(View.INVISIBLE);
                        tv_p1.setText("Cutoff:");
                        both_param = false;
                        break;
                    case 2:
                        et_p1.setVisibility(View.VISIBLE);
                        et_p2.setVisibility(View.INVISIBLE);
                        tv_p1.setText("Cutoff:");
                        tv_p2.setVisibility(View.INVISIBLE);
                        both_param = false;
                        break;
                    case 3:
                    case 4:
                        et_p1.setVisibility(View.VISIBLE);
                        et_p2.setVisibility(View.VISIBLE);
                        tv_p1.setText("Duration 1:");
                        tv_p2.setVisibility(View.VISIBLE);
                        tv_p2.setText("Duration 2:");
                        break;
                    case 5:
                    case 6:
                        et_p1.setVisibility(View.VISIBLE);
                        et_p2.setVisibility(View.VISIBLE);
                        tv_p1.setText("Bar count:");
                        tv_p2.setVisibility(View.VISIBLE);
                        tv_p2.setText("Min strength:");
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = (Spinner) view.findViewById(R.id.dropdown_sell);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.rules, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        ;

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner2.getSelectedItem().toString();
                Log.i("Selected item : ", items);
                rule2 = items;
                both_param_sell = true;
//                tv_select2.setText("You've selected " + items + " as your selling rule. \n Please enter parameters.");

                tv_p3.setVisibility(View.VISIBLE);

                switch (position) {
                    case 0:
                        tv_p3.setVisibility(INVISIBLE);
                        tv_p4.setVisibility(INVISIBLE);
                        et_p3.setVisibility(INVISIBLE);
                        et_p4.setVisibility(INVISIBLE);
                        both_param_sell=false;
                        break;
                    case 1:
                        et_p3.setVisibility(View.VISIBLE);
                        et_p4.setVisibility(View.INVISIBLE);
                        tv_p3.setVisibility(View.VISIBLE);
                        tv_p4.setVisibility(View.INVISIBLE);
                        tv_p3.setText("Cutoff:");
                        both_param_sell = false;
                        break;
                    case 2:
                        et_p3.setVisibility(View.VISIBLE);
                        et_p4.setVisibility(View.INVISIBLE);
                        tv_p3.setText("Cutoff:");
                        tv_p4.setVisibility(View.INVISIBLE);
                        both_param_sell = false;
                        break;
                    case 3:
                    case 4:
                        et_p3.setVisibility(View.VISIBLE);
                        et_p4.setVisibility(View.VISIBLE);
                        tv_p3.setText("Duration 1:");
                        tv_p4.setVisibility(View.VISIBLE);
                        tv_p4.setText("Duration 2:");
                        break;
                    case 5:
                    case 6:
                        et_p3.setVisibility(View.VISIBLE);
                        et_p4.setVisibility(View.VISIBLE);
                        tv_p3.setText("Bar count:");
                        tv_p4.setVisibility(View.VISIBLE);
                        tv_p4.setText("Min strength:");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinner3 = (Spinner) view.findViewById(R.id.dropdown_days);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),
                R.array.times, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);
        ;

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String items = spinner3.getSelectedItem().toString();
                Log.i("Selected item : ", items);

                switch (position) {
                    case 0:
                        et_p6 = "1d";
                        break;
                    case 1:
                        et_p6 = "5d";
                        break;
                    case 2:
                        et_p6 = "1mo";
                        break;
                    case 3:
                        et_p6 = "3mo";
                        break;
                    case 4:
                        et_p6 = "1y";
                        break;
                    case 5:
                        et_p6 = "5y";
                        break;
                    case 6:
                        et_p6 = "10y";
                        break;
                    case 7:
                        et_p6 = "ytd";
                        break;
                    case 8:
                        et_p6 = "max";
                        break;


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backTestingFragment thisObj = this;
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("TEXT SET TO: " + et_p5.getText().toString());
                if((!et_p5.getText().toString().equals(""))) {
                    par5 = et_p5.getText().toString();
                    par6 = et_p6;
                    backtestingParamsSet = true;

                    // we assume par5 is ticker and par6 is range ("5d", ..)
                    Context context = getContext();
                    YahooFinance.ta4jRequestChart(par5, par6, context, thisObj);

                    if (both_param) {
                        if (isNumeric(et_p1.getText().toString()) & isNumeric(et_p2.getText().toString())) {
                            par1 = et_p1.getText().toString();
                            par2 = et_p2.getText().toString();
                            buyingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error =true;
                        }
                    } else {
                        if (isNumeric(et_p1.getText().toString())) {
                            par1 = et_p1.getText().toString();
                            buyingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    }
                    if (both_param_sell) {
                        if (isNumeric(et_p3.getText().toString()) & isNumeric(et_p4.getText().toString())) {
                            par3 = et_p3.getText().toString();
                            par4 = et_p4.getText().toString();
                            sellingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    } else {
                        if (isNumeric(et_p3.getText().toString())) {
                            par3 = et_p3.getText().toString();
                            sellingRuleSet = true;
                        }
                        else {
                            Toast.makeText(getContext(),"Please set parameters", Toast.LENGTH_LONG).show();
                            error = true;
                        }
                    }
                }

                else{
                    System.out.println("NO PARAMETERS!");
                    Toast.makeText(getContext(), "Please enter parameters", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
}

public void createRules(double[][] data) {
        Log.d("getprices", Arrays.deepToString(data));
    // Setting Buying Rule

    TechnicalAnalysis.loadData(par5, getContext(), data);

    if (!error && sellingRuleSet && buyingRuleSet && backtestingParamsSet && !TechnicalAnalysis.series.isEmpty()) {
        System.out.println(rule);
        switch (rule) {
            case "Price Above":
                try {
                    Double.parseDouble(par1);
                    buying_rule = TechnicalAnalysis.triggerAbove(Double.parseDouble(par1));
                } catch (NumberFormatException e) {
                    //the parseDouble failed and you need to handle it here
                    Toast.makeText(getContext(), "Parameter must be a double", Toast.LENGTH_LONG).show();
                    error = true;
                }
                break;
            case "Price Below":
                try {
                    Double.parseDouble(par1);
                    buying_rule = TechnicalAnalysis.triggerBelow(Double.parseDouble(par1));
                } catch (NumberFormatException e) {
                    //the parseDouble failed and you need to handle it here
                    Toast.makeText(getContext(), "Parameter must be a double", Toast.LENGTH_LONG).show();
                    error = true;
                }
                break;
            case "SMA":
                try {
                    Integer.parseInt(par1);
                    Integer.parseInt(par2);
                    buying_rule = TechnicalAnalysis.SMARule(Integer.parseInt(par1), Integer.parseInt(par2));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Parameter must be an integer", Toast.LENGTH_LONG).show();
                    error = true;
                }
                break;
            case "EMA":
                try {
                    Integer.parseInt(par1);
                    Integer.parseInt(par2);
                    buying_rule = TechnicalAnalysis.EMARule(Integer.parseInt(par1), Integer.parseInt(par2));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Parameter must be an integer", Toast.LENGTH_LONG).show();
                    error = true;
                }
                break;
            case "Rising":
                try {
                    Integer.parseInt(par1);
                    buying_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par1), Double.parseDouble(par2));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Check parameter format", Toast.LENGTH_LONG).show();
                    error = true;
                }
                break;
            case "Falling":
                try {
                    Integer.parseInt(par1);
                    buying_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par1), Double.parseDouble(par2));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Check parameter format", Toast.LENGTH_LONG).show();
                    error = true;
                }
                break;
        }

        // Setting Selling Rule
        System.out.println(rule2);
        if (!error) {
            switch (rule2) {
                case "Price Above":
                    try {
                        Double.parseDouble(par3);
                        selling_rule = TechnicalAnalysis.triggerAbove(Double.parseDouble(par3));
                    } catch (NumberFormatException e) {
                        //the parseDouble failed and you need to handle it here
                        Toast.makeText(getContext(), "Parameter must be a double", Toast.LENGTH_LONG).show();
                        error = true;
                    }
                    break;
                case "Price Below":
                    try {
                        Double.parseDouble(par3);
                        selling_rule = TechnicalAnalysis.triggerBelow(Double.parseDouble(par3));
                    } catch (NumberFormatException e) {
                        //the parseDouble failed and you need to handle it here
                        Toast.makeText(getContext(), "Parameter must be a double", Toast.LENGTH_LONG).show();
                        error = true;
                    }
                    break;
                case "SMA":
                    try {
                        Integer.parseInt(par3);
                        Integer.parseInt(par4);
                        selling_rule = TechnicalAnalysis.SMARule(Integer.parseInt(par3), Integer.parseInt(par4));
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Parameter must be an integer", Toast.LENGTH_LONG).show();
                        error = true;
                    }
                    break;
                case "EMA":
                    try {
                        Integer.parseInt(par3);
                        Integer.parseInt(par4);
                        selling_rule = TechnicalAnalysis.EMARule(Integer.parseInt(par3), Integer.parseInt(par4));
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Parameter must be an integer", Toast.LENGTH_LONG).show();
                        error = true;
                    }
                    break;
                case "Rising":
                    try {
                        Integer.parseInt(par3);
                        selling_rule = TechnicalAnalysis.risingRule(Integer.parseInt(par3), Double.parseDouble(par4));
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Check parameter format", Toast.LENGTH_LONG).show();
                        error = true;
                    }
                    break;
                case "Falling":
                    try {
                        Integer.parseInt(par3);
                        selling_rule = TechnicalAnalysis.fallingRule(Integer.parseInt(par3), Double.parseDouble(par4));
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Check parameter format", Toast.LENGTH_LONG).show();
                        error = true;
                    }
                    break;
            }
        }
        if(!error) {
            TradingRecord tradingRecord = TechnicalAnalysis.triggerTa4j(buying_rule, selling_rule);
            FL.passDataToBackTestingResults(tradingRecord, buying_rule, selling_rule, TechnicalAnalysis.series, par1,par2,par3,par4,par5,rule,rule2);
            System.out.println("Number of trades " + String.valueOf(TechnicalAnalysis.num_trades));
            System.out.println("Total Profit " + String.valueOf(TechnicalAnalysis.totProfit));
        }
    }

    else {
        Toast.makeText(getContext(), "Please enter valid parameters for rules", Toast.LENGTH_LONG).show();

    }

    error = false;
    sellingRuleSet = false;
    buyingRuleSet = false;
    backtestingParamsSet = false;

}
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}