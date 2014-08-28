package net.grzechocinski.android.examples.clipdrawable;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import net.grzechocinski.android.examples.domain.Percent;

public class VerticalProgressBar extends ImageView {

    private static final String TAG = "VerticalProgressBar";
    /**
     * @see <a href="http://developer.android.com/reference/android/graphics/drawable/ClipDrawable.html">ClipDrawable</a>
     */
    private static final BigDecimal MAX = BigDecimal.valueOf(10000);
    private int finalLevel = 0;
    private int currentLevel = 0;
    protected boolean locked = false; 
    private static int offset = 500;
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0x1233) { // down
                // 修改ClipDrawable的level值
                setImageLevel(currentLevel);
            }else if(msg.what == 2){ // up
                setImageLevel(currentLevel);
            }
        }
    };
    

     public VerticalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setImageResource(R.drawable.progress_bar);
    }

    public void setCurrentValue(Percent percent){
        if(locked) return;
        locked = true;
        finalLevel = percent.asBigDecimal().multiply(MAX).intValue();
//        setImageLevel(cliDrawableImageLevel);
        Log.d(TAG, "setCurrentValue,percent|finalLeval:"+percent + "|" + finalLevel);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            boolean downing = true;
            public void run() {
                Message msg = handler.obtainMessage();
                if(downing){
                    if(currentLevel <= 0){
                        currentLevel = 0;
                        downing = false;
                    }else{
                        currentLevel -= offset;
                        // 发送消息,通知应用修改ClipDrawable对象的level值
                    }
                    msg.what = 0x1233;
                    handler.sendMessage(msg);
                }else{//up
                    if(currentLevel >= finalLevel){
                        currentLevel = finalLevel;
                        timer.cancel();
                        locked = false;
                    }else{
                        currentLevel += offset;
                    }
                    msg.what = 2;
                    handler.sendMessage(msg);
                }

            }
        }, 0, 100);
        
    }
}
