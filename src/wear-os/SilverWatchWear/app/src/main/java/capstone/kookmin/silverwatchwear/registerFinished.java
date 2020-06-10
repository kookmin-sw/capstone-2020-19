package capstone.kookmin.silverwatchwear;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class registerFinished extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finished);
        setAmbientEnabled();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("finished", "gps permission");
            ActivityCompat.requestPermissions( registerFinished.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        Intent intent = getIntent();
        String uuid = intent.getStringExtra("watchID");
        Intent sendService = new Intent(getApplicationContext(), SendService.class);
        Log.d("finisheduuid", uuid);
        sendService.putExtra("watchID", uuid);
        startForegroundService(sendService);
        Log.d("running", String.valueOf(isLaunchingService(getApplicationContext())));
    }

    public Boolean isLaunchingService(Context mContext){

        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SendService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return  false;
    }
}
