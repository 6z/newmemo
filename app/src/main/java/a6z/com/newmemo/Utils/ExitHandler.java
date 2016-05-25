package a6z.com.newmemo.Utils;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 应用延迟退出处理
 */
public class ExitHandler {

    private final int TimesNeeded = 2;
    private final int MaxWaitingSeconds = 3;
    private Timer mTimer;
    private int mCurTimes;
    private ConfirmRequestListener mListener;
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mListener != null) {
                        mListener.onExitCancelled();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public ExitHandler(ConfirmRequestListener listener) {
        mListener = listener;
    }

    public void exit() {
        mCurTimes++;
        if (mCurTimes < TimesNeeded) {
            if (mTimer == null) {
                if (mListener != null) {
                    mListener.onWaitForExitConfirm();
                }
                mTimer = new Timer(true);
                final TimerTask timerTask = new TimerTask() {
                    public void run() {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        mCurTimes = 0;
                        mTimer.cancel();
                        mTimer = null;
                    }
                };
                mTimer.schedule(timerTask, MaxWaitingSeconds * 1000);
            }
        } else {
            if (mListener != null) {
                mListener.onBeforeExit();
            }
            System.exit(0);
        }
    }

    public interface ConfirmRequestListener {
        void onWaitForExitConfirm();

        void onBeforeExit();

        void onExitCancelled();
    }
}
