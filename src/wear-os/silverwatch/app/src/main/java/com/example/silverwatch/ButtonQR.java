package com.example.silverwatch;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class ButtonQR extends AppCompatActivity {
    private Button createQRBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_qr);
        createQRBtn = (Button) findViewById(R.id.createQR);

        createQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ButtonQR.this, CreateQR.class);
                Intent prevIntent = getIntent();
                String text = prevIntent.getStringExtra("id");
                intent.putExtra("id", text);
                startActivity(intent);
                finish();
            }
        });
    }
}
