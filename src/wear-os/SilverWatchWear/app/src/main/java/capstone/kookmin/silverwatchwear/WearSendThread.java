package capstone.kookmin.silverwatchwear;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class WearSendThread extends Thread{
    Handler handler;
    String watchID;
    int wear;

    public WearSendThread(Handler handler, String watchID, int wear){
        this.handler = handler;
        this.watchID = watchID;
        this.wear = wear;
    }

    @Override
    public void run(){
        Message msg = new Message();
        try{
            String url = "http://203.246.112.155:5000/wear"; 	//URL
            HashMap<String, String> param = new HashMap<>();
            param.put("watch_id", this.watchID);
            param.put("wear", String.valueOf(this.wear));
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
