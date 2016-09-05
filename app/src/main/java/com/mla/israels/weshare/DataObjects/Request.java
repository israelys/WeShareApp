package com.mla.israels.weshare.DataObjects;

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
                return 0;
            }
        };
    }
}
