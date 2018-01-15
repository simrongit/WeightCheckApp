package com.tools.simron.weightcheckapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GraphActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series;
    private GestureDetectorCompat gestureObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);



        Intent intent = getIntent();
        ArrayList<WeightInfo> list = (ArrayList<WeightInfo>) intent.getSerializableExtra("data");
        gestureObject = new GestureDetectorCompat(this, new GestureControlForGraph(list, this));
        Collections.reverse(list);
//        list.sort( (obj1, obj2 )-> (new Date(obj1.getDate())).compareTo(new Date(obj2.getDate())));

        GraphView graph = (GraphView) findViewById(R.id.graph1);
        series = new LineGraphSeries<DataPoint>();

        double min = 500;
        double max = 50;
        for(WeightInfo weightInfo : list){
            if(min > weightInfo.getWeight()){
                min = weightInfo.getWeight();
            }
            if(max < weightInfo.getWeight()){
                max = weightInfo.getWeight();
            }
            series.appendData(new DataPoint(new Date(weightInfo.getDate()),weightInfo.getWeight()), true, list.size());
        }
        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(list.get(0).getDate());
        graph.getViewport().setMaxX(list.get(list.size()-1).getDate());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(min-20);
        graph.getViewport().setMaxY(max+20);
        graph.getViewport().setYAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

//        series.setOnDataPointTapListener((series, dataPoint) -> Toast.makeText(this, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show()); // no benefit of it, can't add weightInfo information in it. also it works only with BarGraph
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
