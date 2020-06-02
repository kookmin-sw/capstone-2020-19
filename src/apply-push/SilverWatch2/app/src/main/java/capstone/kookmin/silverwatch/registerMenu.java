package capstone.kookmin.silverwatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static java.security.AccessController.getContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.io.*;
import android.app.*;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class registerMenu extends AppCompatActivity {
    static RequestQueue requestQueue;
    private IntentIntegrator qrScan;
    private String watch_id;
    String myResult;
    EditText nameText;
    EditText phoneText;
    TextView watch_idText;
    TextView responseView;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu); // activity_register_menu와 연결

        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);
        watch_idText = findViewById(R.id.watch_idText);
        responseView = findViewById(R.id.responseView);

        qrScan = new IntentIntegrator(this);

        Button button = findViewById(R.id.button5); // 액티비티에서 추가한 '메인 메뉴로 돌아가기' 버튼 초기화
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼이 눌렸을 시에 작동하는 메서드
                Intent intent = new Intent(); // result 메시지를 보낼 인텐트 초기화
                setResult(RESULT_OK, intent); // 연결되었을 때 RESULT_OK 메시지를 인텐트에 저장
                finish();
            }
        });

        Button scanQRBtn = findViewById(R.id.scanQRbutton);
        scanQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });


        Button registerBtn = findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });
    }

    public void makeRequest() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        String url = "http://203.246.112.155:5000/set_watch_id";
        JSONObject object = new JSONObject();
        try {
            object.put("watch_id", watch_idText.getText().toString());
            object.put("name", nameText.getText().toString());
            object.put("phone_number", phoneText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    VolleyLog.v("Response:%n %s", response.toString(4));
                    responseView.setText("등록 성공" + response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "취소됨", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "스캔 완료", Toast.LENGTH_LONG).show();
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    watch_idText.setText(obj.getString("watch_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    watch_idText.setText(result.getContents().toString());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
