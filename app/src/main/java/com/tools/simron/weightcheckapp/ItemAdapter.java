package com.tools.simron.weightcheckapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.simron.weightcheckapp.rest.HttpRequestAsk;

/**
 * Created by simro on 11/3/2017.
 */

public class ItemAdapter extends BaseAdapter {

    LayoutInflater inflater;
    String[] items;
    String[] prices;
    String[] desc;

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.listview_detail, null);
        TextView textView1 = (TextView) v.findViewById(R.id.lvTV1);
        textView1.setText(items[i]);
        textView1.setTextColor(Color.BLACK);
        TextView textView2 = (TextView) v.findViewById(R.id.lvTV2);
        textView2.setText(prices[i]);
        TextView textView3 = (TextView) v.findViewById(R.id.lvTV3);
        textView3.setText(desc[i]);
        HttpRequestAsk httpRequestAsk = new HttpRequestAsk(0, v);

        return v;
    }

    public ItemAdapter(Context c, String[] i, String[] p, String[] d){
        items = i;
        prices = p;
        desc = d;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}
