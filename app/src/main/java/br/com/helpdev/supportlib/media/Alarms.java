package br.com.helpdev.supportlib.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;

/**
 * Created by Guilherme Biff Zarelli on 06/07/16.
 */
public class Alarms {

    private static volatile boolean threadReproduzindo;

    public static void reproduzirAlarme(final Context context, final int timeMillis) {
        reproduzirAlarme(context, true, timeMillis);
    }

    public static void reproduzirAlarme(Context context, boolean vibrar, int timeMillis) {
        reproduzirAlarme(context, ToneGenerator.TONE_SUP_ERROR, vibrar, timeMillis);
    }

    /**
     * @param context
     * @param tone       android.media.ToneGenerator.TONE_
     * @param vibrar
     * @param timeMillis
     */
    public static void reproduzirAlarme(final Context context, final int tone, final boolean vibrar, final int timeMillis) {
        if (threadReproduzindo) return;
        threadReproduzindo = true;

        if (vibrar) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(timeMillis);
                }
            }).start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                tg.startTone(tone, timeMillis);
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
}
