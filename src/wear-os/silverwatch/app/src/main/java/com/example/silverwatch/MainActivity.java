package com.example.silverwatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class MainActivity extends WearableActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String tableName = "id";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();
        SQLiteDatabase idDB = this.openOrCreateDatabase("ID", MODE_PRIVATE, null);

        idDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                + " (id VARCHAR(50));");

        Cursor c = idDB.rawQuery("SELECT * FROM " + tableName, null);
        if (!c.moveToFirst()){
            Intent intent = new Intent(MainActivity.this, ButtonQR.class);
            UUID one = UUID.randomUUID();
            intent.putExtra("id", one.toString());
            startActivity(intent);
        }


//        createQRBtn = (Button) findViewById(R.id.createQR);
//
//        createQRBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                Intent intent = new Intent(MainActivity.this, ButtonQR.class);
//                UUID one = UUID.randomUUID();
//                intent.putExtra("id", one.toString());
//                startActivity(intent);
//            }
//        });
    }
}
