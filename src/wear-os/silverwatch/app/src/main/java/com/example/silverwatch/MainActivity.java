package com.example.silverwatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
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
        SQLiteDatabase registerDB = this.openOrCreateDatabase("register", MODE_PRIVATE, null);

//        idDB.execSQL("DROP TABLE IF EXISTS " + tableName);
//        registerDB.execSQL("DROP TABLE IF EXISTS result");

        idDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid text);");
        registerDB.execSQL("CREATE TABLE IF NOT EXISTS result (register integer)");

        Cursor c = idDB.rawQuery("SELECT * FROM " + tableName, null);
        int rowCount = c.getCount();
        Log.d("db", "Row Count: " + rowCount);

        if (rowCount == 0){
            Intent intent = new Intent(MainActivity.this, ButtonQR.class);
            UUID one = UUID.randomUUID();

            String uuid = one.toString();
            intent.putExtra("id", uuid);
            idDB.execSQL("INSERT INTO " + tableName + " VALUES ('" + uuid + "');");
            startActivity(intent);
            finish();
        }else{
            c.moveToFirst();
            Cursor registerCursor = registerDB.rawQuery("SELECT * FROM result", null);
            int registerRowCount = registerCursor.getCount();
            if (registerRowCount == 0) {
                String uuid = c.getString(c.getColumnIndex("uuid"));
                Intent intent = new Intent(MainActivity.this, CreateQR.class);

                intent.putExtra("id", uuid);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(MainActivity.this, registerFinished.class);
                Log.d("Intent", "Reg Completed");

                startActivity(intent);
                finish();
            }
            registerCursor.close();
        }

        c.close();
        idDB.close();
        registerDB.close();
    }
}
