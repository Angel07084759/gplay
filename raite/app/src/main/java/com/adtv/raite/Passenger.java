package com.adtv.raite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Passenger extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener
{
    private AutoCompleteTextView addrStartIn, addrDestIn, addrInhelper;
    private long lastTextChangeTime;
    private int wordsCount;
    private TextView errMsgTV;

    User closestDriver = Main.user;

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
                    public void onProcessFinish(String result)//get drivers
                    {

                        List<User> drivers = new ArrayList<>(0);

                        if (result != null)
                        {
                            Log.d("DEB", "... ... ... ...");
                            drivers = User.sortCloser(result.split(","), Main.user);
                        }
                        String str = "DRIVERS: ";////////////////////////////////

                        for (User driver : drivers)
                        {
                            //Log.d("DEB", ">>>" + driver.toString());///////////////////
                            str += driver.toString().split(",")[User.Field.fname.ordinal()] + "  ";
                        }
                        if (drivers.size() > 0)
                        {
                            closestDriver = drivers.get(0);
                        }

                        errMsgTV.setText(str);////////////////////////////////////

                        LinearLayout ignoredList = (LinearLayout) findViewById(R.id.show_drivers);//to be inflated

                        //inflater: adds  views to a layout
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


                        for(final User user: drivers)
                        {
                            //Retrieves the template view to populate the ignored list layout.
                            View ignoredApp = inflater.inflate(R.layout.showdriver, null);


                            TextView initials = (TextView) ignoredApp.findViewById(R.id.initials);
                            Button openmap = (Button) ignoredApp.findViewById(R.id.mapit);

                            //Populating views
                            String[] fields = user.toString().split(User.DELIMITER);
                            String initialStr = fields[User.Field.fname.ordinal()].charAt(0) + "";
                            initialStr += fields[User.Field.lname.ordinal()].charAt(0) + "";
                            initials.setText(initialStr);

                            ignoredList.addView(ignoredApp);


                            //Adding onclick listener for restore to be available
                            openmap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    String[] dataStar = user.toString().split(",");
                                    String[] dataDest = Main.user.toString().split(",");


                                    String latStart = dataStar[User.Field.latitude.ordinal()];
                                    String lngStart = dataStar[User.Field.longitude.ordinal()];
                                    String latDest = dataDest[User.Field.latitude.ordinal()];
                                    String lngDest = dataDest[User.Field.longitude.ordinal()];
                                    String geoUri = "http://maps.google.com/maps?saddr=" + latStart + "," + lngStart + "&daddr=" + latDest + "," + lngDest;
                                    Log.d("DEB", "[ " +  closestDriver.toString() + " ]");/////////////////////////////////////////////
                                    Log.d("DEB", "[ " +  Main.user.toString() + " ]");/////////////////////////////////////////////
                                    Log.d("DEB", "[ " + geoUri + " ]");/////////////////////////////////////////////
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri)));
                                }
                            });
                        }


                    }
                }).execute(User.DRIVERS, User.Field.driver.name(), "1");//NEED MORE IMPLEMENTATION
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
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri)));
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
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri)));
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
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri)));
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
}
