package com.adtv.raite;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class Const
{
    static final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    static final String REGISTER = SERVER + "register.php";
    static final String VERIFY = SERVER + "verify.php";
    static final String MY_LOCATION = SERVER + "mylocation.php";
    static final String GET_DRIVERS = SERVER + "getdrivers.php";
    static final int DB_FIELDS_COUNT = DBVar.values().length;

    public enum DBVar
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
/**    public static void insert(int val,int[] arr){
        int i;
        for(i=0;i<arr.length-1;i++){
            if(arr[i]>val)
                break;
        }
        for(int k=arr.length-2; k>=i; k--){
            arr[k+1]=arr[k];
        }
        arr[i]=val;
        //System.out.println(Arrays.toString(arr));

    }*/

    public static class User
    {
        private String[] data;

        public User(String[] data)
        {
            this.data = data;
        }

        @Override
        public String toString()
        {
            String str = "";
            for (String s: data)
            {
                str += s + ",";
            }
            return str;
        }
    }




    public static List<User> sortCloser(String[] data, final User u)
    {
        //String[] temp = new String[data.length / DB_FIELDS_COUNT];

        List<User> list = new ArrayList<>(data.length / DB_FIELDS_COUNT);

        for (int i = 0 ; i < data.length ; i += DB_FIELDS_COUNT)
        {
            User user = new User(null);
            user.data = Arrays.copyOfRange(data, i, i + DB_FIELDS_COUNT);
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
        double lat1 = Math.toRadians(Double.valueOf(u1.data[DBVar.latitude.ordinal()]));
        double lng1 = Math.toRadians(Double.valueOf(u1.data[DBVar.longitude.ordinal()]));
        double lat2 = Math.toRadians(Double.valueOf(u2.data[DBVar.latitude.ordinal()]));
        double lng2 = Math.toRadians(Double.valueOf(u2.data[DBVar.longitude.ordinal()]));

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
