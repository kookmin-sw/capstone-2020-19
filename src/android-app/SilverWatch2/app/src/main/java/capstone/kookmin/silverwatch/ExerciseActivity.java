package capstone.kookmin.silverwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    RecyclerView recyclerView;
    BatteryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BatteryAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnBatteryItemClickListener() {
            @Override
            public void onItemClick(BatteryAdapter.ViewHolder holder, View view, int position) {
                battery_info item = adapter.getItem(position);
                Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ item.phone_number));
                startActivity(myIntent);
            }
        });

        makeBatteryRequest();

        Button button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void makeBatteryRequest() {
        String url = "http://203.246.112.155:5000/battery_all";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답-> " + response);

                        processBatteryResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러-> " + error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄");
    }

    public void println(String data) {
        Log.d("TracerActivity", data);
    }

    public void processBatteryResponse(String response) {
        Gson gson = new Gson();
        batteryResult battery_list = gson.fromJson(response, batteryResult.class);
        println("배터리 데이터 수 : " + battery_list.result.size());

        for (int i = 0; i < battery_list.result.size(); i++) {
            battery_info batteryInfo = battery_list.result.get(i);

            adapter.addItem(batteryInfo);
        }

        adapter.notifyDataSetChanged();
    }
}
