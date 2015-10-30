package com.animebracket.android;

import android.test.AndroidTestCase;

import com.animebracket.android.Util.callbacks.JsonStringCallback;
import com.animebracket.android.Util.tasks.BasicRequestTask;


/**
 * Created by noah on 8/4/2015.
 */
public class HttpTaskTest extends AndroidTestCase implements JsonStringCallback{

    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    public void httpRequestTest() {
        String params[] = {"", "test"};
        BasicRequestTask testTask = new BasicRequestTask(this);
        testTask.execute(params);
    }

    @Override
    public void onJsonStringReceived(String jsonString) {

    }
}

