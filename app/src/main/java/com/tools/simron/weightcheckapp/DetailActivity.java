package com.tools.simron.weightcheckapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "WeightCheck";

    String hostIpAddress = "";
    private WeightInfo weightInfoToPost = new WeightInfo();
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(MainActivity.productionMode){
            hostIpAddress = "18.220.231.181";
        }else{
            hostIpAddress = "192.168.0.15";
        }

        Context context = getApplicationContext();
        Intent in = getIntent();
        WeightInfo weightInfo = (WeightInfo)in.getSerializableExtra("weightInfo");
        String user = in.getStringExtra("loggedInUser");
        int index = in.getIntExtra("weightInfoIndex",0);

        EditText weightET = findViewById(R.id.daWeightET);
        weightET.setText(weightInfo.getWeight()+"");
        EditText wakeUpET = findViewById(R.id.daWakeUpET);
        wakeUpET.setText(weightInfo.getHour()+":"+weightInfo.getMin());
        TextView textView2 = findViewById(R.id.daCreationTV2);
        textView2.setText(new Date(weightInfo.getDate())+"");
        EditText noteET = findViewById(R.id.daNoteET);
        noteET.setText(weightInfo.getNote());

//        SimpleDateFormat format = new SimpleDateFormat();
        RequestQueue queue = Volley.newRequestQueue(this);

        Button submit = findViewById(R.id.daSubmitB);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                weightInfoToPost.setDate(weightInfo.getDate());
                weightInfoToPost.setUserId(user);
                weightInfoToPost.setWeight(Double.parseDouble(weightET.getText().toString()));
                weightInfoToPost.setHour(Integer.parseInt(wakeUpET.getText().toString().split(":")[0]));
                weightInfoToPost.setMin(Integer.parseInt(wakeUpET.getText().toString().split(":")[1]));
                weightInfoToPost.setNote(noteET.getText().toString());

                String url = "http://"+hostIpAddress+":8080/lw/createEntry";
                createEntry(queue, url, weightInfoToPost, context, submit);

                Intent intent = new Intent();
                intent.putExtra("weightInfoDA",weightInfoToPost);
                intent.putExtra("indexDA", index);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("weightInfoDA",weightInfoToPost);
//        intent.putExtra("indexDA", index);
//        setResult(RESULT_OK,intent);
//        super.onBackPressed();
//    }

    private void createEntry(RequestQueue queue, String url, WeightInfo weightInfoToPost, final Context context, final Button submit) {
        JSONObject jsonBody = createJsonObject(weightInfoToPost);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                (response) -> {
                    try {
                        Toast.makeText(context,"Response is: "+response.getBoolean("b"), Toast.LENGTH_SHORT).show();
                        submit.setEnabled(false);
                        Log.d(TAG,"Posted! ");
                    } catch (JSONException e) {
                        Log.d(TAG,"Exception",e);
                        e.printStackTrace();
                    }
                }, (error) -> {
            Log.d(TAG,"Oops! " + error.getMessage());
            Toast.makeText(context,"Failure " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        );
        queue.add(jsonRequest);
    }

    @Nullable
    private JSONObject createJsonObject(WeightInfo weightInfoToPost) {
        Gson gson = new GsonBuilder().create();
        JSONObject jsonBody = null;
//                long time = System.currentTimeMillis();
        try {
            jsonBody = new JSONObject(gson.toJson(weightInfoToPost)); //"{\"date\":\""+time+"\",\"userId\":\"Simron\",\"weightTime\":{\"hour\":8,\"min\":0,\"weight\":170.2}}"); //
        } catch (JSONException e) {
            Log.d(TAG,"Exception",e);
            e.printStackTrace();
        }
        return jsonBody;
    }
}
