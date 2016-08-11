package com.mla.israels.weshare.communication;

/**
 * Created by Israel Sameach on 29/07/2016.
 */

public class RestService {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private static final String URL = "http://we-share.azurewebsites.net/";//"http://10.0.2.2:49332/";
    private retrofit.RestAdapter restAdapter;
    private UserService userService;
    private RequestService requestService;
    private OfferService offerService;

    private RestService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        userService = restAdapter.create(UserService.class);
        requestService = restAdapter.create(RequestService.class);
        offerService = restAdapter.create(OfferService.class);
    }

    private static RestService _instance = null;

    public static RestService getInstance()
    {
        if (_instance == null)
        {
            _instance = new RestService();
        }

        return _instance;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public RequestService getRequestService()
    {
        return requestService;
    }

    public OfferService getOfferService()
    {
        return offerService;
    }
}
