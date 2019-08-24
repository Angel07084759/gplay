package com.adtv.raite;

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

import java.util.ArrayList;
import java.util.List;


public class LocationTask //extends AsyncTask<Object, Void, List<Address>>//  implements TextWatcher, View.OnFocusChangeListener
{
    private static final int GEOCODER_MAX_RESULT = 5;

    private Context context;
    private LocationTaskResponse delegate = null;

    public LocationTask(Context context, LocationTaskResponse delegate)
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

    void runTask(String... objs)
    {
        new TaskFromLocationName().execute(objs);
    }

    void runTask(Double... latNlng)
    {
        new TaskFromLocation().execute(latNlng);
    }

    public interface LocationTaskResponse
    {
        void onProcessFinish(List<Address> addresses);
    }





    private class TaskFromLocation extends AsyncTask<Double, Void, List<Address>>
    {
        @Override
        protected void onPostExecute(List<Address> addresses)
        {
            delegate.onProcessFinish(addresses);
        }

        @Override
        protected List<Address> doInBackground(Double... objs)
        {
            try
            {
                if (objs == null || objs.length < 2)
                {
                    throw new Exception("LocationTask:TaskFromLocation:doInBackground: NULL.");
                }
                else if (objs.length > 1)
                {
                    List<Address> list = (objs[0] != null && objs[1] != null) ?
                            new Geocoder(context).getFromLocation(objs[0], objs[1], 1): new ArrayList<Address>();
                    for (int i = 2; i < objs.length; i += 2)
                    {
                        Address addr = null;
                        if (objs[i] != null && objs[i + 1] != null)
                        {
                            addr = new Geocoder(context).getFromLocation(objs[i], objs[i + 1], 1).get(0);
                        }
                        if (addr != null)
                        {
                            list.add(addr);
                        }
                    }
                    return list.size() > 0 ? list : null;
                }
                return new Geocoder(context).getFromLocation(objs[0], objs[1], GEOCODER_MAX_RESULT);
            }
            catch (Exception e)
            {Log.d("DEB", "getLocationFromAddress:Exception: " + e.getMessage());
                return null;
            }
        }
    }
    private class TaskFromLocationName extends AsyncTask<String, Void, List<Address>>
    {
        @Override
        protected void onPostExecute(List<Address> addresses)
        {
            delegate.onProcessFinish(addresses);
        }

        @Override
        protected List<Address> doInBackground(String... objs)
        {
            try
            {
                //Log.d("DEB", "getLocationFromAddress:[try]: SIZE: " + (objs == null ? -1 : objs.length) );
                if (objs == null || objs.length == 0)
                { Log.d("DEB", "getLocationFromAddress:[if]: ");
                    throw new Exception("LocationTask:TaskFromLocationName:doInBackground: NULL .");
                }
                else if (objs.length > 0)
                {
                    List<Address> list = (objs[0] != null && objs[0].trim().length() != 0) ?
                            new Geocoder(context).getFromLocationName(objs[0], 1) : new ArrayList<Address>();

                    Log.d("DEB", "getLocationFromAddress:[else if1]: SIZE: " + list.size());
                    for (int i = 1; i < objs.length; i++)
                    {
                        Address addr = null;
                        if (objs[i] != null && objs[i].trim().length() != 0)
                        {
                            addr = new Geocoder(context).getFromLocationName(objs[i], 1).get(0);
                        }
                        if (addr != null)
                        {
                            list.add(addr);
                        }
                    }
                    Log.d("DEB", "getLocationFromAddress:[else if2]: SIZE: " + list.size());
                    return list.size() > 0 ? list : null ;
                }
                return new Geocoder(context).getFromLocationName(objs[0], GEOCODER_MAX_RESULT);
            }
            catch (Exception e)
            {Log.d("DEB", "getLocationFromAddress:Exception: " + e.getMessage());
                return null;
            }
        }
    }

}
