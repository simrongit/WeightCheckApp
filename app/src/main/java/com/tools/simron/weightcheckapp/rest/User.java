package com.tools.simron.weightcheckapp.rest;

import java.io.Serializable;

/**
 * Created by simro on 11/4/2017.
 */

public class User implements Serializable {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
