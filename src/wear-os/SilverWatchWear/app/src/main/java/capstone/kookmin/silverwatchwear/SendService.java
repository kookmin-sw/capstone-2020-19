package capstone.kookmin.silverwatchwear;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SendService extends Service {
    public SendService() {

    }

    String uuid;
    Timer batteryTimer = new Timer();
    Timer gpsTimer = new Timer();
    String latitude = "";
    String longitude = "";
    LocationManager lm;
    SensorManager manager;
    List<Sensor> sensors;

    @Override
    public void onCreate() {
        super.onCreate();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        createNotificationChannel();
        Log.d("service", "on create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        batteryTimer.cancel();
        gpsTimer.cancel();
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

    public SensorEventListener mySensorLister = new SensorEventListener() {

        @SuppressLint("DefaultLocale")
        @Override
        public void onSensorChanged(SensorEvent event) {
            int output = 0;
            for(int index = 0;index < event.values.length;++index){
                if (event.values[index] == 1.0){
                    output = 1;
                }
            }
            WearSendThread wearSendThread = new WearSendThread(wearHandler, uuid, output);
            wearSendThread.start();
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void getSensorList(){
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensors = manager.getSensorList(Sensor.TYPE_ALL);

        int index = 0;
        String data = "";
        for(Sensor sensor : sensors){
            data += "#" + index + " : " + sensor.getName() + "\n";
            index++;
        }
    }
    public void registerOffBodySensor(){
        manager.registerListener(
                mySensorLister, sensors.get(24), 100000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uuid = intent.getStringExtra("watchID");
        Log.d("sendserviceuuid", uuid);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        getSensorList();
        registerOffBodySensor();
        Notification notification = builder.build();
        startForeground(9, notification);
        Log.d("service", "on start command");
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return 1;
            }
            final Location location = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                Log.d("location", latitude + " "  + longitude);
            }else{
//                Location networkLocation = Objects.requireNonNull(lm).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                latitude = String.valueOf(networkLocation.getLatitude());
//                longitude = String.valueOf(networkLocation.getLongitude());
                Log.d("gps", "failed");
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);

            TimerTask batteryTimeTask = new TimerTask() {
                @Override
                public void run() {
                    IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    Intent batteryStatus = registerReceiver(null, ifilter);

                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                    float batteryPct = level / (float) scale;
                    String value = String.valueOf((int) (batteryPct * 100));
                    Log.d("battery", value);
                    BatterySendThread batterySendThread = new BatterySendThread(batteryHandler, uuid, value);
                    batterySendThread.start();
                }
            };
            batteryTimer.schedule(batteryTimeTask, 0, 20000); //Timer 실행

            TimerTask gpsTimeTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d("gps", "run");
                    if (!(longitude.equals("") || latitude.equals(""))) {
                        GpsSendThread gpsSendThread = new GpsSendThread(gpsHandler, uuid, longitude, latitude);
                        gpsSendThread.start();
                    }else{
                        Log.d("location", "failed");
                    }
                }
            };
            gpsTimer.schedule(gpsTimeTask, 0, 20000);
            // timer.cancel();//타이머 종료
            getSensorList();
            registerOffBodySensor();
            // TODO: 낙상
        }
        return super.onStartCommand(intent, flags, startId);
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    @SuppressLint("HandlerLeak")
    Handler batteryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                Log.d("battery", "success");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler gpsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                Log.d("gps", "success");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler wearHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                Log.d("wear", "success");
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
