package com.adtv.raite;

public final class Const
{
    static final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    static final String REGISTER = SERVER + "register.php";
    static final String VERIFY = SERVER + "verify.php";
    static final String MY_LOCATION = SERVER + "mylocation.php";
    static final String GET_DRIVERS = SERVER + "getdrivers.php";

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

}
