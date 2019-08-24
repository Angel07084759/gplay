package com.adtv.raite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public  class User
{
    public static final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    public static final String LOGIN = SERVER + "login.php";
    public static final String REGISTER = SERVER + "register.php";
    public static final String LOCATION = SERVER + "location.php";
    public static final String DRIVERS = SERVER + "drivers.php";

    public static final String DELIMITER = ",";
    public static final int FIELDS_COUNT = Field.values().length;

    public enum Field
    {
        status,
        driver,
        phone,
        fname,
        lname,
        latitude,
        longitude,
        ftime,
        ltime,
        request,
        requester
    }

    private String[] data;

    public User(String[] data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        String str = "";
        for (String s : data)
        {
            str += s + DELIMITER;
        }
        return str;
    }

    public static ArrayList<User> sortCloser(String[] data, final User u)
    {
        ArrayList<User> list = new ArrayList<>((data.length / FIELDS_COUNT));

        for (int i = 0 ; i < data.length ; i += FIELDS_COUNT)
        {
            User user = new User(null);
            user.data = Arrays.copyOfRange(data, i, (i + FIELDS_COUNT));
            list.add(user);
        }

        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2)
            {
                double d1 = distance(u, o1);
                double d2 = distance(u, o2);
                return Double.compare(d1, d2);
            }
        });

        return list;
    }


    public static double distance(User u1, User u2)
    {
        double lat1 = Math.toRadians(Double.valueOf(u1.data[Field.latitude.ordinal()]));
        double lng1 = Math.toRadians(Double.valueOf(u1.data[Field.longitude.ordinal()]));
        double lat2 = Math.toRadians(Double.valueOf(u2.data[Field.latitude.ordinal()]));
        double lng2 = Math.toRadians(Double.valueOf(u2.data[Field.longitude.ordinal()]));

        // Haversine formula
        double dlon = lng2 - lng1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return(c * 6379000);//in meters// Radius of earth {kilometers = 6371  miles = 3956}OR 3958.8
    }

}
