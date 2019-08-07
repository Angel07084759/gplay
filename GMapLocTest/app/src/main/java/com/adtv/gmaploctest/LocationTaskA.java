package com.adtv.gmaploctest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;


public class LocationTaskA //extends AsyncTask<Object, Void, List<Address>>//  implements TextWatcher, View.OnFocusChangeListener
{
    private static final int GEOCODER_MAX_RESULT = 5;

    private Context context;
    private LocationTaskResponse delegate = null;

    public LocationTaskA(Context context, LocationTaskResponse delegate)
    {
        this.context = context;
        this.delegate = delegate;
    }

    void runTask()
    {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(context, "LocationTask:runTask(): ACCESS_FINE_LOCATION: Failed", Toast.LENGTH_LONG ).show();
            return;
        }

        LocationServices.getFusedLocationProviderClient(context). getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if (location != null)
                {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    try
                    {
                        List<Address> addresses = new Geocoder(context).getFromLocation(latitude, longitude, GEOCODER_MAX_RESULT);
                        if (addresses == null || addresses.size() == 0)
                        {
                            throw new Exception("LocationTask:runTask(): Could not find current location.");
                        }
                        addresses.get(0).setLatitude(latitude);
                        addresses.get(0).setLongitude(longitude);
                        delegate.onProcessFinish(addresses);
                    }
                    catch (Exception e)
                    {
                        Log.d("DEB", "LocationTask:runTask(): Exception: " + e.getMessage());
                    }
                }
            }
        });
    }

    void runTask(String s)
    {
        new ProcessTask().execute(s);
    }

    void runTask(double latitude, double longitude)
    {
        new ProcessTask().execute(latitude, longitude);
    }

    public interface LocationTaskResponse
    {
        void onProcessFinish(List<Address> addresses);
    }





    private class ProcessTask extends AsyncTask<Object, Void, List<Address>>
    {
        @Override
        protected void onPostExecute(List<Address> addresses)
        {
            delegate.onProcessFinish(addresses);
        }

        @Override
        protected List<Address> doInBackground(Object... objs)
        {
            long testTime = System.currentTimeMillis();
            try
            {
                if (objs.length > 1)
                {
                    List<Address> a = new Geocoder(context).getFromLocation((Double) objs[0], (Double) objs[1], GEOCODER_MAX_RESULT);
                    //Log.d("DEB", "getLocationFromAddress: " + (objs.length) + "   testTime: " + (System.currentTimeMillis() - testTime));
                    return a;
                }
                else if (objs.length > 1 ){}
                List<Address> a = new Geocoder(context).getFromLocationName((String) objs[0], GEOCODER_MAX_RESULT);
                //Log.d("DEB", "getLocationFromAddress:: " + (objs.length) + "   testTime: " + (System.currentTimeMillis() - testTime));
                return a;
            }
            catch (Exception e)
            {//Log.d("DEB", "getLocationFromAddress:Exception: " + e.getMessage());
                //Log.d("DEB", "getLocationFromAddress:Exception: " + (objs.length) + "   testTime: " + (System.currentTimeMillis() - testTime));
                return null;
            }
        }
    }
}
