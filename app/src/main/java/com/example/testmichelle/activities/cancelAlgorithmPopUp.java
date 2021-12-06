package com.example.testmichelle.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testmichelle.R;

public class cancelAlgorithmPopUp extends AppCompatActivity {

    TextView tv_canceltext;
    Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithmpopup);

        tv_canceltext = (TextView) findViewById(R.id.tv_canceltext);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.75), (int) (height*.75));

    }
}
