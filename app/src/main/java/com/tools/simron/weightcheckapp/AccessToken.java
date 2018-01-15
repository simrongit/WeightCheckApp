package com.tools.simron.weightcheckapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by simro on 10/31/2017.
 */

class AccessToken {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}
