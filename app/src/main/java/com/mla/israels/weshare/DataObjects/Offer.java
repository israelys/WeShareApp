package com.mla.israels.weshare.DataObjects;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Israel Sameach on 29/07/2016.
 */
public class Offer implements Serializable {
    public int Id;
    public int RequestId;
    public int UserId;
    public String Comment;
    public int IsApproved;
    public User User;
    public Request Request;
}
