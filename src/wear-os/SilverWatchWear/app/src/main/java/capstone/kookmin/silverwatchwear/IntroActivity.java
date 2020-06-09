package capstone.kookmin.silverwatchwear;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

public class IntroActivity extends WearableActivity {
    SQLiteDatabase registerDB;
    SQLiteDatabase idDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setAmbientEnabled();
        idDB = this.openOrCreateDatabase("ID", MODE_PRIVATE, null);
        registerDB = this.openOrCreateDatabase("register", MODE_PRIVATE, null);
//        registerDB.execSQL("DROP TABLE IF EXISTS result");
        registerDB.execSQL("CREATE TABLE IF NOT EXISTS result (register integer)");
        if(!isIgnoringBatteryOptimizations(this)) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        IntroThread introThread = new IntroThread(handler);
        introThread.start();


    }
    static boolean isIgnoringBatteryOptimizations(Context context) {
        PowerManager powerManager =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return true;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                Cursor registerCursor = registerDB.rawQuery("SELECT * FROM result", null);
                int registerRowCount = registerCursor.getCount();
                if (registerRowCount == 0) {
                    Intent intent = new Intent(IntroActivity.this, startRegister.class);
                    startActivity(intent);
                    finish();
                }else{
                    final String tableName = "id";
                    Cursor c = idDB.rawQuery("SELECT * FROM " + tableName, null);
                    Log.d("cursor", String.valueOf(c.getCount()));
                    c.moveToFirst();
                    String uuid = c.getString(c.getColumnIndex("uuid"));
                    Intent intent = new Intent(IntroActivity.this, registerFinished.class);
                    intent.putExtra("watchID", uuid);
                    Log.d("Intent", "Reg Completed");
                    startActivity(intent);
                }
                registerCursor.close();
            }
        }
    };
}
