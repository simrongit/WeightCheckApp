package com.tools.simron.weightcheckapp.rest;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.tools.simron.weightcheckapp.R;
import com.tools.simron.weightcheckapp.WeightInfo;

/**
 * Created by simro on 11/4/2017.
 */

public class HttpRequestAsk extends AsyncTask<Void, Void, WeightInfo> {

    private int id;
    private View v;
    public HttpRequestAsk(int id, View c){
        this.id = id;
        this.v = c;
    }
    @Override
    protected WeightInfo doInBackground(Void... voids) {
        ProductRestClient client = new ProductRestClient();
        WeightInfo product = client.find(0);
        return product;
    }

    @Override
    protected  void onPostExecute(WeightInfo product){
        TextView textView1 = (TextView) v.findViewById(R.id.lvTV1);
        TextView textView2 = (TextView) v.findViewById(R.id.lvTV2);
        textView1.setText(String.valueOf(product.getWeight()));
        textView2.setText(String.valueOf(product.getHour()));

    }
}
