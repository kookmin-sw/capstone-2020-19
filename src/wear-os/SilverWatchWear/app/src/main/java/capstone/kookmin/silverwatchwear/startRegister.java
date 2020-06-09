package capstone.kookmin.silverwatchwear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

public class startRegister extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_register);
        setAmbientEnabled();
    }

    public void moveToQRCode(View v){
        final String tableName = "id";
        SQLiteDatabase idDB = this.openOrCreateDatabase("ID", MODE_PRIVATE, null);

//        idDB.execSQL("DROP TABLE IF EXISTS " + tableName);

        String uuid;
        idDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid text);");

        Cursor c = idDB.rawQuery("SELECT * FROM " + tableName, null);
        int rowCount = c.getCount();
        Log.d("db", "Row Count: " + rowCount);

        if (rowCount == 0){
            uuid = UUID.randomUUID().toString();
            idDB.execSQL("INSERT INTO " + tableName + " VALUES ('" + uuid + "');");
        }else{
            c.moveToFirst();
            uuid = c.getString(c.getColumnIndex("uuid"));
        }
        Log.d("start register uuid", uuid);
        Intent intent = new Intent(startRegister.this, qrGeneration.class);
        intent.putExtra("uuid", uuid);
        startActivity(intent);
    }
}
