package hdv.iky.nrftouch.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import javax.inject.Inject;

import hdv.iky.nrftouch.R;
import hdv.iky.nrftouch.injection.ApplicationContext;
import hdv.iky.nrftouch.ui.control.ControlActivity;

/**
 * Created by Ann on 1/30/16.
 */
public class NotificationHelper {

    public static final int BLE_SERVICE_NOTIFICATION_ID = 2;

    Context context;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;

    public Notification getNotify(String ss){
        builder.setContentText(ss);
        Intent myIntent1 = new Intent(context, ControlActivity.class);
        myIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        myIntent1.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, BLE_SERVICE_NOTIFICATION_ID, myIntent1, 0);
        builder.setContentIntent(pendingIntent1);
        return builder.build();
    }

    @Inject
    public NotificationHelper(@ApplicationContext Context context) {
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context) .setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(Notification.DEFAULT_SOUND);
//        builder.setVibrate(new long[] { 1000, 1000});
        builder.setAutoCancel(true);
        builder.setContentTitle("IKY Smartkey Running...");
    }

    public void show(String ss){
        builder.setContentText(ss);
        Intent myIntent1 = new Intent(context, ControlActivity.class);
        myIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, BLE_SERVICE_NOTIFICATION_ID, myIntent1, 0);
        builder.setContentIntent(pendingIntent1);
        mNotificationManager.notify(BLE_SERVICE_NOTIFICATION_ID, builder.build());
    }
}
