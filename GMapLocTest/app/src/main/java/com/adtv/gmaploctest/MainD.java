package com.adtv.gmaploctest;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
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

public class MainD extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener
{

    private AutoCompleteTextView addrStartIn, addrDestIn, addrInhelper;
    //Address addrStart, addrDest, addrHelper;
    long lastTextChangeTime;
    int wordsCount;


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

        addrStartIn = (AutoCompleteTextView) findViewById(R.id.et_addr_start);
        addrDestIn = (AutoCompleteTextView) findViewById(R.id.et_addr_dest);

        addrStartIn.addTextChangedListener(this);
        addrDestIn.addTextChangedListener(this);
        addrStartIn.setOnFocusChangeListener(this);
        addrDestIn.setOnFocusChangeListener(this);

        lastTextChangeTime = System.currentTimeMillis();

    }


    public void click(final View view)
    {//Log.d("DEB", "{Main.click---------------------------------------}");
        //addrStart = addrDest = null;
        view.setEnabled(false);
        new LocationTask(this, new LocationTask.LocationTaskResponse()
        {//Trying resolving
            @Override
            public void onProcessFinish(final List<Address> addressesA)
            {//Log.d("DEB", "{Main.click.onProcessFinish(A)}");
                double latStart = 0.0;
                double lngStart = 0.0;
                double latDest = 0.0;
                double lngDest = 0.0;
                String geoUri = "";

                if (addressesA != null)
                {
                    switch (addressesA.size())
                    {
                        case 1:Log.d("DEB", "{Main.click.onProcessFinish(A)}");

                            new LocationTask(MainD.this, new LocationTask.LocationTaskResponse()
                            {
                                @Override
                                public void onProcessFinish(List<Address> addresses)
                                {
                                    view.setEnabled(true);
                                    if (addresses != null && addresses.size() > 0)
                                    {
                                        double latStart = addressesA.get(0).getLatitude();
                                        double lngStart = addressesA.get(0).getLongitude();
                                        double latDest = addresses.get(1).getLatitude();
                                        double lngDest = addresses.get(1).getLongitude();
                                        String geoUri = "http://maps.google.com/maps?saddr="
                                                + latStart + "," + lngStart + "&daddr=" + latDest + "," + lngDest;
                                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                                    }
                                }
                            }).runTask();

                            view.setEnabled(true);
                            latStart = addressesA.get(0).getLatitude();
                            lngStart = addressesA.get(0).getLongitude();
                            geoUri = "http://maps.google.com/maps?q=loc:" + latStart + "," + lngStart;
                            startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                            break;
                        case 2:Log.d("DEB", "{Main.click.onProcessFinish(B)}");
                            view.setEnabled(true);
                            latStart = addressesA.get(0).getLatitude();
                            lngStart = addressesA.get(0).getLongitude();
                            latDest = addressesA.get(1).getLatitude();
                            lngDest = addressesA.get(1).getLongitude();
                            geoUri = "http://maps.google.com/maps?saddr="
                                    + latStart + "," + lngStart + "&daddr=" + latDest + "," + lngDest;
                            startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));

                            break;
                        default:
                            break;
                    }
                }
                else
                {Log.d("DEB", "{Main.click.onProcessFinish(C)}");
                    new LocationTask(MainD.this, new LocationTask.LocationTaskResponse()
                    {
                        @Override
                        public void onProcessFinish(List<Address> addresses)
                        {
                            view.setEnabled(true);
                            if (addresses != null && addresses.size() > 0)
                            {
                                double latStart = addresses.get(0).getLatitude();
                                double lngStart = addresses.get(0).getLongitude();
                                String geoUri = geoUri = "http://maps.google.com/maps?q=loc:" + latStart + "," + lngStart;
                                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                            }
                        }
                    }).runTask();
                }
            }
        }).runTask(addrStartIn.getText().toString().trim(), addrDestIn.getText().toString().trim());

    }





    @Override public void onTextChanged(CharSequence s, final int start, int before, int count)
    {
        long textChangeElapse = System.currentTimeMillis() - lastTextChangeTime;
        String[] strSplit = s.toString().split(" ");

        if (textChangeElapse > 700 || strSplit.length != wordsCount)//USE CONSTANT
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainD.this, android.R.layout.simple_dropdown_item_1line, addrs);

                        addrInhelper.setAdapter(adapter);

                        if (addrs.length == 1 && addrs[0].equals(addrInhelper.getText().toString()))
                        {
/*                            if (helperAddr == addrStart)
                            {
                                addrStart = addresses.get(0);
                                return;
                            }
                            addrDest = addresses.get(0);*/
                            return;
                        }
                        //helperAddr = null;
                        addrInhelper.showDropDown();
                    }
                    //helperAddr = null;
                }
            }).runTask(s.toString());
            wordsCount = strSplit.length;
        }
        lastTextChangeTime = System.currentTimeMillis();

    }


    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void afterTextChanged(Editable s) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus && (v.getId() == R.id.et_addr_start || v.getId() == R.id.et_addr_dest))
        {
            addrInhelper = (AutoCompleteTextView) v;
            //addrHelper = addrInhelper == addrStartIn ? addrStart : addrDest;
            //Log.d("DEB", "onFocusChange: " + v.getId() +  hasFocus + ">>>" + addrInhelper.getHint());
        }

    }
    //public void checkAccessFineLocationPermission(){}
}
