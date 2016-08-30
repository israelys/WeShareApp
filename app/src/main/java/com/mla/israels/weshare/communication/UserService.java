package com.mla.israels.weshare.communication;

/**
 * Created by Israel Sameach on 29/07/2016.
 */
import com.mla.israels.weshare.DataObjects.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface UserService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/weshare/api/users/Users
    @GET("/api/users")
    public void getUser(Callback<List<User>> callback);

    //i.e. http://localhost/apiweshare/api/users/Users/1
    //Get User record base on ID
    @GET("/api/users/{id}")
    public void getUserById(@Path("id") Integer id,Callback<User> callback);

    @GET("/api/users/getUserRequests/{id}")
    public void getUserRequests(@Path("id") Integer id,Callback<User> callback);

    @GET("/api/users/getUserOffers/{id}")
    public void getUserOffers(@Path("id") Integer id,Callback<User> callback);

    //i.e. http://localhost/apiweshare/api/users/Users/1
    //Delete User record base on ID
    @DELETE("/api/users/{id}")
    public void deleteUserById(@Path("id") Integer id,Callback<User> callback);

    //i.e. http://localhost/apiweshare/api/users/Users/1
    //PUT User record and post content in HTTP request BODY
    @PUT("/api/users/{id}")
    public void updateUserById(@Path("id") Integer id,@Body User User,Callback<User> callback);

    //i.e. http://localhost/apiweshare/api/users/Users
    //Add User record and post content in HTTP request BODY
    @POST("/api/users")
    public void addUser(@Body User User,Callback<User> callback);
}
