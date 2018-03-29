package br.com.helpdev.supportlib.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

/**
 * Created by Guilherme Biff Zarelli on 14/03/16.
 */
public class NotificationUtils {

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    public static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";


    private NotificationUtils() {
        throw new RuntimeException("No NotificationUtils!");
    }

    public static NotificationCompat.Builder createNotification(Context context, String title, String mensage, PendingIntent pi) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentTitle(title);
        builder.setContentText(mensage);
        builder.setContentIntent(pi);
        return builder;
    }

    public static void notify(Context context, NotificationCompat.Builder notificationBuilder, int id) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notificationBuilder.build());
    }

    public static void cancel(Context context, int id) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(id);
    }


    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     *
     * @return True if eanbled, false otherwise.
     */
    public static boolean isNotificationServiceEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
