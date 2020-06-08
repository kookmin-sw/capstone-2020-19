package capstone.kookmin.silverwatchwear;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

public class registerFinished extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finished);
        setAmbientEnabled();
    }
}
