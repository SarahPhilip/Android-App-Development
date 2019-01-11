package com.sdsu.cs646.shameetha818455307.datepickerdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateActivity extends ActionBarActivity implements View.OnClickListener {

    DatePicker mDatePicker;
    Button mButton;
    private String mTextFieldValue;

    public static final String PREFS_NAME = "DatePickerDate";
    public static final String EXTRA_TEXTFIELD_VALUE_RECEIVED = "com.sdsu.cs646.shameetha818455307.datepickerdemo.textfield_value_received";
    public static final String EXTRA_TEXTFIELD_VALUE_SENT = "com.sdsu.cs646.shameetha818455307.datepickerdemo.textfield_value_sent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        mTextFieldValue = getIntent().getStringExtra(EXTRA_TEXTFIELD_VALUE_RECEIVED);
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String selected_date = settings.getString("selected_date",null);

        int day = -1;
        int month = -1;
        int year = -1;
        String expectedPattern = "MMM d, yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
            try {
                if(selected_date != null) {
                    Date date = formatter.parse(selected_date);
                    Log.d("DATE", date.toString());
                    day = Integer.parseInt((String) android.text.format.DateFormat.format("dd", date));
                    month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", date));
                    year = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", date));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        if(selected_date != null)
            mDatePicker.updateDate(year, month - 1, day);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aaa, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        DatePicker myDatePicker = (DatePicker) findViewById(R.id.datePicker);
        final String selectedDate = DateFormat.getDateInstance().format(myDatePicker.getCalendarView().getDate());
        final int day = mDatePicker.getDayOfMonth();
        final int month = mDatePicker.getMonth();
        final int year = mDatePicker.getYear();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Do you want to set the date?");
        alertDialog.setMessage("Are you sure you want set " + (month +1) + "/" + day + "/" +year + "?");
        alertDialog.setIcon(R.drawable.success);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("selected_date",selectedDate);
                        editor.commit();
                        sendDataBack(selectedDate);
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void sendDataBack(String textfield_value) {
        Intent data = new Intent();
        data.putExtra(EXTRA_TEXTFIELD_VALUE_SENT, textfield_value);
        setResult(RESULT_OK, data);
    }
}
