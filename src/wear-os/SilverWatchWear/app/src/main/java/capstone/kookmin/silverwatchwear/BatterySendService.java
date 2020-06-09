package capstone.kookmin.silverwatchwear;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BatterySendService extends Service {
    public BatterySendService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Log.d("battery service", "on create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("battery service", "on destroy");
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        startForeground(9, notification);
        Log.d("battery service", "on start command");
        request();
        if (intent == null){
            return Service.START_STICKY;
        }else{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String command = intent.getStringExtra("command");
            request();
            Log.d("batteryservice", "command: " + command);
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void request(){
        StringBuilder output = new StringBuilder();
        String urlString = "http://203.246.112.155:5000/";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null){
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int resCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while (true){
                    line = reader.readLine();
                    if (line == null){
                        break;
                    }

                    output.append(line);
                }
                reader.close();
                conn.disconnect();

            }
        }catch (Exception ex){
            Log.d("ERROR", ex.toString());
        }
        Log.d("RESPONSE", output.toString());
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
