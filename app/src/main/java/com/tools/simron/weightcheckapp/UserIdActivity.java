package com.tools.simron.weightcheckapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class UserIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);
        Context c = this;

        EditText userId = (EditText) findViewById(R.id.userIdET);
        Button submit = (Button) findViewById(R.id.submitBU);
        submit.setOnClickListener((view)-> {
                    String inputUserId = userId.getText().toString();
                    if (!inputUserId.isEmpty() && inputUserId.length() < 32) {
                        SharedPreferences sharedPref = c.getSharedPreferences(getString(R.string.WeightCheckSharedPreferences), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.PersistentKeyUserId), inputUserId);
                        editor.commit();
                        Intent intent = new Intent(this, MainActivity.class);
//                        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
                        startActivity(intent);
                        finish();
                    }
                }
        );



    }
}
