package com.mla.israels.weshare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.mla.israels.weshare.DataObjects.User;
import com.mla.israels.weshare.communication.RestService;

import java.util.Calendar;

public class RequestCreationActivity extends AppCompatActivity {

    Button publish;
    Spinner spnJobs;
    User currentUser;
    private String array_spinner[];
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText startDate;
    EditText endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_creation);

        Bundle b = this.getIntent().getExtras();
        if (b != null)
            currentUser = (User)b.getSerializable("current_user");

        publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RestService.getInstance().getRequestService().addRequest();
            }
        });

        array_spinner=new String[4];
        array_spinner[0]="Lecturer";
        array_spinner[1]="Teacher";
        array_spinner[2]="Kindergarden Teacher";
        array_spinner[3]="Assistant (Garden)";
        spnJobs = (Spinner)findViewById(R.id.spinnerJobs);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spnJobs.setAdapter(adapter);

        startDate = (EditText)findViewById(R.id.editStartDate);
        endDate = (EditText)findViewById(R.id.editEndDate);

        Button btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RequestCreationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                startDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                //Toast.makeText(getApplicationContext(), "jkjl", Toast.LENGTH_LONG);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        Button btnEnd = (Button)findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RequestCreationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                endDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                //Toast.makeText(getApplicationContext(), "jkjl", Toast.LENGTH_LONG);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
    }
}
