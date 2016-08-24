package com.mla.israels.weshare.DataObjects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Israel Sameach on 29/07/2016.
 */
public class Request implements Serializable, Comparable<Request> {
    public int Id;
    public int UserId;
    public int JobId;
    public String Title;
    public String Details;
    public String Location;
    public String StartDate;
    public String EndDate;
    public String CreationDate;

    @Override
    public int compareTo(Request another) {
        return another.CreationDate.compareTo(CreationDate);
    }
}
