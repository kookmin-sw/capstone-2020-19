package com.example.silverwatch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Date;

public class CreateQR extends AppCompatActivity {
    private ImageView iv;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        Intent intent = getIntent();
        iv = (ImageView)findViewById(R.id.qrcode);
        text = intent.getStringExtra("id");

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run(){

                    Date start = new Date();
                    while(true){
                        Date end = new Date();
                        if ((end.getTime() - start.getTime()) / 1000 > 1) break;
                    }
                    Log.d("time", "start");
                    start = new Date();
                    while(true){
                        Date end = new Date();
                        if ((end.getTime() - start.getTime()) / 1000 > 2) break;
                    }

                    Log.d("time", "end");
                    Intent mainIntent = new Intent(CreateQR.this, registerFinished.class);
                    Log.d("Intent", "start finished activity");
                    startActivity(mainIntent);
                }
            }, 500);

        }catch (Exception e){}
    }
}