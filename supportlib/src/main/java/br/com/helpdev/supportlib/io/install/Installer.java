package br.com.grupocriar.swapandroid.io.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.Settings;

import java.io.File;

/**
 * Created by demantoide on 16/03/16.
 */
public final class Installer {
    private Installer() {
        throw new IllegalArgumentException("No Installer");
    }

    public static void install(Context context, File file) throws Throwable {
        int result = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        if (result == 0) {
            Intent itPermissao = new Intent();
            itPermissao.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            itPermissao.setAction(Settings.ACTION_APPLICATION_SETTINGS);
            context.startActivity(itPermissao);
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent itInstall = new Intent(Intent.ACTION_VIEW);
        itInstall.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        itInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(itInstall);
    }
}
