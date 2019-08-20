package com.adtv.raite;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MyLocation extends Service
{
    public static final String CHANNEL_ID = "MyLocationServiceChannel";
    private static final String NOTIFICATION_MSG = "inputExtra";
    private static final String NOTIFICATION_TITLE = "Your location is currently visible!";
    private static final long UPDATE_INTERVAL = 5000;
    private static boolean isRunning = false;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String notificationMsg = intent.getStringExtra(NOTIFICATION_MSG);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,
                    "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }


        Intent notificationIntent = new Intent(this, Main.class);

        notificationIntent.setAction(Intent.ACTION_MAIN);//
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);//
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(notificationMsg)
                .setSmallIcon(R.drawable.ic_raite_green)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        if (!isRunning)
        {
            isRunning = true;
            new Thread(new UpdateLocation()).start();
        }
        return START_NOT_STICKY;
    }



    class UpdateLocation implements Runnable
    {
        private Handler handler = new Handler();

        @Override
        public void run()
        {
            if (isRunning)
            {
                //Wake up phone
                PowerManager pm = (PowerManager) getApplication().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DEB: TAG: Alarm: BroadcastReceiver");
                wl.acquire();
                //Perform Task
                new LocationTask(getApplicationContext(), new LocationTask.LocationTaskResponse()
                {
                    @Override
                    public void onProcessFinish(List<Address> addresses)
                    {
                        if (addresses != null && addresses.size() > 0)
                        {
                            String latitude = addresses.get(0).getLatitude() + "";
                            String longitude = addresses.get(0).getLongitude() + "";

                            new PHPConnect(new PHPConnect.PHPResponse()
                            {
                                @Override
                                public void processFinish(String result)
                                {
                                    if (result == null || result.trim().length() == 0)
                                    {
                                        Toast.makeText(getApplicationContext(), "MyLocation: Location update Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute(Const.MY_LOCATION,
                                    Const.DBVar.phone.name(), Main.phoneNumber,
                                    Const.DBVar.ltime.name(), Main.timeMillis(),
                                    Const.DBVar.latitude.name(), latitude,
                                    Const.DBVar.longitude.name(), longitude);
                        }
                    }
                }).runTask();
                //Release sleep phone
                handler.postDelayed(this, UPDATE_INTERVAL);
                wl.release();
            }
        }

    }


    public static void startService(Context context, String msg)
    {
        Intent serviceIntent = new Intent(context, MyLocation.class);

        serviceIntent.putExtra(NOTIFICATION_MSG, msg);

        ContextCompat.startForegroundService(context, serviceIntent);
    }

    public static void stopService(Context context)
    {
        Intent serviceIntent = new Intent(context, MyLocation.class);
        context.stopService(serviceIntent);
    }
    public static boolean isRunning()
    {
        return isRunning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}