package com.mla.israels.weshare.communication;

/**
 * Created by Israel Sameach on 29/07/2016.
 */
import com.mla.israels.weshare.DataObjects.Request;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface RequestService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/weshare/api/requests/Requests
    @GET("/api/requests")
    public void getRequest(Callback<List<Request>> callback);

    //i.e. http://localhost/apiweshare/api/requests/Requests/1
    //Get Request record base on ID
    @GET("/api/requests/{id}")
    public void getRequestById(@Path("id") Integer id, Callback<Request> callback);

    //i.e. http://localhost/apiweshare/api/requests/Requests/1
    //Delete Request record base on ID
    @DELETE("/api/requests/{id}")
    public void deleteRequestById(@Path("id") Integer id, Callback<Request> callback);

    //i.e. http://localhost/apiweshare/api/requests/Requests/1
    //PUT Request record and post content in HTTP request BODY
    @PUT("/api/requests/{id}")
    public void updateRequestById(@Path("id") Integer id, @Body Request Request, Callback<Request> callback);

    //i.e. http://localhost/apiweshare/api/requests/Requests
    //Add Request record and post content in HTTP request BODY
    @POST("/api/requests")
    public void addRequest(@Body Request Request, Callback<Request> callback);
}
