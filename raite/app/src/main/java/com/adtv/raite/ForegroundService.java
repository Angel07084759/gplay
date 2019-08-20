package com.adtv.raite;

import android.app.ActivityManager;
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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Log.d("DEB", "ForegroundService:STARTED: " + isRunning);/////////////////////////

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
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_raite_green)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);


        //do heavy work on a background thread

        if (!isRunning)
        {
            isRunning = true;
            new Thread(new UpdateLocation()).start();
        }


        //stopSelf();

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
                new LocationTask(getApplicationContext(), new LocationTask.LocationTaskResponse()
                {
                    @Override
                    public void onProcessFinish(List<Address> addresses)
                    {
                        if (addresses != null && addresses.size() > 0)
                            Toast.makeText(getApplicationContext(), "Lat:[" + addresses.get(0).getLatitude()
                                    + "] Lng[" + addresses.get(0).getLongitude() + "]", Toast.LENGTH_SHORT).show();
                        Log.d("DEB","Lat:[" + addresses.get(0).getLatitude() + "] Lng[" + addresses.get(0).getLongitude() + "]" );
                    }
                }).runTask();
                handler.postDelayed(this, 5000);
            }
        }

    }

/*    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }*/



    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.d("DEB", "ForegroundService:STOPPED");/////////////////////////
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}