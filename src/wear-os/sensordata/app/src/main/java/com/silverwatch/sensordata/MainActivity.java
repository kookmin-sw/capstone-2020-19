package com.silverwatch.sensordata;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Map;

public class MainActivity extends WearableActivity {

    TextView textView;

    SensorManager manager;
    List<Sensor> sensors;
    Deque<String> sensorData = new ArrayDeque<>();
//    String sensorValue = "x,y,z\n";
    final int SENSOR_NUMBER = 40;
    boolean isWalk = true;

    public SensorEventListener mySensorLister = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
//            String output = "Sensor Timestamp : " + event.timestamp + "\n\n";
            String output = "";
            String temp = "";
            for(int index = 0;index < event.values.length;++index){
                output += String.format("%.3f", event.values[index]) + ",";
                temp +=  event.values[index] + ",";
            }
            if (sensorData.size() >= 3000) sensorData.pop();
            sensorData.add(temp.substring(0, temp.length() - 1) + "\n");
//            sensorValue += temp.substring(0, temp.length() - 1) + "\n";
            println(output.substring(0, output.length() - 1));
            output = "";
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // Enables Always-on
        setAmbientEnabled();

        getSensorList();
    }

    @Override
    protected void onPause(){
        super.onPause();
        manager.unregisterListener(mySensorLister);
        finishAffinity();
        System.exit(0);
//        submitData(sensorValue);
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
////        manager.registerListener(
////                mySensorLister, sensors.get(SENSOR_NUMBER), 100000);
//    }

    @Override
    protected void onStop(){
        super.onStop();
        manager.unregisterListener(mySensorLister);
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        manager.unregisterListener(mySensorLister);
        finishAffinity();
        System.exit(0);
    }

    public void walkButtonClicked(View v){
        println("WALK START");
        isWalk = true;
        registerAccelerometerSensor();
    }

    public void fallButtonClicked(View v){
        println("FALL START");
        isWalk = false;
        registerAccelerometerSensor();
    }

    public void endButtonClicked(View v){
        manager.unregisterListener(mySensorLister);
        submitData();
        println("END");
    }

    public void getSensorList(){
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = manager.getSensorList(Sensor.TYPE_ALL);

        int index = 0;
        String data = "";
        for(Sensor sensor : sensors){
            data += "#" + index + " : " + sensor.getName() + "\n";
            index++;
        }
//        submitData(data);
//        println(data);
//        registerAccelerometerSensor();
    }

    public void submitData(){
        String url = "https://0i771f8hz3.execute-api.ap-northeast-2.amazonaws.com/demo/upload";
        String data = "x,y,z\n";
        while(!sensorData.isEmpty()){
            data += sensorData.poll();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("data", data);
            if (isWalk){
                object.put("isWalk", 1);
            }else{
                object.put("isWalk", 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        sensorData = null;
        System.gc();
        sensorData = new ArrayDeque<>();
    }

    public void registerAccelerometerSensor(){
        manager.registerListener(
                mySensorLister, sensors.get(SENSOR_NUMBER), 100000);
    }

    public void println(String data){
        textView.setText(data);
    }
}
