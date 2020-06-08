package capstone.kookmin.silverwatchwear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrGeneration extends WearableActivity {
    String TAG = "GenerateQRCode";
    EditText edtValue;
    ImageView qrImage;
    Button start, save;
    String inputValue;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    RequestQueue requestQueue;
    boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generation);

        initialization();
    }

    public void waitForRegister(final String uuid){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                submitData(uuid);
                Log.d("data", String.valueOf(registered));
                if (registered) {
                    Intent intent = new Intent(qrGeneration.this, registerFinished.class);
                    Log.d("Intent", "Reg Completed");

                    startActivity(intent);
                    finish();
                }
            }
        }, 15000);
    }

    public void initialization(){
        qrImage = (ImageView) findViewById(R.id.QR_Image);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String tableName = "id";
        SQLiteDatabase idDB = this.openOrCreateDatabase("ID", MODE_PRIVATE, null);
        SQLiteDatabase registerDB = this.openOrCreateDatabase("register", MODE_PRIVATE, null);

//        idDB.execSQL("DROP TABLE IF EXISTS " + tableName);
//        registerDB.execSQL("DROP TABLE IF EXISTS result");

        idDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid text);");
        registerDB.execSQL("CREATE TABLE IF NOT EXISTS result (register integer)");

        Cursor c = idDB.rawQuery("SELECT * FROM " + tableName, null);
        int rowCount = c.getCount();
        Log.d("db", "Row Count: " + rowCount);

        if (rowCount == 0){
            String uuid = UUID.randomUUID().toString();
            generateQRCode(uuid);
            idDB.execSQL("INSERT INTO " + tableName + " VALUES ('" + uuid + "');");
            waitForRegister(uuid);
        }else{
            c.moveToFirst();
            Cursor registerCursor = registerDB.rawQuery("SELECT * FROM result", null);
            int registerRowCount = registerCursor.getCount();
            if (registerRowCount == 0) {
                String uuid = c.getString(c.getColumnIndex("uuid"));
                generateQRCode(uuid);
                waitForRegister(uuid);
            }else{
                Intent intent = new Intent(qrGeneration.this, registerFinished.class);
                Log.d("Intent", "Reg Completed");

                startActivity(intent);
                finish();
            }
            registerCursor.close();
        }
    }

    public void generateQRCode(String inputValue){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }


    // TODO: Change to POST
    public void submitData(String data){
        String url = "http://203.246.112.155:5000/check_watch_id?watch_id="+data;

        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("data", response.toString());
                        try {
                            if (response.getInt("register_result") == 1){
                                Log.d("data", "registered");
                                registered = true;
                                Intent intent = new Intent(qrGeneration.this, registerFinished.class);
                                Log.d("Intent", "Reg Completed");

                                startActivity(intent);
                                finish();
                            }else{
                                Log.d("data", "not registered");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
