package br.com.helpdev.supportlib.sistema;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

/**
 * Created by demantoide on 17/03/16.
 */
public class RuntimeAndroid {
    private RuntimeAndroid() {
        throw new RuntimeException("No RuntimeAndroid!");
    }

    public static Process getProcess(String comando) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        return runtime.exec(comando);
    }


    public static void backToMain(Context context, Bundle params, Class mainClass) {
        Intent mStartActivity = new Intent(context, mainClass);
        if (params != null) {
            mStartActivity.putExtras(params);
        }

        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 5_000, mStartActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, mPendingIntent);

        killApp();
    }

    public static void killApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}
