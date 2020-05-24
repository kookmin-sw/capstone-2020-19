package capstone.kookmin.silverwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class registerMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu); // activity_register_menu와 연결

        Button button = findViewById(R.id.button5); // 액티비티에서 추가한 '메인 메뉴로 돌아가기' 버튼 초기화
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 버튼이 눌렸을 시에 작동하는 메서드
                Intent intent = new Intent(); // result 메시지를 보낼 인텐트 초기화
                setResult(RESULT_OK, intent); // 연결되었을 때 RESULT_OK 메시지를 인텐트에 저장
                finish();
            }
        });
    }
}
