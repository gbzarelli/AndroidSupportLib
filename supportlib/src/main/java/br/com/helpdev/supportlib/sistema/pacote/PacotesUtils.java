package br.com.helpdev.supportlib.sistema.pacote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.io.File;

/**
 * Created by Guilherme Biff Zarelli on 16/03/16.
 */
public final class PacotesUtils {
    private PacotesUtils() {
        throw new IllegalArgumentException("No PacotesUtils");
    }

    public static void instalarPacote(Context context, File file) {
        int result = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        if (result == 0) {
            Intent itPermissao = new Intent();
            itPermissao.setAction(Settings.ACTION_APPLICATION_SETTINGS);
            context.startActivity(itPermissao);
        }
        Intent itInstall = new Intent(Intent.ACTION_VIEW);
        itInstall.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        itInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(itInstall);
    }
}
