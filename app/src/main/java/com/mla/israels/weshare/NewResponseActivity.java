package com.mla.israels.weshare;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mla.israels.weshare.DataObjects.Offer;
import com.mla.israels.weshare.DataObjects.Request;
import com.mla.israels.weshare.communication.OfferService;
import com.mla.israels.weshare.communication.RestService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by david on 15/08/2016.
 */
public class NewResponseActivity extends Activity implements View.OnClickListener {
    Button btnSendOffer;
    EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_response);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .8));
        btnSendOffer = (Button) findViewById(R.id.btn_sned_offer);
        btnSendOffer.setOnClickListener(this);
        etComment = (EditText) findViewById(R.id.offer_comment);
    }

    @Override
    public void onClick(View v) {
        Offer offer = new Offer();
        offer.Comment = etComment.getText().toString();
        offer.RequestId = getIntent().getIntExtra("REQUEST_ID", -1);
        offer.UserId = getIntent().getIntExtra("USER_ID", -1);
        RestService.getInstance().getOfferService().addOffer(offer, new Callback<Offer>() {
            @Override
            public void success(Offer offer, Response response) {
                Toast.makeText(getApplicationContext(), "Success!...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "failed... " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}