package com.mla.israels.weshare;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by david on 15/08/2016.
 */
public class NewResponseActivity extends Activity {
    TextView responce_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_response);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        responce_id = (TextView) findViewById(R.id.request_id);
        responce_id.setText(getIntent().getStringExtra("ID"));
    }
}
