package br.com.helpdev.supportlib.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @deprecated Ajustar classe para retirar o context da variavel de classe;
 * <p>
 * Created by Guilherme Biff Zarelli on 12/20/16.
 */
@Deprecated
public class TimeChangeReceiver extends BroadcastReceiver {

    private static TimeChangeReceiver tcr;

    public interface Callback {
        void timeChange(String action);
    }


    public static TimeChangeReceiver registerBroadcast(Context context) {
        return registerBroadcast(context, null);
    }

    public static TimeChangeReceiver registerBroadcast(Context context, Callback callback) {
        if (tcr != null) {
            unregisterBroadcast();
        }

        tcr = new TimeChangeReceiver();
        tcr.setCallback(callback);
        tcr.context = context;

        IntentFilter s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

        context.registerReceiver(tcr, s_intentFilter);
        return tcr;
    }

    public static void unregisterBroadcast() {
        if (tcr == null) return;
        tcr.setCallback(null);
        tcr.context.unregisterReceiver(tcr);
        tcr = null;
    }

    private Callback callback;
    private Context context;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static TimeChangeReceiver getTimeChangeRegister() {
        return tcr;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (callback != null) {
            callback.timeChange(intent.getAction());
        }
    }
}
