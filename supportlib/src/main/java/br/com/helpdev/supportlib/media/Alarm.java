package br.com.grupocriar.swapandroid.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Vibrator;

/**
 * Created by demantoide on 06/07/16.
 */
public class Alarm {

    private static volatile boolean threadReproduzindo;

    public static void playAlarm(final Context context, final int timeMillis) {
        if (threadReproduzindo) return;
        threadReproduzindo = true;
        new Thread(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(timeMillis);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                tg.startTone(ToneGenerator.TONE_SUP_ERROR, timeMillis);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeMillis + 2_000);
                    threadReproduzindo = false;
                } catch (Throwable e) {
                }
            }
        }).start();
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
