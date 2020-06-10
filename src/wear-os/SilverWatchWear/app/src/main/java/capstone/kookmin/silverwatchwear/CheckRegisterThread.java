package capstone.kookmin.silverwatchwear;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;


public class CheckRegisterThread extends Thread{
    Handler handler;
    String watchID;
    boolean registered = false;
    public CheckRegisterThread(Handler handler, String watchID){
        this.handler = handler;
        this.watchID = watchID;
    }

    @Override
    public void run(){
        Message msg = new Message();
        try{
            while(!registered){
                Thread.sleep(2000);
                request();
            }
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void request(){
        StringBuilder output = new StringBuilder();
        String urlString = "http://203.246.112.155:5000/user?watch_id=" + this.watchID;
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

                JSONObject jsonObject = new JSONObject(output.toString());
                int registerResult = jsonObject.getInt("register_result");
                if (registerResult == 1){
                    registered = true;
                }
                Log.d("JSON", String.valueOf(jsonObject.getInt("register_result")));
            }
        }catch (Exception ex){
            Log.d("ERROR", ex.toString());
        }
        Log.d("RESPONSE", output.toString());
    }
}
