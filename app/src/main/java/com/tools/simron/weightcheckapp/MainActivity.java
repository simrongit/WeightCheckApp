package com.tools.simron.weightcheckapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tools.simron.weightcheckapp.rest.ItemAdapter2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * rest/ItemAdapter2 listview_detail.xml
 * DetailActivity activity_detail.xml
 * UserIdActivity activity_user_id.xml
 * GraphActivity activity_graph.xml
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "WeightCheck";
    private String clientId = "66e7f15d28f2db6af2fe";
    private String clientSecret = "d2e4fc7dfce452639e117d31ecf569a5a95b61ef";
    private String redirectUri = "sim://callback";
    private String generalUri = "http://sim.io";
    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    private GestureDetectorCompat gestureObject;
    private String loggedInUser = "";
    public static final boolean productionMode = true;
    private RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        dateFormat.applyPattern("EEE MMM dd yyyy");
        String hostIpAddress = "";
        if(productionMode){
            hostIpAddress = "18.220.231.181";
        }else{
            hostIpAddress = "192.168.0.15";
        }

        gestureObject = new GestureDetectorCompat(this, new GestureControlForMain(weightList, this));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //    this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Context c = getApplicationContext();
        Resources r = getResources();

        Log.d(TAG, "Logging Works");

        SharedPreferences sharedPref = c.getSharedPreferences(getString(R.string.WeightCheckSharedPreferences), Context.MODE_PRIVATE);

        String defaultValue = r.getString(R.string.PersistentKeyUserId);
//      SharedPreferences.Editor editor = sharedPref.edit();
//      editor.remove(getString(R.string.PersistentKeyUserId));
//      editor.commit();
        loggedInUser = sharedPref.getString(getString(R.string.PersistentKeyUserId), defaultValue);
        Log.d(TAG, loggedInUser);
        if (defaultValue.equals(loggedInUser)) {
            Intent intent = new Intent(this, UserIdActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent); // may be need to call finish as well to disable back functionality, assuming finish on UserIdActivity is enough as it will clear all history hence not adding here
        } else {
            Intent getIntent = getIntent();
            if(getIntent.hasExtra("dataG")) {
                ArrayList<WeightInfo> list = (ArrayList<WeightInfo>) getIntent.getSerializableExtra("dataG");
                Collections.sort(list); // Change due to move from minSdk 24 to 23
//                list.sort( (obj1, obj2 )-> (new Date(obj2.getDate())).compareTo(new Date(obj1.getDate())));
                weightList.addAll(list);
                populateListView(c, weightList);
            }else if(getIntent.hasExtra("weightInfoDA")) {
                int updateWeighInfoIndex = getIntent.getIntExtra("indexDA",0);
                WeightInfo updatedWeightInfo = (WeightInfo)getIntent.getSerializableExtra("weightInfoDA");
                weightList.add(updateWeighInfoIndex,updatedWeightInfo);
                populateListView(c, weightList);
            }
            setTitle("Welcome "+loggedInUser+"!");
            final EditText weightTextBox = (EditText) findViewById(R.id.weightTextBox);
            final EditText hourTextBox = (EditText) findViewById(R.id.hourTextBox);
            final EditText minuteTextBox = (EditText) findViewById(R.id.minuteTextBox);
            final EditText noteTextBox = (EditText) findViewById(R.id.noteET);

            TextView dateTV = (TextView) findViewById(R.id.dateText1);
            final Date date = new Date();
            dateTV.setText(dateFormat.format(date));

            final RequestQueue queue = prepareSerialRequestQueue(this); // Volley.newRequestQueue(this);
            String url = "http://"+hostIpAddress+":8080/lw/lastEntry";
            final Button submit = (Button) findViewById(R.id.submitButton1);

            getLastEntry(weightTextBox, hourTextBox, minuteTextBox, queue, url, submit, noteTextBox, c);
            if(weightList.isEmpty()) {
                getAllEntries(queue, c, hostIpAddress);
            }

            final String host = hostIpAddress;
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "http://"+host+":8080/lw/createEntry";
                    WeightInfo weightInfoToPost = createWeightInfo(date, weightTextBox, hourTextBox, minuteTextBox, noteTextBox);
                    createEntry(queue, url, weightInfoToPost, c, submit);
                }
            });


//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/login/oauth/authorize" + "?client_id=" + clientId +"&scope=repo&redirect_uri="+redirectUri));
//        startActivity(intent);

//        final ListView listView = (ListView) findViewById(R.id.pagination_list);
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl("https://api.github.com/")
//                .addConverterFactory(GsonConverterFactory.create());
//        Retrofit retrofit = builder.build();
//        GitHubClient client = retrofit.create(GitHubClient.class);
//        Call<List<GitHubRepo>> call = client.reposForUser ("simrongit");
//        call.enqueue(new Callback<List<GitHubRepo>> () {
//
//            @Override
//            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
//                List<GitHubRepo> repos = response.body();
//                listView.setAdapter(new GitHubRepoAdapter(MainActivity.this, repos));
//            }
//
//            @Override
//            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
//                Toast.makeText(MainActivity.this,"oops!",Toast.LENGTH_SHORT).show();
//            }
//        });

//        Button page2 = (Button)findViewById(R.id.page2btn);
//        page2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent page2Intent = new Intent(getApplicationContext(), SecondPage.class);
//                page2Intent.putExtra("val1", "1");
//                page2Intent.putExtra("val2", 2);
//                startActivity(page2Intent);
//            }
//        });

                String[] items = new String[]{"Peach", "Orange"};
                String[] prices = r.getStringArray(R.array.prices);
                String[] desc = new String[]{"Peach!", "Orange!"};

                ItemAdapter itemAdapter = new ItemAdapter(this, items, prices, desc);

            }
    }

    private void getLastEntry(final EditText weightTextBox, final EditText hourTextBox, final EditText minuteTextBox, RequestQueue queue, String url, final Button submit, final TextView noteTextBox, Context context) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, loggedInUser,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            weightTextBox.setText(response.getString("weight"));
                            hourTextBox.setText(String.format("%02d", response.getInt("hour")));
                            minuteTextBox.setText(String.format("%02d", response.getInt("min")));
                            if(dateFormat.format(System.currentTimeMillis()).equals(dateFormat.format(response.getLong("date")))) {
                                submit.setEnabled(false);
                            }
                            noteTextBox.setText(response.getString("note"));
//                            mTextView.setText("Success");
                            Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.d(TAG,"Exception",e);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"Oops1! " + error.getMessage());
//                mTextView.setText("Failure " + error.getMessage());
                Toast.makeText(context,"Failure " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonRequest.setRetryPolicy(retryPolicy);
        queue.add(jsonRequest);
    }

    private ArrayList<WeightInfo> weightList = new ArrayList<>();

    private void getAllEntries(RequestQueue queue, final Context context, String host) {
        String url = "http://"+host+":8080/lw/allEntries";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, loggedInUser,
                (response) -> {
                    String status = populateWeightList(weightList, response);
//                    mTextView.setText(status);
                    Toast.makeText(context,"Success " + status, Toast.LENGTH_SHORT).show();
                    populateListView(context, weightList);
                    }
                , (error) -> {
                Log.d(TAG,"Oops2! " + error.getMessage());
//                mTextView.setText("Failure " + error.getMessage());
                Toast.makeText(context,"Failure " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        );
        jsonArrayRequest.setRetryPolicy(retryPolicy);
        queue.add(jsonArrayRequest);
    }

    private String populateWeightList(List<WeightInfo> weightList, JSONArray response) {
        try {
            for(int i=0; i<response.length(); i++){
                JSONObject object = (JSONObject)response.get(i);
                saveWeightInfo(object, weightList);
            }
            return "Success";
        } catch (JSONException e) {
            Log.d(TAG,"Exception",e);
            e.printStackTrace();
            return "Error";
        }
    }

    int CODE_FOR_2ND_ACTIVITY = 2222;
    private void populateListView(Context c, ArrayList<WeightInfo> weightList) {
        ItemAdapter2 itemAdapter2 = new ItemAdapter2(c, weightList);
        ListView listView = (ListView) findViewById(R.id.pagination_list);
        listView.setAdapter(itemAdapter2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Intent activity = new Intent(getApplicationContext(), DetailActivity.class);
                //activity.putExtra("item_index", i);
                Intent activity = new Intent(getApplicationContext(), DetailActivity.class);
                activity.putExtra("weightInfo",weightList.get(i));
                activity.putExtra("loggedInUser", loggedInUser);
                activity.putExtra("weightInfoIndex", i);
                startActivityForResult(activity,CODE_FOR_2ND_ACTIVITY);
            }
        });
    }

    private void saveWeightInfo(JSONObject object, List<WeightInfo> weightList) throws JSONException {
        WeightInfo weightInfo = new WeightInfo();
        weightList.add(weightInfo);
        weightInfo.setWeight(object.getDouble("weight"));
        weightInfo.setHour(object.getInt("hour"));
        weightInfo.setMin(object
                .getInt("min"));
        weightInfo.setDate(object.getLong("date"));
        weightInfo.setNote(object.getString("note"));
    }

    private void createEntry(RequestQueue queue, String url, WeightInfo weightInfoToPost, final Context context, final Button submit) {
        JSONObject jsonBody = createJsonObject(weightInfoToPost);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                (response) -> {
                        try {
//                            mTextView.setText("Response is: "+response.getBoolean("b"));
                            Toast.makeText(context,"Response is: "+response.getBoolean("b"), Toast.LENGTH_SHORT).show();
                            submit.setEnabled(false);
//                            getAllEntries(weightInfoToPost.getUserId(), mTextView, queue, c);
                            weightList.add(weightInfoToPost);
                            Collections.sort(weightList);
//                            weightList.sort( (obj1, obj2 )-> (new Date(obj2.getDate())).compareTo(new Date(obj1.getDate())));
                            populateListView(context, weightList);
                        } catch (JSONException e) {
                            Log.d(TAG,"Exception",e);
                            e.printStackTrace();
                        }
                }, (error) -> {
                Log.d(TAG,"Oops! " + error.getMessage());
//                mTextView.setText("Oops! " + error.getMessage());
                Toast.makeText(context,"Failure " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        );
        jsonRequest.setRetryPolicy(retryPolicy);
        queue.add(jsonRequest);
    }

    @NonNull
    private WeightInfo createWeightInfo(Date date, EditText weightTextBox, EditText hourTextBox, EditText minuteTextBox, EditText noteTextBox) {
        WeightInfo weightInfoToPost = new WeightInfo();
        weightInfoToPost.setDate(date.getTime());
        weightInfoToPost.setUserId(loggedInUser);
        weightInfoToPost.setWeight(Double.parseDouble(weightTextBox.getText().toString()));
        weightInfoToPost.setHour(Integer.parseInt(hourTextBox.getText().toString()));
        weightInfoToPost.setMin(Integer.parseInt(minuteTextBox.getText().toString()));
        weightInfoToPost.setNote(noteTextBox.getText().toString());
        return weightInfoToPost;
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

    @Override
    protected void onResume() {
        super.onResume();
//        Uri uri = getIntent().getData();
//        if(uri != null && uri.toString().startsWith(redirectUri)){
//            String code = uri.getQueryParameter("code");
//
//            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://github.com/").addConverterFactory(GsonConverterFactory.create());
//            Retrofit retrofit = builder.build();
//            GitHubClient client = retrofit.create(GitHubClient.class);
//            Call<AccessToken> accessTokenCall = client.getAccessToken(clientId,clientSecret,code);
//            accessTokenCall.enqueue(new Callback<AccessToken>() {
//                @Override
//                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
//                    Toast.makeText(MainActivity.this,"yay!",Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<AccessToken> call, Throwable t) {
//                    Toast.makeText(MainActivity.this,"oops!",Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(getApplicationContext(),"Intent "+data.getIntExtra("indexDA",-2)+" "+requestCode+" "+resultCode, Toast.LENGTH_SHORT).show();
        if(resultCode == RESULT_OK) {
            int updateWeighInfoIndex = data.getIntExtra("indexDA", 0);
            WeightInfo updatedWeightInfo = (WeightInfo) data.getSerializableExtra("weightInfoDA");
            weightList.remove(updateWeighInfoIndex);
            weightList.add(updateWeighInfoIndex, updatedWeightInfo);
        }
        populateListView(getApplicationContext(), weightList);
    }

    int MAX_SERIAL_THREAD_POOL_SIZE = 1;
    final int MAX_CACHE_SIZE = 2 * 1024 * 1024; //2 MB
    private RequestQueue prepareSerialRequestQueue(Context context) {
        Cache cache = new DiskBasedCache(context.getCacheDir(), MAX_CACHE_SIZE);
        Network network = getNetwork();
        RequestQueue queue = new RequestQueue(cache, network, MAX_SERIAL_THREAD_POOL_SIZE);
        queue.start();
        return queue;
    }
    private static Network getNetwork() {
        HttpStack stack;
        String userAgent = "volley/0";
        if(Build.VERSION.SDK_INT >= 9) {
            stack = new HurlStack();
        } else {
//            stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            stack = null;
        }
        return new BasicNetwork(stack);
    }
}

