package capstone.kookmin.silverwatchwear;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class BatterySendThread extends Thread  {
    Handler handler;
    String watchID;
    String percentage;

    public BatterySendThread(Handler handler, String watchID, String percentage){
        this.handler = handler;
        this.watchID = watchID;
        this.percentage = percentage;
    }

    @Override
    public void run(){
        Message msg = new Message();
        try{
            String url = "http://203.246.112.155:5000/battery"; 	//URL
            HashMap<String, String> param = new HashMap<>();
            param.put("watch_id", this.watchID);
            param.put("watch_battery", this.percentage);
            String json = new JSONObject(param).toString();
            String res = sendPost(url, json);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String sendPost(String sendUrl, String jsonValue) throws IllegalStateException {
        String inputLine = null;
        StringBuffer outResult = new StringBuffer();

        try{
            URL url = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            OutputStream os = conn.getOutputStream();
            os.write(jsonValue.getBytes("UTF-8"));
            os.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null){
                outResult.append(inputLine);
            }

            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return outResult.toString();
    }
}
