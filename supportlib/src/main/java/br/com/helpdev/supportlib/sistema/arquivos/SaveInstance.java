package br.com.helpdev.supportlib.sistema.arquivos;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by demantoide on 22/02/16.
 */
public class SaveInstance {
    private static final String LOG = "SaveInstance";

    public SaveInstance() {
        throw new RuntimeException("No SaveInstance!");
    }

    public static void saveInstance(Context context, String nameFile, Serializable persistir) {
        try {
            PersistenciaSerial persistenciaSerial = new PersistenciaSerial(context.getFilesDir(), nameFile);
            persistenciaSerial.persistir(persistir);
        } catch (Throwable e) {
            Log.e(LOG, "saveInstance", e);
        }
    }

    public static Serializable restoreInstance(Context context, String nameFile) {
        try {
            PersistenciaSerial persistenciaSerial = new PersistenciaSerial(context.getFilesDir(), nameFile);
            return (Serializable) persistenciaSerial.pegarObjeto();
        } catch (Throwable e) {
            Log.e(LOG, "restoreInstanceSerializable", e);
        }
        return null;
    }

    public static void deleteFiles(Context context, String nameFile) {
        try {
            PersistenciaSerial persistenciaSerial = new PersistenciaSerial(context.getFilesDir(), nameFile);
            persistenciaSerial.removerObjetos();
        } catch (Throwable e) {
            Log.e(LOG, "restoreInstanceSerializable", e);
        }
    }

}
