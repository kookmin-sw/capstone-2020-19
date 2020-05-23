package com.example.silverwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class MainActivity extends WearableActivity {
    private Button createQRBtn;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        setAmbientEnabled();

        createQRBtn = (Button) findViewById(R.id.createQR);

        createQRBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CreateQR.class);
                UUID one = UUID.randomUUID();
                intent.putExtra("id", one.toString());
                startActivity(intent);
            }
        });
    }
}
