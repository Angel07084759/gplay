package com.adtv.gmaploctest;



import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainB extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener
{

    private EditText addrStart;
    private EditText addrDest;
    private Button btnGetDirection;
    private Button btnShowAutoComp;

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

        addrStart = (EditText) findViewById(R.id.et_addr_start);
        addrDest = (EditText) findViewById(R.id.et_addr_dest);
        btnGetDirection = (Button) findViewById(R.id.btn_get_directions);
        //btnShowAutoComp = (Button) findViewById(R.id.btn_show_auto_comp);

        addrStart.addTextChangedListener(this);
        addrDest.addTextChangedListener(this);
        addrStart.setOnFocusChangeListener(this);
        addrDest.setOnFocusChangeListener(this);

    }
    int currentEditText = 2;
    public void click(View view)
    {
        Log.d("DEB", "click: " + (view.getId() == btnShowAutoComp.getId()) + "[" + currentEditText +  "]");

        if (view.getId() == btnShowAutoComp.getId() )
        {
            switch (currentEditText)
            {
                case 0:
                    addrStart.setText(btnShowAutoComp.getText());
                    break;
                case 1:
                    addrDest.setText(btnShowAutoComp.getText());
                    break;
                default:
                    break;
            }
            return;
        }
        try
        {
            Address a = new Geocoder(this).getFromLocation(33.1356404, -117.18882930000001, 1).get(0);
            Log.e("DEB", a.getPremises() + ">>" + a.toString());//
        }catch (Exception e){}

    }
    Runnable runnable = new Runnable() {
        @Override
        public void run()
        {

        }
    };

    @Override public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        long sTime = System.currentTimeMillis();
        Address addr = getLocationFromAddress(s.toString());

        currentEditText = addrStart.getText().toString().trim().equals(s.toString()) ? 0 : 1;


        if (addr == null)
        {
            btnShowAutoComp.setText(s.toString());
            return;
        }

        Log.d("DEB", "onTextChanged: " + (System.currentTimeMillis() - sTime) + "[" + currentEditText+ "]" + addr.toString());

        btnShowAutoComp.setText(addr.getAddressLine(addr.getMaxAddressLineIndex()));

    }

    public Address getLocationFromAddress(String strAddress)//, int maxResults)
    {
        try
        {
            return new Geocoder(this).getFromLocationName(strAddress, 1).get(0);//
        }
        catch (Exception e)
        {Log.d("DEB", "getLocationFromAddress:Exception: " + e.getMessage());
            return null;
        }
    }




    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void afterTextChanged(Editable s) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        Log.d("DEB", "onFocusChange: " + v.getId() +  hasFocus + ">>>" + v.toString());
    }
    //public void checkAccessFineLocationPermission(){}
}
