package com.adtv.gmaploctest;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;

public class Main extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener
{

    private AutoCompleteTextView ACTVstartAddr, ACTVdestAddr, ACTVhelper;
    Address startAddr, destAddr, helperAddr;
    long lastTextChange;
    int numberOfWords;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        ACTVstartAddr = (AutoCompleteTextView) findViewById(R.id.et_addr_start);
        ACTVdestAddr = (AutoCompleteTextView) findViewById(R.id.et_addr_dest);

        ACTVstartAddr.addTextChangedListener(this);
        ACTVdestAddr.addTextChangedListener(this);
        ACTVstartAddr.setOnFocusChangeListener(this);
        ACTVdestAddr.setOnFocusChangeListener(this);

        lastTextChange = System.currentTimeMillis();

    }


    public void click(View view)
    {
        startAddr = destAddr = null;

        new LocationTask(this, new LocationTask.LocationTaskResponse()
        {
            @Override
            public void onProcessFinish(List<Address> addresses)
            {Log.d("DEB", "{Main.click.onProcessFinish(A)}");

                new LocationTask(Main.this, new LocationTask.LocationTaskResponse()
                {
                    @Override
                    public void onProcessFinish(List<Address> addresses)
                    {
                        Log.d("DEB", "{Main.click.onProcessFinish(C)}");

                        if (startAddr == null && addresses != null && addresses.size() > 0)
                        {
                            startAddr = addresses.get(0);
                        }



                    }
                }).runTask();//Current user location
                if (addresses != null && addresses.size() > 0 )
                {
                    startAddr = addresses.get(0);
                }



            }
        }).runTask(ACTVstartAddr.getText().toString().trim());

        new LocationTask(this, new LocationTask.LocationTaskResponse()
        {
            @Override
            public void onProcessFinish(List<Address> addresses)
            { Log.d("DEB", "{Main.click.onProcessFinish(B)}");

                if (addresses != null && addresses.size() > 0)
                {
                    destAddr = addresses.get(0);
                    //double latStart = startAddr.getLatitude();
                    //double lngStart = startAddr.getLongitude();
                    //double latDest = destAddr.getLatitude();
                    //double lngDest = destAddr.getLongitude();

                    //String geoUri = "http://maps.google.com/maps?saddr="
                    //        + latStart + "," + lngStart + "&daddr="+ latDest + "," + lngDest;

                    //Log.d("DEB", "{Main.ckick((2)) onProcessFinish} = [" + geoUri + "]");
                    //startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                    return;
                }
                //Log.d("DEB", "{Main.click(2)}" + (startAddr == null));//CRASHES
/*                if (ACTVdestAddr.getText().toString().trim().length() == 0)
                {
                    double latStart = startAddr.getLatitude();
                    double lngStart = startAddr.getLongitude();
                    String geoUri = "http://maps.google.com/maps?q=loc:" + latStart + "," + lngStart;
                    startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                }*/

                //Log.d("DEB", "{Main.click(2)}" + destAddr.toString());//CRASHES
            }
        }).runTask(ACTVdestAddr.getText().toString().trim());

    }



    @Override public void onTextChanged(CharSequence s, final int start, int before, int count)
    {
        long lastChangeElapse = System.currentTimeMillis() - lastTextChange;
        String[] strSplit = s.toString().split(" ");

        if (lastChangeElapse > 700 || strSplit.length != numberOfWords)//USE CONSTANT
        {
            //Log.d("DEB", "---------------if-------------" + lastChangeElapse);
            new LocationTask(this, new LocationTask.LocationTaskResponse()
            {
                @Override
                public void onProcessFinish(List<Address> addresses)
                {
                    if (addresses != null && addresses.size() > 0)
                    {

                        //Log.d("DEB", "----------------------------\n");
                        String[] addrs = new String[addresses.size()];
                        for (int i = 0; i < addresses.size(); i++)
                        {
                            Address addr = addresses.get(i);
                            addrs[i] = addr.getAddressLine(addr.getMaxAddressLineIndex());
                            //Log.d("DEB", addr.getLatitude() + "[ao]" + addr.getLongitude() +  "[i:" + i + "]" + "<" + addr.getFeatureName() + ">" + addr.getAddressLine(addr.getMaxAddressLineIndex()));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main.this, android.R.layout.simple_dropdown_item_1line, addrs);

                        ACTVhelper.setAdapter(adapter);

                        if (addrs.length == 1 && addrs[0].equals(ACTVhelper.getText().toString()))
                        {
/*                            if (helperAddr == startAddr)
                            {
                                startAddr = addresses.get(0);
                                return;
                            }
                            destAddr = addresses.get(0);*/
                            return;
                        }
                        //helperAddr = null;
                        ACTVhelper.showDropDown();
                    }
                    //helperAddr = null;
                }
            }).runTask(s.toString());
            numberOfWords = strSplit.length;
        }
        lastTextChange = System.currentTimeMillis();

    }


    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void afterTextChanged(Editable s) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus && (v.getId() == R.id.et_addr_start || v.getId() == R.id.et_addr_dest))
        {
            ACTVhelper = (AutoCompleteTextView) v;
            //helperAddr = ACTVhelper == ACTVstartAddr ? startAddr : destAddr;
            Log.d("DEB", "onFocusChange: " + v.getId() +  hasFocus + ">>>" + ACTVhelper.getHint());
        }

    }
    //public void checkAccessFineLocationPermission(){}
}
