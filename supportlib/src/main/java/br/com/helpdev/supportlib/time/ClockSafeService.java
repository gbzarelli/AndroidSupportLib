package br.com.grupocriar.swapandroid.time;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Guilherme Biff Zarelli on 12/7/17.
 */

public class ClockSafeService extends Service implements Runnable {

    private static final String LOG = "ClockSafeService";

    public interface ClockSafeListener {
        void onUpdateClock(long timestamp);
    }

    public class LocalBinder extends Binder {
        private ClockSafeService clockSafeService;

        LocalBinder(ClockSafeService clockSafeService) {
            this.clockSafeService = clockSafeService;
        }

        private ClockSafeService getService() {
            return clockSafeService;
        }

        public void setUpdateListener(ClockSafeListener clockSafeListener) {
            getService().setListener(clockSafeListener);
        }
    }

    private final IBinder myBinder = new LocalBinder(this);
    private ClockSafeListener listener;
    private volatile boolean threadRunning;


    public void setListener(ClockSafeListener listener) {
        Log.i(LOG, "new Listener: " + listener);
        this.listener = listener;
        if (this.listener == null) {
            threadRunning = false;
        } else {
            if (!threadRunning) {
                threadRunning = true;
                new Thread(this, ClockSafeService.class.getName() + "-THREAD").start();
            }
        }
    }


    @Override
    public void run() {
        Log.i(LOG, "Thread running");
        while (threadRunning) {
            if (this.listener == null) {
                threadRunning = false;
                return;
            }
            listener.onUpdateClock(ClockSafe.getCurrentDateSafeMillis());
            try {
                Thread.sleep(1000);
            } catch (Throwable t) {
            }
        }
        Log.i(LOG, "Thread finish.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG, "onBind");
        return myBinder;
    }


    @Override
    public void onDestroy() {
        Log.i(LOG, "onDestroy");
        threadRunning = false;
        listener = null;
        super.onDestroy();
    }
}
