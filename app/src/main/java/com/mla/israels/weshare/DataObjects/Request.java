package com.mla.israels.weshare.DataObjects;

import android.location.Location;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Israel Sameach on 29/07/2016.
 */
public class Request implements Serializable {
    public int Id;
    public int UserId;
    public int JobId;
    public String Title;
    public String Details;
    public String Location;
    public String StartDate;
    public String EndDate;
    public String CreationDate;
    public User User;
    public Offer[] Offers;


    public static Comparator<Request> CompareByCreationDate() {
        return new Comparator<Request>() {
            @Override
            public int compare(Request lhs, Request rhs) {
                return rhs.CreationDate.compareTo(lhs.CreationDate);
            }
        };
    }

    public static Comparator<Request> CompareByDistance() {
        return new Comparator<Request>() {
            @Override
            public int compare(Request lhs, Request rhs) {
                try{
                    double latitude = 32.035183;
                    double longitude = 34.851398;
                    String lhslatlong[] = lhs.Location.substring(lhs.Location.lastIndexOf(";")+1).split(",", 2);
                    String rhslatlong[] = rhs.Location.substring(rhs.Location.lastIndexOf(";")+1).split(",", 2);

                    float[] results1 = new float[3];
                    android.location.Location.distanceBetween(latitude, longitude, Double.valueOf(lhslatlong[0]), Double.valueOf(lhslatlong[1]), results1);

                    float[] results2 = new float[3];
                    android.location.Location.distanceBetween(latitude, longitude, Double.valueOf(rhslatlong[0]), Double.valueOf(rhslatlong[1]), results2);

                    return ((Float)results1[0]).compareTo((Float)results2[0]);
                }catch (Exception e){
                    return 0;
                }
            }
        };
    }
}
