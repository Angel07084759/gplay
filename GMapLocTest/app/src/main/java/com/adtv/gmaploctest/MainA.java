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
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainA extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void click(final View view)
    {
        Intent intent = null;

        if (view.getId() == findViewById(R.id.main_id).getId())//TEST:
        {//new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<lat>,<long>?q=<lat>,<long>(Label+Name)"));
            Log.d("DEB", ((EditText) view).getText().toString().trim());

            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
            {
                @Override
                public void onSuccess(Location location)
                {
                    if (location != null)
                    {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + "My Location" + ")";
                        //Log.d("DEB", (Uri.parse("geo:"+lat+","+lon+"?q="+lat+","+lon+"(My Location)").toString()) );
                        //startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+lat+","+lon+"?q="+lat+","+lon+"(My Location)")   ));
                        Log.d("DEB", geoUri);
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri)));

                    }
                }
            });
            return;
        }
        else if (view.getId() == findViewById(R.id.main_id).getId())
        {
            Log.d("DEB", ((EditText)findViewById(R.id.et_addr_start)).getText().toString().trim());

            Geocoder coder = new Geocoder(this);
            List<Address> address;

            try {
                address = coder.getFromLocationName(((EditText)findViewById(R.id.et_addr_start)).getText().toString().trim(), 1);
                if (address == null) {
                    return;
                }
                Address location = address.get(0);
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                Log.d("DEB", "COLLECTED INFO"  + address.get(0).toString());

                intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse( "http://maps.google.com/maps?q=loc:" + lat + "," + lon + " (" + "My Location" + ")"));
            } catch (Exception e) {
                return;
            }
        }
        else if (view.getId() == findViewById(R.id.btn_get_directions).getId())//LOC: from current location to given address
        {
            Log.d("DEB", ((EditText)findViewById(R.id.et_addr_start)).getText().toString().trim());

            intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=33.135619,-117.188826"));
        }
        else//NAV: from given location to given address
        {
            Log.d("DEB", ((EditText)findViewById(R.id.et_addr_start)).getText().toString().trim());

            intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=33.183682,-117.244531&daddr=33.135619, -117.188826"));
        }
        //((LinearLayout)findViewById(R.id.main_id)).setBackgroundColor(Color.BLACK);
        startActivity(intent);
    }
    public String getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            return lat + "," + lng;
        } catch (Exception e) {
            return null;
        }
    }



/*    public void click(View view)
    {
        double latitude = 33.183682;// 40.714728;
        double longitude = -117.244531;//-73.998672;
        String label = "I'm Here!";
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(mapIntent);
    }*/
}
