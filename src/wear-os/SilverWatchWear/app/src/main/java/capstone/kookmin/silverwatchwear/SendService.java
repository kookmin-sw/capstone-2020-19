package capstone.kookmin.silverwatchwear;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class SendService extends Service {
    public SendService() {

    }
    String uuid;
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        Log.d("service", "on create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("service", "on destroy");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel("1", "back", importance);
            channel.setDescription("background");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uuid = intent.getStringExtra("watchID");
        Log.d("sendserviceuuid", uuid);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        startForeground(9, notification);
        Log.d("service", "on start command");
        if (intent == null){
            return Service.START_STICKY;
        }else{
            Timer batteryTimer = new Timer();
            TimerTask batteryTimeTask = new TimerTask() {
                @Override
                public void run() {
                    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = registerReceiver(null, ifilter);

                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    float batteryPct = level / (float)scale;
                    String value = String.valueOf((int)(batteryPct*100));
                    Log.d("battery", value);
                    BatterySendThread batterySendThread = new BatterySendThread(batteryHandler, uuid, value);
                    batterySendThread.start();
                }
            };
            batteryTimer.schedule(batteryTimeTask, 0, 3000); //Timer 실행

            // timer.cancel();//타이머 종료
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @SuppressLint("HandlerLeak")
    Handler batteryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                Log.d("battery", "success");
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                Log.d("send", "battery success");
            }
        }
    };
}
