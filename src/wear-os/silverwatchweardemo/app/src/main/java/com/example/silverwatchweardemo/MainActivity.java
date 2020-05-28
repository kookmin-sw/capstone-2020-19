package com.example.silverwatchweardemo;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MainActivity extends WearableActivity {
    SensorManager manager;
    List<Sensor> sensors;
    Deque<String> sensorData = new ArrayDeque<>();

    private TextView textView;
    public SensorEventListener mySensorLister = new SensorEventListener() {

        @SuppressLint("DefaultLocale")
        @Override
        public void onSensorChanged(SensorEvent event) {
            String output = "";
            String temp = "";
            for(int index = 0;index < event.values.length;++index){
                if (event.values[index] == 1.0){
                    output = "착용";
                }else{
                    output = "미착용";
                }
//                output += String.format("%.3f", event.values[index]);
            }
            println(output);
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
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
    public void registerOffBodySensor(){
        manager.registerListener(
                mySensorLister, sensors.get(24), 100000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();
        getSensorList();
        registerOffBodySensor();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onStop(){
        super.onStop();
        finishAffinity();
        System.exit(0);
    }

    public void println(String data){
        textView.setText(data);
    }
}
