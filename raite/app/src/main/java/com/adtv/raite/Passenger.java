package com.adtv.raite;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Passenger extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener
{
    private AutoCompleteTextView addrStartIn, addrDestIn, addrInhelper;
    private long lastTextChangeTime;
    private int wordsCount;
    private TextView errMsgTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger);
        errMsgTV = (TextView) findViewById(R.id.tv_passenger_err_msg);
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

        new LocationTask(this, new LocationTask.LocationTaskResponse()
        {
            @Override
            public void onProcessFinish(final List<Address> addresses)//get lat lng
            {
                new PHPConnect(new PHPConnect.PHPResponse() {
                    @Override
                    public void processFinish(String result)//get drivers
                    {

                        List<Const.User> drivers = new ArrayList<>(0);

                        if (result != null)
                        {
                            Log.d("DEB", "... ... ... ...");
                            drivers = Const.sortCloser(result.split(","), Main.currentUser);
                        }
                        String str = "DRIVERS: ";////////////////////////////////

                        for (Const.User driver : drivers)
                        {
                            //Log.d("DEB", ">>>" + driver.toString());///////////////////
                            str += driver.toString().split(",")[Const.DBVar.fname.ordinal()] + "  ";
                        }
                        if (drivers.size() > 0)
                        {
                            closestDriver = drivers.get(0);
                        }

                        errMsgTV.setText(str);////////////////////////////////////
                    }
                }).execute(Const.GET_DRIVERS, Const.DBVar.driver.name(), "1");
            }
        }).runTask();

    }


    ArrayList<String> getDriver()
    {
        ArrayList<String>  drivers  = new ArrayList<String> ();


        return drivers;
    }


    public void click(final View view)
    {//Log.d("DEB", "{Main.click---------------------------------------}");
        //addrStart = addrDest = null;
        view.setEnabled(false);
        new LocationTask(this, new LocationTask.LocationTaskResponse()
        {//Trying resolving
            @Override
            public void onProcessFinish(final List<Address> addrsListAB)
            {//Log.d("DEB", "{Main.click.onProcessFinish(A)}");
                view.setEnabled(true);
                if (addrsListAB != null)
                {
                    if (addrsListAB.size() == 1)
                    {
                        new LocationTask(Passenger.this, new LocationTask.LocationTaskResponse()
                        {
                            @Override
                            public void onProcessFinish(List<Address> addrsListA)
                            {
                                Log.d("DEB", "{Main.click.onProcessFinish(): getAddressLine[A ]}" + addrsListA.get(0).getAddressLine(0));
                                Log.d("DEB", "{Main.click.onProcessFinish(): getAddressLine[AB]}" + addrsListAB.get(0).getAddressLine(0));
                                double latStart = addrsListA.get(0).getLatitude();
                                double lngStart = addrsListA.get(0).getLongitude();
                                double latDest = addrsListAB.get(0).getLatitude();
                                double lngDest = addrsListAB.get(0).getLongitude();

                                Location loc1 = new Location("");Location loc2 = new Location("");////
                                loc1.setLatitude(latStart);loc1.setLongitude(lngStart);loc2.setLatitude(latDest);loc2.setLongitude(lngDest);////

                                Toast.makeText(Passenger.this, "Distance: " + (loc1.distanceTo(loc2) * 0.00062137) , Toast.LENGTH_SHORT).show();//miles = meters × 0.00062137

                                String geoUri = "http://maps.google.com/maps?saddr=";
                                if (addrsListAB.get(0).getAddressLine(0).trim().equalsIgnoreCase(addrDestIn.getText().toString().trim()))
                                {
                                    geoUri += (latStart + "," + lngStart + "&daddr=" + latDest + "," + lngDest);
                                }
                                else
                                {
                                    geoUri += (latDest + "," + lngDest + "&daddr=" + latStart + "," + lngStart);
                                }
                                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                            }
                        }).runTask();
                    }
                    else if (addrsListAB.size() == 2)
                    {
                        double latStart = addrsListAB.get(0).getLatitude();
                        double lngStart = addrsListAB.get(0).getLongitude();
                        double latDest = addrsListAB.get(1).getLatitude();
                        double lngDest = addrsListAB.get(1).getLongitude();
                        String geoUri = "http://maps.google.com/maps?saddr=" + latStart + "," + lngStart + "&daddr=" + latDest + "," + lngDest;
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
                    }
                    else
                    {
                        Toast.makeText(Passenger.this, "Address Inputs Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {Log.d("DEB", "{Main.click.onProcessFinish(C)}");
                    new LocationTask(Passenger.this, new LocationTask.LocationTaskResponse()
                    {
                        @Override
                        public void onProcessFinish(List<Address> addresses)
                        {
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
            new LocationTask(this, new LocationTask.LocationTaskResponse()
            {
                @Override
                public void onProcessFinish(List<Address> addresses)
                {
                    if (addresses != null && addresses.size() > 0)
                    {
                        String[] addrs = new String[addresses.size()];
                        for (int i = 0; i < addresses.size(); i++)
                        {
                            Address addr = addresses.get(i);
                            addrs[i] = addr.getAddressLine(addr.getMaxAddressLineIndex());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Passenger.this, android.R.layout.simple_dropdown_item_1line, addrs);

                        addrInhelper.setAdapter(adapter);

                        if (addrs.length == 1 && addrs[0].equals(addrInhelper.getText().toString()))
                        {
                            return;
                        }
                        addrInhelper.showDropDown();
                    }
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

    Const.User closestDriver = Main.currentUser;
    public void testingClick(View view)
    {
        String[] dataStar = closestDriver.toString().split(",");
        String[] dataDest = Main.currentUser.toString().split(",");


        String latStart = dataStar[Const.DBVar.latitude.ordinal()];
        String lngStart = dataStar[Const.DBVar.longitude.ordinal()];
        String latDest = dataDest[Const.DBVar.latitude.ordinal()];
        String lngDest = dataDest[Const.DBVar.longitude.ordinal()];
        String geoUri = "http://maps.google.com/maps?saddr=" + latStart + "," + lngStart + "&daddr=" + latDest + "," + lngDest;
        Log.d("DEB", "[ " +  closestDriver.toString() + " ]");/////////////////////////////////////////////
        Log.d("DEB", "[ " +  Main.currentUser.toString() + " ]");/////////////////////////////////////////////
        Log.d("DEB", "[ " + geoUri + " ]");/////////////////////////////////////////////
        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));
    }
}
