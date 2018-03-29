package br.com.grupocriar.swapandroid.pm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demantoide on 10/03/16.
 */
public final class PackageUtils {
    private PackageUtils() {
        throw new IllegalArgumentException("No PacotesUtils");
    }

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

    public static boolean isPackageExisted(PackageManager packageManager, String targetPackage, int flags) {
        try {
            packageManager.getApplicationInfo(targetPackage, flags);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
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

    public static List<ApplicationInfo> getInstalledApplications(Context context) {
        List<ApplicationInfo> lista = context.getPackageManager().getInstalledApplications(0);
        List<ApplicationInfo> retorno = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo ai : lista) {
            if (isUserApp(ai)) {
                retorno.add(ai);
            }
        }
        return retorno;
    }

    private static boolean isUserApp(ApplicationInfo ai) {
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (ai.flags & mask) == 0;
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcesses(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses();
    }

    public static String getVersion(Context context, String myPackage) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(myPackage, 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
