package com.tools.simron.weightcheckapp;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by simro on 11/12/2017.
 */

public class GestureControlForMain extends GestureDetector.SimpleOnGestureListener {

    private ArrayList<WeightInfo> mainWeightList;
    private Activity mainActivity;

    public GestureControlForMain(ArrayList<WeightInfo> weightList, Activity activity){
        super();
        mainWeightList = weightList;
        mainActivity = activity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if(e1.getX() < e2.getX()){ //swipe right
        }else if(e1.getX() > e2.getX()){ // swipe left
            Intent activity = new Intent(mainActivity, GraphActivity.class);
            activity.putExtra("data",mainWeightList);
            mainActivity.finish();
            mainActivity.startActivity(activity);
        }

        return true;
    }
}