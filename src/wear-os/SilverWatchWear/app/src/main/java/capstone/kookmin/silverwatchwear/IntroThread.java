package capstone.kookmin.silverwatchwear;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends Thread {
    Handler handler;

    public IntroThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run(){
        Message msg = new Message();

        try{
            Thread.sleep(2000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
