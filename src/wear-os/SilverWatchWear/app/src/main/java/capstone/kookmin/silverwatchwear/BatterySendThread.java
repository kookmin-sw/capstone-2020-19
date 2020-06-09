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
            Log.d("value", this.watchID + this.percentage);
            param.put("watch_id", this.watchID);
            param.put("watch_battery", this.percentage);
            String json = new JSONObject(param).toString();
            Log.d("json", json);
            String res = sendPost(url, json);
            Log.d("result", res);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String postRequest(String pURL, HashMap< String, String > pList) {

        String myResult = "";

        try {
            //   URL 설정하고 접속하기
            URL url = new URL(pURL); // URL 설정

            HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
            //--------------------------
            //   전송 모드 설정 - 기본적인 설정
            //--------------------------
            http.setDefaultUseCaches(false);
            http.setDoInput(true); // 서버에서 읽기 모드 지정
            http.setDoOutput(true); // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST"); // 전송 방식은 POST



            //--------------------------
            // 헤더 세팅
            //--------------------------
            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");


            //--------------------------
            //   서버로 값 전송
            //--------------------------
            StringBuffer buffer = new StringBuffer();

            //HashMap으로 전달받은 파라미터가 null이 아닌경우 버퍼에 넣어준다
            if (pList != null) {

                Set key = pList.keySet();

                for (Iterator iterator = key.iterator(); iterator.hasNext();) {
                    String keyName = (String) iterator.next();
                    String valueName = pList.get(keyName);
                    buffer.append(keyName).append(":").append(valueName);
                }
            }

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }
            myResult = builder.toString();
            return myResult;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myResult;
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
