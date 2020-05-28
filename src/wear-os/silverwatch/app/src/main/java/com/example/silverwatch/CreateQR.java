package com.example.silverwatch;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Date;

public class CreateQR extends WearableActivity {
    private ImageView iv;
    private String text;
    boolean registerResult = false;
    RequestQueue requestQueue;
    private final String USER_AGENT = "Mozilla/5.0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        Intent intent = getIntent();
        iv = (ImageView)findViewById(R.id.qrcode);
        text = intent.getStringExtra("id");

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            final SQLiteDatabase registerDB = this.openOrCreateDatabase("register", MODE_PRIVATE, null);
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run(){
                    long timeInMilliseconds = System.currentTimeMillis();
                    while(!registerResult){
                        if (System.currentTimeMillis() - timeInMilliseconds > 1000){
                            timeInMilliseconds = System.currentTimeMillis();
                            String url = "http://203.246.112.155:5000/check_watch_id?";
                            url += "watch_id=" + text;
                            Log.d("data", text);

                            submitData(text);
                        }
                    }
                    registerDB.execSQL("INSERT INTO result VALUES (1)");

                    Intent mainIntent = new Intent(CreateQR.this, registerFinished.class);
                    startActivity(mainIntent);
                }
            }, 1000);

        }catch (Exception e){}
    }

    public void submitData(String data){
        String url = "http://203.246.112.155:5000/check_watch_id?watch_id=" + data;
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