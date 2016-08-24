package com.mla.israels.weshare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import com.mla.israels.weshare.DataObjects.Request;
import com.mla.israels.weshare.DataObjects.User;
import com.mla.israels.weshare.communication.RestService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RequestCreationActivity extends AppCompatActivity {

    public static String s_result_code = "new_req";
    ProgressDialog progress;
    Button publish;
    Spinner spnJobs;
    User currentUser;
    private String array_spinner[];
    EditText startDateTxt;
    EditText endDateTxt;
    Calendar startDate;
    Calendar endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_creation);

        Bundle b = this.getIntent().getExtras();
        if (b != null)
            currentUser = (User)b.getSerializable("current_user");

        progress= new ProgressDialog(this);
        progress.setMessage("Publishing...");
        progress.setCanceledOnTouchOutside(false);

        publish = (Button) findViewById(R.id.publish);
        publish.setEnabled(false);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                Calendar now = Calendar.getInstance();
                Request req = new Request();
                req.Title = ((EditText)findViewById(R.id.etxtTitle)).getText().toString();
                req.Details = ((EditText)findViewById(R.id.etxtDetails)).getText().toString();
                req.CreationDate = String.format("%1$04d-%2$02d-%3$02dT%4$02d:%5$02d:00",
                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),
                        now.get(Calendar.HOUR), now.get(Calendar.MINUTE));
                req.StartDate = String.format("%1$04d-%2$02d-%3$02dT%4$02d:%5$02d:00",
                        startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH),
                        startDate.get(Calendar.HOUR), startDate.get(Calendar.MINUTE));
                req.EndDate = String.format("%1$04d-%2$02d-%3$02dT%4$02d:%5$02d:00",
                        endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH),
                        endDate.get(Calendar.HOUR), endDate.get(Calendar.MINUTE));
                req.JobId = spnJobs.getSelectedItemPosition() + 1;
                req.Location = ((EditText)findViewById(R.id.etxtLocation)).getText().toString();
                req.UserId = currentUser.Id;
                RestService.getInstance().getRequestService().addRequest(req, new Callback<Request>() {
                    @Override
                    public void success(Request request, Response response) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Request published", Toast.LENGTH_SHORT).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(s_result_code, request);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to publish your request. " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
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

        startDateTxt = (EditText)findViewById(R.id.editStartDate);
        endDateTxt = (EditText)findViewById(R.id.editEndDate);

        Button btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RequestCreationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                startDateTxt.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                startDate =  new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                if (endDate != null)
                                    publish.setEnabled(true);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        final Button btnEnd = (Button)findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Process to get Current Date
                final Calendar c = Calendar.getInstance();

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RequestCreationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                endDateTxt.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                                endDate =  new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                if (startDate != null)
                                    publish.setEnabled(true);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }
}
