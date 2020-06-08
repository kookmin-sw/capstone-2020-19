package capstone.kookmin.silverwatchwear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.activity.WearableActivity;

public class IntroActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        IntroThread introThread = new IntroThread(handler);
        introThread.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                // TODO: 현재 등록된 정보 가져와서 등록 액티비티로 넘길지 등록 완료 액티비티로 넘길
                Intent intent = new Intent(IntroActivity.this, startRegister.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
