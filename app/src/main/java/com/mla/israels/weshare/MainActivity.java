package com.mla.israels.weshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.mla.israels.weshare.DataObjects.Offer;
import com.mla.israels.weshare.DataObjects.Request;
import com.mla.israels.weshare.DataObjects.User;
import com.mla.israels.weshare.Utils.RecyclerAllRequestsAdapter;
import com.mla.israels.weshare.Utils.RecyclerUserOffersAdapter;
import com.mla.israels.weshare.Utils.RecyclerUserRequeatsAdapter;
import com.mla.israels.weshare.communication.RestService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progressBar;
    public User currentUser;
    private Request[] AllRequests;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    RelativeLayout back_dim_layout;
    MainActivity mThis;
    ProgressDialog progress;
    ImageView profile_pic;
    TextView user_name,user_email;
    NavigationView navigation_view;
    FloatingActionButton add_request_btn;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    RecyclerAllRequestsAdapter recyclerAllRequestsAdapter;
    ArrayList<Request> arrayListAllRequests = new ArrayList<Request>();

    RecyclerUserRequeatsAdapter recyclerUserRequeatsAdapter;
    ArrayList<Request> arrayListUserRequests = new ArrayList<Request>();

    RecyclerUserOffersAdapter recyclerUserOffersAdapter;
    ArrayList<Request> arrayListUserOffersRequests = new ArrayList<Request>();

    int viewSelection = R.id.nav_all_requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThis = this;
        setContentView(R.layout.activity_main);
        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress= new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        getUserData();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerAllRequestsAdapter = new RecyclerAllRequestsAdapter(this, arrayListAllRequests);
        recyclerUserRequeatsAdapter = new RecyclerUserRequeatsAdapter(this, arrayListUserRequests) ;
        recyclerUserOffersAdapter = new RecyclerUserOffersAdapter(this, arrayListUserOffersRequests);
        recyclerView.setAdapter(recyclerAllRequestsAdapter);
        //new ItemTouchHelper(new SwipeHelper(recyclerAllRequestsAdapter)).attachToRecyclerView(recyclerView);
        //GetAllRequests();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setNavigationHeader();
        navigation_view.setNavigationItemSelectedListener(this);

        add_request_btn = (FloatingActionButton)findViewById(R.id.add_request_btn);
        add_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RequestCreationActivity.class);
                i.putExtra("current_user", currentUser);
                startActivityForResult(i, s_requestRequestCode);
            }
        });

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (viewSelection == R.id.nav_all_requests) {
                    ShowProgressBar();
                    GetAllRequests();
                } else if (viewSelection == R.id.nav_my_requests) {
                    ShowProgressBar();
                    GetUserRequests();
                } else if (viewSelection == R.id.nav_my_offers) {
                    ShowProgressBar();
                    GetUserOffers();
                }
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void GetAllRequests(){
        recyclerView.setAdapter(recyclerAllRequestsAdapter);
        recyclerAllRequestsAdapter.collapseAllRequests();
        RestService.getInstance().getRequestService().getRequest(new Callback<List<Request>>() {
            @Override
            public void success(List<Request> requests, Response response) {
                progressBar.setVisibility(View.GONE);
                arrayListAllRequests.clear();

                Request[] sortRequests = requests.toArray(new Request[requests.size()]);
                AllRequests = sortRequests;
                Arrays.sort(sortRequests);

                arrayListAllRequests.addAll(Arrays.asList(sortRequests));

                recyclerAllRequestsAdapter.refresh();
                swipeContainer.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Success to get requests from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Unable to get requests from server. " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }
    private void GetUserRequests(){
        recyclerView.setAdapter(recyclerUserRequeatsAdapter);
        recyclerUserRequeatsAdapter.collapseAllRequests();
        RestService.getInstance().getUserService().getUserRequests(currentUser.Id, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                progressBar.setVisibility(View.GONE);
                arrayListUserRequests.clear();

                for (Request r : user.Requests) {
                    arrayListUserRequests.add(r);
                }

                recyclerUserRequeatsAdapter.refresh();
                swipeContainer.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Success to get requests from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Unable to get requests from server. " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }

    public void GetUserOffers() {
        recyclerView.setAdapter(recyclerUserOffersAdapter);
        recyclerUserOffersAdapter.collapseAllOffers();
        RestService.getInstance().getUserService().getUserOffers(currentUser.Id, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                progressBar.setVisibility(View.GONE);
                arrayListUserOffersRequests.clear();

                for (Request request : user.Requests) {
                    arrayListUserOffersRequests.add(request);
                }

                recyclerUserOffersAdapter.refresh();
                swipeContainer.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Success to get requests from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Unable to get requests from server. " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }

    private final int s_requestRequestCode = 77;
    private final int s_offerRequestCode = 78;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (s_requestRequestCode) : {
                if (resultCode == Activity.RESULT_OK) {
                    Request newReq = (Request)data.getSerializableExtra(RequestCreationActivity.s_result_code);
                    arrayListAllRequests.add(0, newReq);
                    recyclerAllRequestsAdapter.requestAdded();
                    arrayListUserRequests.add(newReq);
                    if (viewSelection == R.id.nav_all_requests){
                        recyclerAllRequestsAdapter.refresh();
                    }else if (viewSelection == R.id.nav_my_requests){
                        recyclerUserRequeatsAdapter.refresh();
                    }
                }
                break;
            }
            case (s_offerRequestCode) : {
                if (resultCode == Activity.RESULT_OK) {
                    Request editedRequest = (Request)data.getSerializableExtra(EditOfferActivity.s_result_code);
                    for (int index = 0; index < arrayListUserOffersRequests.size(); ++ index){
                        Request request = arrayListUserOffersRequests.get(index);
                        if (request.Id == editedRequest.Id){
                            arrayListUserOffersRequests.set(index, editedRequest);
                            recyclerUserOffersAdapter.refresh();
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    public void StartOffertActivity(View view){
        //back_dim_layout.setVisibility(View.VISIBLE);
        Intent i = new Intent(this, EditOfferActivity.class);
        i.putExtra("REQUEST_ID", Integer.valueOf(view.getTag().toString()));
        i.putExtra("USER_ID", currentUser.Id);
        startActivity(i);
    }

    public void UpdateOffer(View view){
        //back_dim_layout.setVisibility(View.VISIBLE);
        Intent i = new Intent(this, EditOfferActivity.class);
        i.putExtra("REQUEST", (Request) view.getTag());
        startActivityForResult(i, s_offerRequestCode);
    }
    /*Once User's can authenticated,
      It make an HTTP GET request to LinkedIn's REST API using the currently authenticated user's credentials.
      If successful, A LinkedIn ApiResponse object containing all of the relevant aspects of the server's response will be returned.
     */

    public void getUserData(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(MainActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    saveUserProfile(result.getResponseDataAsJson());
                    setUserProfile(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }
    /*
       Set Navigation header by using Layout Inflater.
     */

    public void setNavigationHeader(){

        navigation_view = (NavigationView) findViewById(R.id.nav_view);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_profile, null);
        navigation_view.addHeaderView(header);

        user_name = (TextView) header.findViewById(R.id.username);
        profile_pic = (ImageView) header.findViewById(R.id.profile_pic);
        user_email = (TextView) header.findViewById(R.id.email);
    }

    public void saveUserProfile(JSONObject response){

        try {
            User user = new User();
            user.Id = -1;
            user.Email = response.get("emailAddress").toString();
            user.Name = response.get("formattedName").toString();
            user.LinkdinUserProfileUrl = response.get("publicProfileUrl").toString();
            user.PictureUrl = response.get("pictureUrl").toString();

            RestService.getInstance().getUserService().addUser(user, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    currentUser = user;
                    GetAllRequests();
                    Toast.makeText(getApplicationContext(), "User saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    currentUser = null;
                    Toast.makeText(getApplicationContext(), "Unable to save user. " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
       Set User Profile Information in Navigation Bar.
     */
    public void setUserProfile(JSONObject response){
        try {
            user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());
            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_pic);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    private void ShowProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            LISessionManager.getInstance(getApplicationContext()).clearSession();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_requests) {
            ShowProgressBar();
            GetAllRequests();
            viewSelection = R.id.nav_all_requests;
            setTitle(R.string.title_activity_all_requests);
        } else if (id == R.id.nav_my_requests) {
            ShowProgressBar();
            GetUserRequests();
            viewSelection = R.id.nav_my_requests;
            setTitle(R.string.title_activity_user_requests);
        } else if (id == R.id.nav_my_offers) {
            ShowProgressBar();
            GetUserOffers();
            viewSelection = R.id.nav_my_offers;
            setTitle(R.string.title_activity_offers);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "'WeSahre' is an awesome application");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_linkedin_prof) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUser.LinkdinUserProfileUrl));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void DeleteRequest(View view) {
        final Request requestToDelete = (Request)view.getTag(R.id.request);
        final int pos = (int) view.getTag(R.id.position);
        RestService.getInstance().getRequestService().deleteRequestById(requestToDelete.Id, new Callback<Request>() {
            @Override
            public void success(Request request, Response response) {
                Toast.makeText(getApplicationContext(), R.string.request_deleted_successfuly, Toast.LENGTH_SHORT).show();
                if (viewSelection == R.id.nav_all_requests){
                    arrayListAllRequests.remove(requestToDelete);
                    recyclerAllRequestsAdapter.closeFlexible(pos);
                    recyclerAllRequestsAdapter.refresh();
                }else if (viewSelection == R.id.nav_my_requests){
                    arrayListUserRequests.remove(requestToDelete);
                    recyclerUserRequeatsAdapter.closeFlexible(pos);
                    recyclerUserRequeatsAdapter.refresh();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "failed... " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DeleteOffer(View view) {
        final Request requestToDelete = (Request)view.getTag(R.id.request);
        final int pos = (int) view.getTag(R.id.position);
        RestService.getInstance().getOfferService().deleteOfferById(requestToDelete.Offers[0].Id, new Callback<Offer>() {
            @Override
            public void success(Offer offer, Response response) {
                Toast.makeText(getApplicationContext(), "Success!...", Toast.LENGTH_SHORT).show();
                arrayListUserOffersRequests.remove(requestToDelete);
                recyclerUserOffersAdapter.closeFlexible(pos);
                recyclerUserOffersAdapter.refresh();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "failed... " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
