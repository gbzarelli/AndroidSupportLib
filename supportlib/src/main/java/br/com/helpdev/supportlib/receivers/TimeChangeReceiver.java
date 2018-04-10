package br.com.helpdev.supportlib.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

/**
 * <p>
 * Created by Guilherme Biff Zarelli on 12/20/16.
 */
public class TimeChangeReceiver extends BroadcastReceiver {

    private static TimeChangeReceiver tcr;
    public static final long DIFF_TIME_TO_AUTOMATIC_CHANGE = 60_000;

    public interface Callback {

        void automaticTimeChange(String action, long diff);

        void timeChange(String action, long diff);

        void timeZoneChange(String action, long diff);
    }

    public static TimeChangeReceiver registerBroadcast(Context context, Callback callback) {
        if (tcr == null) {
            tcr = new TimeChangeReceiver();

            IntentFilter s_intentFilter = new IntentFilter();
            s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            context.registerReceiver(tcr, s_intentFilter);
        }
        tcr.setCallback(callback);

        return tcr;
    }

    public static void unregisterBroadcast(Context context) {
        if (tcr == null) return;
        tcr.setCallback(null);
        try {
            context.unregisterReceiver(tcr);
        } catch (Throwable t) {
        }
        tcr = null;
    }

    private Callback callback;
    private long upTime;
    private long currentTime;
    private long diffTimeToAutomaticChange = DIFF_TIME_TO_AUTOMATIC_CHANGE;


    public void setDiffTimeToAutomaticChange(long diffTimeToAutomaticChange) {
        this.diffTimeToAutomaticChange = diffTimeToAutomaticChange;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
        updateTimes();
    }

    private void updateTimes() {
        this.upTime = SystemClock.uptimeMillis();
        this.currentTime = System.currentTimeMillis();
    }

    public static TimeChangeReceiver getTimeChangeRegister() {
        return tcr;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (callback != null && intent != null) {

                long diffUpTime = SystemClock.uptimeMillis() - upTime;
                long diffCurrentTime = System.currentTimeMillis() - currentTime;
                long timeDiff = diffCurrentTime - diffUpTime;

                if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
                    if (timeDiff > diffTimeToAutomaticChange || timeDiff < (diffTimeToAutomaticChange * -1)) {
                        callback.timeChange(intent.getAction(), timeDiff);
                    } else {
                        callback.automaticTimeChange(intent.getAction(), timeDiff);
                    }
                } else if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                    callback.timeZoneChange(intent.getAction(), timeDiff);
                }
            }
        } finally {
            updateTimes();
        }
    }
}
