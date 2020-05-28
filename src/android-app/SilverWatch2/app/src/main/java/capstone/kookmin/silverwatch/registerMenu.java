package capstone.kookmin.silverwatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.EditText;
import android.widget.Toast;
// import static java.security.AccessController.getContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.io.*;
import android.app.*;

public class registerMenu extends AppCompatActivity {
    private IntentIntegrator qrScan;
    private String watch_id;
    EditText nameText;
    EditText phoneText;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu); // activity_register_menu와 연결

        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request("https://2ouujsem46.execute-api.ap-northeast-2.amazonaws.com/demo/user");
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "취소됨", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "스캔 완료", Toast.LENGTH_LONG).show();
                watch_id = result.toString();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void request(String urladdress) {
        try {
            URL url = new URL("https://2ouujsem46.execute-api.ap-northeast-2.amazonaws.com/demo/user");
            HttpURLConnection httpURLCon = (HttpURLConnection)url.openConnection();

            httpURLCon.setDefaultUseCaches(false);
            httpURLCon.setDoInput(true);
            httpURLCon.setDoOutput(true);
            httpURLCon.setRequestMethod("POST");
            httpURLCon.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            StringBuffer sb = new StringBuffer();
            sb.append("watch_id").append("=").append(watch_id.toString()).append("&");
            sb.append("name").append("=").append(nameText.getText().toString()).append("&");
            sb.append("phone_number").append("=").append(phoneText.getText().toString()).append("&");

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(httpURLCon.getOutputStream(), "EUC-KR"));
            pw.write(sb.toString());
            pw.flush();

            BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLCon.getInputStream(), "EUC-KR"));
            StringBuilder buff = new StringBuilder();
            String line;

            while(true) {
                line = bf.readLine();
                if(line == null) {
                    break;
                }
                buff.append(line + "\n");
            }
            bf.close();
            httpURLCon.disconnect();
        } catch (Exception ex) {
            System.out.println("예외 발생함: " + ex.toString());
        }
    }
}
