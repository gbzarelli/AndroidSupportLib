package br.com.grupocriar.swapandroid.time;

import android.os.SystemClock;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Guilherme Biff Zarelli on 12/7/17.
 */

public class ClockSafe {

    private static final AtomicBoolean SAFE = new AtomicBoolean(false);
    private static final AtomicLong BASE_DATE = new AtomicLong(new Date().getTime());
    private static final AtomicLong BASE_UP_TIME = new AtomicLong(SystemClock.uptimeMillis());

    private ClockSafe() {
        throw new IllegalArgumentException("No ClockSafe");
    }

    public static void setBaseDate(long baseDate) {
        SAFE.set(true);
        ClockSafe.BASE_DATE.set(baseDate);
        ClockSafe.BASE_UP_TIME.set(SystemClock.uptimeMillis());
    }

    public static boolean isSafe() {
        return SAFE.get();
    }

    public static long getBaseDate() {
        return BASE_DATE.get();
    }

    public static long getBaseUpTime() {
        return BASE_UP_TIME.get();
    }

    public static Date getCurrentDateSafe() {
        return new Date(getCurrentDateSafeMillis());
    }

    public static long getCurrentDateSafeMillis() {
        return BASE_DATE.get() + (SystemClock.uptimeMillis() - BASE_UP_TIME.get());
    }

}
