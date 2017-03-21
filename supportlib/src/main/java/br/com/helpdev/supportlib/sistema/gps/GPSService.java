package br.com.helpdev.supportlib.sistema.gps;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gbzarelli on 3/21/17.
 */

public class GPSService extends Service implements Runnable {

    public interface VerificadorGPS {
        public void isGpsEnable(boolean enable);

        public void updateStatusGPS(int status, String msg);
    }

    public interface PosicoesGpsIO {
        public void onUpdatePosicoesGPS(android.location.Location location);
    }

    private static final String LOG = "ServicoGPS";

    public static final int STATUS_ATUALIZADO = 1;
    public static final int STATUS_DESATUALIZADO = 2;
    public static final int STATUS_NAO_ENCONTRADO = 3;
    public static final int STATUS_DESATIVADO = 4;
    public static final int STATUS_BUSCANDO = 5;

    private static final int TEMPO_ATUALIZACAO_DEFAULT = 5;

    public class LocalBinder extends Binder {
        public GPSService getService() {
            return GPSService.this;
        }
    }

    private final AtomicBoolean rodando = new AtomicBoolean(false);
    private final IBinder mBinder = new LocalBinder();
    private GPS gps;
    private Location location;
    private int status;
    private VerificadorGPS verificadorGps;
    private PosicoesGpsIO posicoesGpsIO;
    private int tempoAtualizacao;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!rodando.get()) {
            iniciarGPS();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void iniciarGPS() {
        setTempoAtualizacao(TEMPO_ATUALIZACAO_DEFAULT);
        if (gps == null) {
            gps = new GPS(this);
        }
        rodando.set(true);
        new Thread(this).start();
    }

    @Override
    public void onDestroy() {
        verificadorGps = null;
        posicoesGpsIO = null;
        rodando.set(false);
        super.onDestroy();
    }

    @Override
    public void run() {

        while (!gps.isGpsEnable()) {
            status = STATUS_DESATIVADO;
            if (verificadorGps != null) {
                verificadorGps.isGpsEnable(false);
            }
            try {
                Thread.sleep(10_000);
            } catch (Throwable e) {
            }
        }

        status = STATUS_BUSCANDO;
        while (rodando.get()) {
            if (gps.isGpsEnable()) {
                if (verificadorGps != null) {
                    verificadorGps.isGpsEnable(true);
                }
                location = this.gps.getLocation();
                if (location != null) {
                    if (this.gps.getViews() <= 1) {
                        if (verificadorGps != null) {
                            verificadorGps.updateStatusGPS(STATUS_ATUALIZADO, "GPS: Sinal encontrado.");
                        }
                        status = STATUS_ATUALIZADO;

                        if (posicoesGpsIO != null) {
                            try {
                                posicoesGpsIO.onUpdatePosicoesGPS(location);
                            } catch (Throwable ex) {
                                Log.e(LOG, "Throwable", ex);
                            }
                        }
                    } else {
                        if (verificadorGps != null) {
                            verificadorGps.updateStatusGPS(STATUS_DESATUALIZADO, "GPS: Sinal desatualizado");
                        }
                        status = STATUS_DESATUALIZADO;
                    }
                } else {
                    if (verificadorGps != null) {
                        verificadorGps.updateStatusGPS(STATUS_NAO_ENCONTRADO, "GPS: Sinal não encontrado");
                    }
                    status = STATUS_NAO_ENCONTRADO;
                }

            } else {
                if (verificadorGps != null) {
                    verificadorGps.isGpsEnable(false);
                }
                status = STATUS_DESATIVADO;
                rodando.set(false);
            }
            try {
                Thread.sleep(tempoAtualizacao * 1000);
            } catch (Throwable e) {
            }
        }
        if (status == STATUS_DESATIVADO) {
            iniciarGPS();
        } else {
            gps.close();
            stopSelf();
        }
    }

    public void setTempoAtualizacao(int tempoAtualizacao) {
        this.tempoAtualizacao = tempoAtualizacao;
    }

    public boolean isRodando() {
        return rodando.get();
    }

    public void setRodando(boolean rodando) {
        this.rodando.set(rodando);
    }

    public int getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public void setPosicoesGpsIO(PosicoesGpsIO posicoesGpsIO) {
        this.posicoesGpsIO = posicoesGpsIO;
    }

    public void setVerificadorGPS(VerificadorGPS verificadorGPS) {
        verificadorGps = verificadorGPS;
    }

}