package com.mla.israels.weshare.communication;

/**
 * Created by Israel Sameach on 29/07/2016.
 */
import com.mla.israels.weshare.DataObjects.Offer;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface OfferService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/weshare/api/offers/Offers
    @GET("/api/offers")
    public void getOffer(Callback<List<Offer>> callback);

    //i.e. http://localhost/apiweshare/api/offers/Offers/1
    //Get Offer record base on ID
    @GET("/api/offers/{id}")
    public void getOfferById(@Path("id") Integer id, Callback<Offer> callback);

    //i.e. http://localhost/apiweshare/api/offers/Offers/1
    //Delete Offer record base on ID
    @DELETE("/api/offers/{id}")
    public void deleteOfferById(@Path("id") Integer id, Callback<Offer> callback);

    //i.e. http://localhost/apiweshare/api/offers/Offers/1
    //PUT Offer record and post content in HTTP offer BODY
    @PUT("/api/offers/{id}")
    public void updateOfferById(@Path("id") Integer id, @Body Offer Offer, Callback<Offer> callback);

    //i.e. http://localhost/apiweshare/api/offers/Offers
    //Add Offer record and post content in HTTP offer BODY
    @POST("/api/offers")
    public void addOffer(@Body Offer Offer, Callback<Offer> callback);
}
