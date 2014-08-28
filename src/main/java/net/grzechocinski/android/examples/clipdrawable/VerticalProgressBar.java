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
                Message msg = handler.obtainMessage(0x1233);
                if(downing){
                    if(currentLevel <= 0){
                        currentLevel = 0;
                        downing = false;
                    }else{
                        currentLevel -= offset;
                    }
                }else{//up
                    if(currentLevel >= finalLevel){
                        currentLevel = finalLevel;
                        timer.cancel();
                        locked = false;
                    }else{
                        currentLevel += offset;
                    }
                }
                handler.sendMessage(msg);
            }
        }, 0, 50);
        
    }
}
