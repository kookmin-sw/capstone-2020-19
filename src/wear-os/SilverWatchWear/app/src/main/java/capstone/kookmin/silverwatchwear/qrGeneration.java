package capstone.kookmin.silverwatchwear;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

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
    SQLiteDatabase registerDB;

    boolean registered = false;
    String uuid;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 1){
                registerDB.execSQL("INSERT INTO result VALUES (1)");
                Intent intent = new Intent(qrGeneration.this, registerFinished.class);
                intent.putExtra("watchID", uuid);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generation);
        setAmbientEnabled();

        initialization();

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");
        Log.d("generationuuid", uuid);
        registerDB = this.openOrCreateDatabase("register", MODE_PRIVATE, null);
        generateQRCode(uuid);


        CheckRegisterThread checkRegisterThread = new CheckRegisterThread(handler, uuid);
        checkRegisterThread.start();
    }

    public void initialization(){
        qrImage = (ImageView) findViewById(R.id.QR_Image);
    }

    public void generateQRCode(String inputValue){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        assert manager != null;
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
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
}
