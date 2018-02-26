package br.com.helpdev.supportlib.sistema.log;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by demantoide on 03/02/16.
 */
public class LogCatExtractor {
    private static final String LOG = "LogCatExtractor";
    public static final String PRIORITY_VERBOSE = "V";
    public static final String PRIORITY_DEBUG = "D";
    public static final String PRIORITY_INFO = "I";
    public static final String PRIORITY_WARNING = "W";
    public static final String PRIORITY_ERROR = "E";

    private LogCatExtractor() {
        throw new RuntimeException("No LogCatExtractor!");
    }

    public static File extractLogToFile(String packageName, String appName) {
        return extractLogToFile(Environment.getExternalStorageDirectory(), packageName, appName, PRIORITY_VERBOSE);
    }

    /**
     * @param dir
     * @param packageName context.getPackageName()
     * @param appName
     * @param priorityLog
     * @return
     */
    public static File extractLogToFile(File dir, String packageName, String appName, String priorityLog) {
        //set a file
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String fullName = appName.toUpperCase() + "_" + df.format(new Date()) + ".log";
        File file = new File(dir, fullName);
        try {
            if (!file.exists()) file.createNewFile();

            String command = String.format("logcat -d -v threadtime " + packageName + "*:" + priorityLog);
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String currentLine = null;

            while ((currentLine = reader.readLine()) != null) {
                result.append(currentLine);
                result.append("\n");
            }

            FileWriter out = new FileWriter(file, true);
            out.append(result.toString());
            out.flush();
            out.close();

            //clear the log
            try {
                Runtime.getRuntime().exec("logcat -c");
            } catch (IOException e) {
            }
        } catch (IOException e) {
            Log.e(LOG, "extractLog", e);
            file = null;
        }

        return file;
    }
}
