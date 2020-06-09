package capstone.kookmin.silverwatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.InstanceIdResult;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    public static final int REQUEST_CODE_MENU = 101; // REQUEST_CODE_MENU 초기화 (임의 설정 가능)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // 새로 띄웠던 메뉴들에서 응답을 보내오면 그 응답을 처리하는 역할을 하는 메서드
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU) {
            Toast.makeText(getApplicationContext(), "onActivityResult 메서드 호출됨. 요청 코드 : "
            + requestCode + ", 결과 코드 : " + resultCode, Toast.LENGTH_LONG).show();

            if (requestCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "응답 성공", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        Button registerButton = findViewById(R.id.button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), registerMenu.class);
                // 인테트 초기화 후에 registerMenu와 연결
                startActivityForResult(intent, REQUEST_CODE_MENU);
                // 어떤 액티비티에 갔다가 돌아오는지를 처리할 수 있는 startActivityForResult 함수 사용
            }
        });

        Button settingButton = findViewById(R.id.button2);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), settingActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        Button exerciseButton = findViewById(R.id.button3);
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        Button tracerButton = findViewById(R.id.button4);
        tracerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TracerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.w("FCM Log", "getInstanceID failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰 : "+ token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, @NonNull String[] permissions) {
        Toast.makeText(this, "Permissions Denied : " + permissions.length,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, @NonNull String[] permissions) {
        Toast.makeText(this, "Permissions Granted : " + permissions.length,
                Toast.LENGTH_LONG).show();
    }

}
