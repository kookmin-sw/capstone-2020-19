package com.example.silverwatch;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
            SQLiteDatabase registerDB = this.openOrCreateDatabase("register", MODE_PRIVATE, null);
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run(){
                    boolean registerResult = false;
                    while(!registerResult){
                        // TODO: GET REGISTRATION RESULT
                        registerResult = true;
                    }
                    registerDB.execSQL("INSERT INTO result VALUES (1)");

                    Intent mainIntent = new Intent(CreateQR.this, registerFinished.class);
                    startActivity(mainIntent);
                }
            }, 0);

        }catch (Exception e){}
    }
}