package com.example.silverwatchfalldemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

//import com.google.android.gms.common.api.Response;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.List;

public class MainActivity extends WearableActivity {
    SensorManager manager;
    List<Sensor> sensors;
    Deque<String> sensorData = new ArrayDeque<>();
    boolean isWalk = true;
    final int SENSOR_NUMBER = 0;

    private TextView textView;
    private TextView fallResultText;
    int count = 1;
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
            if (sensorData.size() >= 30){
                getFallResult();
                long time = System.currentTimeMillis();
                SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm:ss");
                String str = dayTime.format(new Date(time));
                fallResultText.setText(String.format("%s: %s", str, isWalk));
            }
            sensorData.add(temp.substring(0, temp.length() - 1) + "\n");
            println(output.substring(0, output.length() - 1));
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void getFallResult(){
        String url = "http://203.246.112.155:8000";
        String sensorValue = "x,y,z\n";
        while (!sensorData.isEmpty()){
            sensorValue += sensorData.poll();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("id", "demotest");
            object.put("data", sensorValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fall", String.valueOf(response));
                        try {
                            if ((Integer) response.getInt("result") == 1){
                                isWalk = true;
                            }else{
                                isWalk = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fall", String.valueOf(error));
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void getSensorList(){
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = manager.getSensorList(Sensor.TYPE_ALL);
    }


    public void registerAccelerometerSensor(){
        manager.registerListener(
                mySensorLister, sensors.get(SENSOR_NUMBER), 100000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        fallResultText = (TextView) findViewById(R.id.result);

        // Enables Always-on
        setAmbientEnabled();
        getSensorList();
        registerAccelerometerSensor();
    }

    public void println(String data){
        textView.setText(data);
    }
}
