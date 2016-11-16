package br.com.helpdev.supportlib.sistema.pakage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guilherme Biff Zarelli on 10/03/16.
 */
public final class PackageUtils {

    /**
     * @param it              Exemplo de intent: (Retorna aplicações com ACTION_MAIN e categoria LAUNCHER(executavel))
     *                        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
     *                        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
     * @param containsPackage Pega os ApplicationInfo se contem o parametro passado no package das aplicações
     * @return List<ApplicationInfo>
     * @throws PackageManager.NameNotFoundException
     */
    public static List<ApplicationInfo> queryIntentActivities(Context context, Intent it, String containsPackage) throws PackageManager.NameNotFoundException {
        List<ResolveInfo> lista = context.getPackageManager().queryIntentActivities(it, 0);
        List<ApplicationInfo> retorno = new ArrayList<ApplicationInfo>();
        for (ResolveInfo ri : lista) {
            if (ri.activityInfo.packageName.contains(containsPackage)) {
                retorno.add(context.getPackageManager().getApplicationInfo(ri.activityInfo.packageName, 0));
            }
        }
        return retorno;
    }

    /**
     * @param containsPackage Pega os ApplicationInfo se contem o parametro passado no package das aplicações
     * @return List<ApplicationInfo>
     */
    public static List<ApplicationInfo> getInstalledApplications(Context context, String containsPackage) {
        List<ApplicationInfo> lista = context.getPackageManager().getInstalledApplications(0);
        List<ApplicationInfo> retorno = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo apinf : lista) {
            if (apinf.packageName.contains(containsPackage)) {
                retorno.add(apinf);
            }
        }
        return retorno;
    }

    public static String getApplicationName(Context context, ApplicationInfo applicationInfo) {
        return context.getPackageManager().getApplicationLabel(applicationInfo).toString();
    }

    public static Drawable getApplicationIcon(Context context, ApplicationInfo applicationInfo) {
        return context.getPackageManager().getApplicationIcon(applicationInfo);
    }

    public static Intent getLaunchIntent(Context context, ApplicationInfo applicationInfo) {
        return context.getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName);
    }
}
