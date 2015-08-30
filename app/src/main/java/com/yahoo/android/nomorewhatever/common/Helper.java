package com.yahoo.android.nomorewhatever.common;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by mchsieh on 8/27/15.
 */
public class Helper {
    public static final double SCOPE = 0.5;

    private static final double EARTH_RADIUS = 6378137.0;

    public static final double DEFAULT_LAT = 25.0339639;

    public static final double DEFUALT_LNG = 121.5644722;

    /**
     * calc distance
     * @param lat_a src
     * @param lng_a src
     * @param lat_b tag
     * @param lng_b tag
     * @return  Meter
     */
    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }



}
