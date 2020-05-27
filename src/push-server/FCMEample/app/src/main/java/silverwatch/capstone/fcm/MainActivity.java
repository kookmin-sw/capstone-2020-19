package silverwatch.capstone.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import android.support.annotation.NonNull;
//import android.support.v4.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
