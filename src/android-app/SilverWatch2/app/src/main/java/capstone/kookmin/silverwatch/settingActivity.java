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

public class settingActivity extends AppCompatActivity {
    static RequestQueue requestQueue;
    RecyclerView recyclerView;
    WearAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new WearAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnWearItemClickListener() {
            @Override
            public void onItemClick(WearAdapter.ViewHolder holder, View view, int position) {
                wear_info item = adapter.getItem(position);
                Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ item.phone_number));
                startActivity(myIntent);
            }
        });

        makeWearRequest();

        Button button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void makeWearRequest() {
        String url = "http://203.246.112.155:5000/wear_all";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답-> " + response);

                        processWearResponse(response);
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
        Log.d("WearActivity", data);
    }

    public void processWearResponse(String response) {
        Gson gson = new Gson();
        wearResult wear_list = gson.fromJson(response, wearResult.class);
        println("배터리 데이터 수 : " + wear_list.result.size());

        for (int i = 0; i < wear_list.result.size(); i++) {
            wear_info wearInfo = wear_list.result.get(i);

            adapter.addItem(wearInfo);
        }

        adapter.notifyDataSetChanged();
    }
}
