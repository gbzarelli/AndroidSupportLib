package br.com.grupocriar.swapandroid.io.files;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author Guilherme Biff Zarelli
 */
public class SerialPersistence<T> {

    private static final String LOG = "SerialPersistence";
    private static final String FILE_EXT_1 = ".1";
    private static final String FILE_EXT_2 = ".2";
    private static final String FILE_EXT_BKP = ".BKP";

    private File path;
    private String name;

    public SerialPersistence(File path, String name) {
        this.path = path;
        this.name = name;
    }

    private File getFileWrite() throws Exception {
        File file1 = new File(this.path, name + FILE_EXT_1);
        File file2 = new File(this.path, name + FILE_EXT_2);

        if (file1.exists() && file1.lastModified() > new Date().getTime()) {
            file1.delete();
        }
        if (file2.exists() && file2.lastModified() > new Date().getTime()) {
            file2.delete();
        }

        if (!file1.exists()) {
            file1.createNewFile();
            return file1;
        }

        if (!file2.exists()) {
            file2.createNewFile();
            return file2;
        }


        if (file1.lastModified() < file2.lastModified()) {
            return file1;
        } else {
            return file2;
        }
    }

    public boolean persist(T objeto) throws Exception {
        if (objeto == null) {
            Log.w(LOG, "OBJETO A SER GRAVADO NULLO, RETORNO SEM GRAVAÇÃO");
            return false;
        }
        File file = getFileWrite();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(objeto);
        oos.close();


        File bkp = new File(this.path, name + FILE_EXT_BKP);
        if (!bkp.exists()) {
            Log.d(LOG, "GRAVANDO ARQUIVO DE BKP");
            fos = new FileOutputStream(bkp);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(objeto);
            oos.close();
        }

        return true;
    }

    public T getObject() throws Exception {
        File file1 = new File(this.path, name + FILE_EXT_1);
        File file2 = new File(this.path, name + FILE_EXT_2);
        File file3 = new File(this.path, name + FILE_EXT_BKP);
        if (file1.lastModified() >= file2.lastModified()) {
            return loadFile(file1, file2, file3);
        }

        return loadFile(file2, file1, file3);
    }

    private T loadFile(File... files) {
        for (File file : files) {
            if (!file.exists()) {
                Log.w(LOG, "Arquivo inexistente {" + file.getName() + "}");
                continue;
            }
            try {
                T ob = load(file);
                if (ob != null) {
                    return ob;
                }
            } catch (Exception e) {
                Log.e(LOG, "loadFile", e);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private T load(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        }
    }

    public void removeObjects() throws Exception {
        File file1 = new File(this.path, name + FILE_EXT_1);
        File file2 = new File(this.path, name + FILE_EXT_2);
        File file3 = new File(this.path, name + FILE_EXT_BKP);
        file1.delete();
        file2.delete();
        file3.delete();
    }
}
