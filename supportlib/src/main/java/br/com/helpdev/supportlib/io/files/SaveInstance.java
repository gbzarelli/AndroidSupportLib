package br.com.helpdev.supportlib.io.files;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Guilherme Biff Zarelli on 22/02/16.
 */
public class SaveInstance {
    private static final String LOG = "SaveInstance";

    private SaveInstance() {
        throw new RuntimeException("No SaveInstance!");
    }

    public static void saveInstance(Context context, String nameFile, Serializable persistir) {
        try {
            SerialPersistence persistenciaSerial = new SerialPersistence(context.getFilesDir(), nameFile);
            persistenciaSerial.persist(persistir);
        } catch (Throwable e) {
            Log.e(LOG, "saveInstance", e);
        }
    }

    public static Serializable restoreInstance(Context context, String nameFile) {
        try {
            SerialPersistence persistenciaSerial = new SerialPersistence(context.getFilesDir(), nameFile);
            return (Serializable) persistenciaSerial.getObject();
        } catch (Throwable e) {
            Log.e(LOG, "restoreInstanceSerializable", e);
        }
        return null;
    }

    public static void deleteFiles(Context context, String nameFile) {
        try {
            SerialPersistence persistenciaSerial = new SerialPersistence(context.getFilesDir(), nameFile);
            persistenciaSerial.removeObjects();
        } catch (Throwable e) {
            Log.e(LOG, "restoreInstanceSerializable", e);
        }
    }

}
