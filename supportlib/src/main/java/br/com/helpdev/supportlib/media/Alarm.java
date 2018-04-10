package br.com.helpdev.supportlib.media;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.RequiresPermission;

/**
 * Created by Guilherme Biff Zarelli on 06/07/16.
 */
public class Alarm {

    private static volatile boolean threadReproduzindo;

    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void playAlarm(final Context context, final int timeMillis) {
        if (threadReproduzindo) return;
        threadReproduzindo = true;
        new Thread("Vibrator") {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (null != vibrator) vibrator.vibrate(timeMillis);
            }
        }.start();
        new Thread("ToneGenerator") {
            @Override
            public void run() {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                tg.startTone(ToneGenerator.TONE_SUP_ERROR, timeMillis);
            }
        }.start();
        new Thread("LoopPlayAlarm") {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeMillis + 2_000);
                    threadReproduzindo = false;
                } catch (Throwable e) {
                }
            }
        }.start();
    }

    @Deprecated
    public static void playNotification(final Context context) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
