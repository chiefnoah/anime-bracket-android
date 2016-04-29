package com.animebracket.android.Util.models;

import java.io.Serializable;

/**
 * Created by Noah Pederson on 1/28/2015.
 */
public class BasicResponse implements Serializable{
    private boolean success;
    private String message;
    private BasicResponseData data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BasicResponseData getData() {
        return data;
    }

    public void setData(BasicResponseData data) {
        this.data = data;
    }
}
