package capstone.kookmin.silverwatchwear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

public class registerFinished extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finished);
        setAmbientEnabled();
        Intent batterySendService = new Intent(getApplicationContext(), BatterySendService.class);
        startForegroundService(batterySendService);
        Log.d("running", String.valueOf(isLaunchingService(getApplicationContext())));
    }

    public Boolean isLaunchingService(Context mContext){

        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BatterySendService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return  false;
    }
}
