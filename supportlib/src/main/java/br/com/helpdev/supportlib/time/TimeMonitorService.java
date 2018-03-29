package br.com.helpdev.supportlib.time;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by felipe on 09/03/17.
 */
@Deprecated
public class TimeMonitorService extends Service {

    private static final int TEMPO_VERIFICACAO_HORA = 5;

    private static final String LOG = "TimeMonitorService";
    private static final String TEMPO_ANTERIOR = "TEMPO_ANTERIOR";

    private Date dataAtual;
    private static final AtomicBoolean thread = new AtomicBoolean(false);
    private Bundle params;
    private TimeMonitorListener listener;
    private final IBinder localBinder = new LocalBinder(this);

    public class LocalBinder extends Binder {
        private TimeMonitorService service;

        public LocalBinder(TimeMonitorService service) {
            this.service = service;
        }

        public TimeMonitorService getService() {
            return service;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        dataAtual = new Date();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iniciarThreadMonitoramento();
        return super.onStartCommand(intent, flags, startId);
    }

    private void iniciarThreadMonitoramento() {
        if (thread.get()) {
            return;
        }
        thread.set(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if ((System.currentTimeMillis() - getParams().getLong(TEMPO_ANTERIOR, 0)) >=
                                TimeUnit.MINUTES.toMillis(TEMPO_VERIFICACAO_HORA)) {
                            getParams().putLong(TEMPO_ANTERIOR, System.currentTimeMillis());
                            Date agora = new Date();
                            if (agora.getTime() < dataAtual.getTime()) {
                                if (listener != null) {
                                    listener.onChangeDetected(true, dataAtual.getTime() - agora.getTime());
                                }
                            } else {
                                long t = agora.getTime() - dataAtual.getTime();
                                if (TimeUnit.MILLISECONDS.toMinutes(t) > TEMPO_VERIFICACAO_HORA) {
                                    if (listener != null) {
                                        listener.onChangeDetected(false, t);
                                    }
                                }
                            }
                            dataAtual = new Date();
                        }

                        Thread.sleep(1_000);
                    }
                } catch (Throwable t) {
                    Log.e(LOG, "erro run servico de monitoramento de tempo", t);
                }
            }

        }, "iniciarThreadMonitoramento").start();
    }

    public void setListener(TimeMonitorListener listener) {
        this.listener = listener;
    }

    private Bundle getParams() {
        if (params == null) {
            params = new Bundle();
        }
        return params;
    }

    public interface TimeMonitorListener {

        void onChangeDetected(boolean back, long milis);

    }

}
