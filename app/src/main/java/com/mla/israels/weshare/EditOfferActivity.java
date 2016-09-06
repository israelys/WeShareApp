package com.mla.israels.weshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.UnsupportedEncodingException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by david on 15/08/2016.
 */
public class EditOfferActivity extends Activity implements View.OnClickListener {

    public static String s_result_code = "edited_offer";
    Button btnSendOffer;
    EditText etComment;
    int requestId;
    Request request;

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

        requestId = getIntent().getIntExtra("REQUEST_ID", -1);
        if (requestId == -1){
            request = (Request) getIntent().getSerializableExtra("REQUEST");
            byte[] data = Base64.decode(request.Offers[0].Comment, Base64.DEFAULT);
            try {
                etComment.setText(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                etComment.setText(request.Offers[0].Comment);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (requestId != -1) {
            Offer offer = new Offer();
            byte[] data = new byte[0];
            try {
                data = etComment.getText().toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            offer.Comment = Base64.encodeToString(data, Base64.DEFAULT);
            offer.RequestId = getIntent().getIntExtra("REQUEST_ID", -1);
            offer.UserId = getIntent().getIntExtra("USER_ID", -1);
            RestService.getInstance().getOfferService().addOffer(offer, new Callback<Offer>() {
                @Override
                public void success(Offer offer, Response response) {
                    Toast.makeText(getApplicationContext(), R.string.offer_added_successfuly, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), "failed... " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }
        else {
            byte[] data = new byte[0];
            try {
                data = etComment.getText().toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            request.Offers[0].Comment =  Base64.encodeToString(data, Base64.DEFAULT);
            RestService.getInstance().getOfferService().updateOfferById(request.Offers[0].Id, request.Offers[0], new Callback<Offer>() {
                @Override
                public void success(Offer offer, Response response) {
                    Toast.makeText(getApplicationContext(), R.string.offer_updated_successfuly, Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(s_result_code, request);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getApplicationContext(), "failed... " + error.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}