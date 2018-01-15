package com.tools.simron.weightcheckapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by simro on 11/12/2017.
 */

public class GestureControlForGraph extends GestureDetector.SimpleOnGestureListener {

    private ArrayList<WeightInfo> graphWeightList;
    private Activity graphActivity;

    public GestureControlForGraph(ArrayList<WeightInfo> weightList, Activity activity){
        super();
        graphWeightList = weightList;
        graphActivity = activity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if(e1.getX() < e2.getX()){ //swipe right
            Intent activity = new Intent(graphActivity, MainActivity.class);
            activity.putExtra("dataG", graphWeightList);
            graphActivity.finish();
            graphActivity.startActivity(activity);
        }else if(e1.getX() > e2.getX()){ // swipe left
Log.d("ABC", "ABC");
        }

        return true;
    }
}
