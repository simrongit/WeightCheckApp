package com.tools.simron.weightcheckapp.secondpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tools.simron.weightcheckapp.R;

public class SecondPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        if(getIntent().hasExtra("val1")){
            String text1 = getIntent().getExtras().getString("val1");
            TextView textV1 = (TextView) findViewById(R.id.page2Text1);
            textV1.setText(text1);
        }
    }
}
