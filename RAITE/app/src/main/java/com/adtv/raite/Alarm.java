package com.adtv.raite;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
public class Alarm extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DEB: TAG: Alarm: BroadcastReceiver");
        wl.acquire();

        // Put here YOUR code.
        //if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        //this phone number is a  driver, set alarm on boot else:
        setAlarm(context, 2000);

        if (isOnline(context))

        Toast.makeText(context, "DEB:intent.getAction():: " + intent.getAction(), Toast.LENGTH_SHORT).show();//.makeText(context, "Alarm !!!!!!!!!!" + topOfStack(context, 0), Toast.LENGTH_SHORT).show(); // For example
        Log.d("DEB", "DEB:intent.getAction():: " + intent.getAction());//////////////////////////
        //cancelAlarm(context);
        wl.release();
    }

    static public void setAlarm(Context context, long intervalMillis)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);
    }

    static public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

/*
    public boolean CheckAlertService(Context context)
    {
        Intent i = new Intent(context, AlertNotifier.class);
        Boolean alarmup=(PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_NO_CREATE)!=null);
        return alarmup;
    }
*/

    public static boolean isSet(Context context)//TESTING
    {

        Intent intent = new Intent(context, Alarm.class);
        boolean b = (PendingIntent.getBroadcast( context, 0, intent, PendingIntent.FLAG_NO_CREATE ) != null);
        //Toast.makeText(context, "i" + b, Toast.LENGTH_SHORT).show(); // For example
        return b;
    }



















    static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}