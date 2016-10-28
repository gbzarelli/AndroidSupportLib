package br.com.helpdev.supportlib.sistema.notificacao;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by demantoide on 14/03/16.
 */
public class NotificationUtils {

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

}
