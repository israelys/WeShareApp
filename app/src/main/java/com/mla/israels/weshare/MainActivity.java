package com.mla.israels.weshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.TextView;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.mla.israels.weshare.DataObjects.Request;
import com.mla.israels.weshare.DataObjects.User;
import com.mla.israels.weshare.Utils.RecyclerJobsAdapter;
import com.mla.israels.weshare.Utils.SwipeHelper;
import com.mla.israels.weshare.communication.RestService;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected User currentUser;

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    MainActivity mThis;
    ProgressDialog progress;
    ImageView profile_pic;
    TextView user_name,user_email;
    NavigationView navigation_view;
    FloatingActionButton add_request_btn;

    RecyclerView recyclerView;
    RecyclerJobsAdapter recyclerJobsAdapter;
    ArrayList<Request> arrayList = new ArrayList<Request>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThis = this;
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress= new ProgressDialog(this);
        progress.setMessage("Retrieve data...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        getUserData();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerJobsAdapter = new RecyclerJobsAdapter(arrayList);
        recyclerView.setAdapter(recyclerJobsAdapter);
        new ItemTouchHelper(new SwipeHelper(recyclerJobsAdapter)).attachToRecyclerView(recyclerView);
        getRequests();

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
                startActivityForResult(i, s_resultCode);
            }
        });
    }

    private final int s_resultCode = 77;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (s_resultCode) : {
                if (resultCode == Activity.RESULT_OK) {
                    Request newReq = (Request)data.getSerializableExtra(RequestCreationActivity.s_result_code);
                    arrayList.add(0,newReq);
                    recyclerJobsAdapter.refresh();
                }
                break;
            }
        }
    }

    public void StartRequestActivity(View view){
        Intent i = new Intent(this, NewResponseActivity.class);
        i.putExtra("ID", view.getTag().toString());
        startActivity(i);
    }

    private void getRequests() {
        RestService.getInstance().getRequestService().getRequest(new Callback<List<Request>>() {
            @Override
            public void success(List<Request> requests, Response response) {
                arrayList.clear();

                Request[] sortRequests = requests.toArray(new Request[requests.size()]);

                Arrays.sort(sortRequests);

                arrayList.addAll(Arrays.asList(sortRequests));

                recyclerJobsAdapter.refresh();
                Toast.makeText(getApplicationContext(), "Success to get requests from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Unable to get requests from server. " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRequestsByID() {
        RestService.getInstance().getRequestService().getRequest(new Callback<List<Request>>() {
            @Override
            public void success(List<Request> requests, Response response) {
                arrayList.clear();

                for (Request r : requests) {
                    {
                        if (r.UserId == currentUser.Id)
                            arrayList.add(r);
                    }
                }

                recyclerJobsAdapter.refresh();
                Toast.makeText(getApplicationContext(), "Success to get requests from server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Unable to get requests from server. " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public  void  saveUserProfile(JSONObject response){

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
    public  void  setUserProfile(JSONObject response){

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
            getRequests();
        } else if (id == R.id.nav_my_requests) {
            getRequestsByID();
        } else if (id == R.id.nav_my_offers) {

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
}
