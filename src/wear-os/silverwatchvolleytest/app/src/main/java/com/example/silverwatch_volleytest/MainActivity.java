package com.example.silverwatch_volleytest;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends WearableActivity {
    RequestQueue requestQueue;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
//        while (true)
        submitData("505b360b-a809-45f1-bb1e-7e3d5f9892a0");
    }

    public void submitData(String data){
        String url = "http://203.246.112.155:5000/check_watch_id?watch_id="+data;
//        JSONObject object = new JSONObject();
//        try {
//            //input your API parameters
//            object.put("watch_id", data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("data", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
