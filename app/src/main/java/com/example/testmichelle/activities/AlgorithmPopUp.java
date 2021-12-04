package com.example.testmichelle.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testmichelle.R;
import com.google.firebase.auth.FirebaseAuth;

public class AlgorithmPopUp extends AppCompatActivity {

    TextView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithmpopup);
        TextView information = (TextView) findViewById(R.id.information);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        System.out.println("width height: " + width + " " + height);

        getWindow().setLayout((int) (width*.6), (int) (height*.6));

        Bundle bundle = getIntent().getExtras();

        String par1 = bundle.getString("par1");
        String par2 = bundle.getString("par2");
        String par3 = bundle.getString("par3");
        String par4 = bundle.getString("par4");
        String rule = bundle.getString("rule");
        String rule2 = bundle.getString("rule2");

        String explanation = information(par1, par2, par3, par4, rule, rule2);

        System.out.println("explanation: " + explanation);
        information.setText(explanation);

        }

    private String information(String par1, String par2, String par3, String par4, String rule, String rule2) {
        String info = "This algorithm will buy when ";

        // First do buying rule
        switch (rule) {
            case "Price Above":
                info += "the stock price goes above " + par1 + ".";
                break;
            case "Price Below":
                info += "the stock price goes below " + par1 + ".";
                break;
            case "SMA":
                info += "the average stock price in the past " + par1 + " days goes above the average stock price in the past " + par2 + " days.";
                break;
            case "EMA":
                info += "the average stock price (weighted more heavily towards recent prices) in the past " + par1 + " days goes above the average stock price (weighted more heavily towards recent prices) in the past " + par2 + " days.";
                break;
            case "Rising":
                info += "considering a window of the past " + par1 + " days, the stock rose in price " + par2 + "percentage of the time";
                break;
            case "Falling":
                info += "considering a window of the past " + par1 + " days, the stock fell in price " + par2 + "percentage of the time";
                break;
        }

        info += " This algorithm will sell when ";

        switch (rule2) {
            case "Price Above":
                info += "the stock price goes above " + par3 + ".";
                break;
            case "Price Below":
                info += "the stock price goes below " + par3 + ".";
                break;
            case "SMA":
                info += "the average stock price in the past " + par3 + " days goes above the average stock price in the past " + par4 + " days.";
                break;
            case "EMA":
                info += "the average stock price (weighted more heavily towards recent prices) in the past " + par3 + " days goes above the average stock price (weighted more heavily towards recent prices) in the past " + par4 + " days.";
                break;
            case "Rising":
                info += "considering a window of the past " + par3 + " days, the stock rose in price " + par4 + "percentage of the time.";
                break;
            case "Falling":
                info += "considering a window of the past " + par3 + " days, the stock fell in price " + par4 + "percentage of the time.";
                break;
        }

        return info;

    };
}
