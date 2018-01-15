package com.tools.simron.weightcheckapp.rest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.simron.weightcheckapp.R;
import com.tools.simron.weightcheckapp.WeightInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by simro on 11/3/2017.
 */

public class ItemAdapter2 extends BaseAdapter {

    LayoutInflater inflater;
//    String[] items;
//    String[] prices;
//    String[] desc;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");

    private List<WeightInfo> weightInfos;

    @Override
    public int getCount() {
        return weightInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return weightInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.listview_detail, null);
        TextView textView1 = (TextView) v.findViewById(R.id.lvTV1);
        textView1.setText(weightInfos.get(i).getWeight()+"");
        textView1.setTextColor(Color.BLACK);
        TextView textView2 = (TextView) v.findViewById(R.id.lvTV2);
        textView2.setText(String.format("%02d", weightInfos.get(i).getHour()));
        textView2.setTextColor(Color.BLACK);
        TextView textView3 = (TextView) v.findViewById(R.id.lvTV3);
        textView3.setText(String.format("%02d", weightInfos.get(i).getMin()));
        textView3.setTextColor(Color.BLACK);
//        HttpRequestAsk httpRequestAsk = new HttpRequestAsk(0, v);
        TextView textView4 = (TextView) v.findViewById(R.id.lvTV4);
        textView4.setText(dateFormat.format(weightInfos.get(i).getDate()));
        textView4.setTextColor(Color.BLACK);

        return v;
    }

    public ItemAdapter2(Context c, List<WeightInfo> weightInfos){
        this.weightInfos = weightInfos;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

}
