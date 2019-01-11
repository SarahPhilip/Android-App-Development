package com.sdsu.cs646.shameetha818455307.datepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, MyListFragment.OnDataPass {

    Spinner mSpinner;
    Button mButton;
    EditText mEditText;
    String textfield_value;
    String date_value;
    int mCurrentPosition;

    public static final String EXTRA_INDEX = "com.sdsu.cs646.shameetha818455307.datepickerdemo.index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.activities, R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mButton = (Button) findViewById(R.id.select_button);
        mButton.setOnClickListener(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }

    @Override
    protected void onNewIntent (Intent intent) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentPosition = getIntent().getIntExtra(EXTRA_INDEX, -1);
        }    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentPosition = getIntent().getIntExtra(EXTRA_INDEX, -1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        mEditText = (EditText) findViewById(R.id.editText);
        textfield_value = mEditText.getText().toString();
        switch (item.getItemId()) {
            case R.id.action_date:
                i = new Intent(MainActivity.this, DateActivity.class);
                i.putExtra(DateActivity.EXTRA_TEXTFIELD_VALUE_RECEIVED, textfield_value);
                startActivityForResult(i, 1);
                return true;
            case R.id.action_keyboard:
                i = new Intent(MainActivity.this, KeyboardActivity.class);
                i.putExtra(DateActivity.EXTRA_TEXTFIELD_VALUE_RECEIVED, textfield_value);
                startActivityForResult(i, 0);
                return true;
            case R.id.action_list:
                i = new Intent(MainActivity.this, ListActivity.class);
                i.putExtra(ListActivity.EXTRA_TEXTFIELD_VALUE_RECEIVED, textfield_value);
                i.putExtra(ListActivity.EXTRA_INDEX, mCurrentPosition);
                startActivityForResult(i, 0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        String[] activities = getResources().getStringArray(R.array.activities);
        mEditText = (EditText) findViewById(R.id.editText);
        textfield_value = mEditText.getText().toString();

        if (mSpinner.getSelectedItem().toString().equals(activities[0])) {
            Intent i = new Intent(MainActivity.this, DateActivity.class);
            i.putExtra(DateActivity.EXTRA_TEXTFIELD_VALUE_RECEIVED, textfield_value);
            startActivityForResult(i, 1);
        }

        if (mSpinner.getSelectedItem().toString().equals(activities[1])) {
            Intent i = new Intent(MainActivity.this, KeyboardActivity.class);
            i.putExtra(KeyboardActivity.EXTRA_TEXTFIELD_VALUE_RECEIVED, textfield_value);
            startActivityForResult(i, 0);
        }

        if (mSpinner.getSelectedItem().toString().equals(activities[2])) {
            Intent i = new Intent(MainActivity.this, ListActivity.class);
            i.putExtra(ListActivity.EXTRA_TEXTFIELD_VALUE_RECEIVED, textfield_value);
            i.putExtra(ListActivity.EXTRA_INDEX, mCurrentPosition);
            startActivityForResult(i, 0);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                date_value = data.getStringExtra(DateActivity.EXTRA_TEXTFIELD_VALUE_SENT);
                if (date_value != null)
                    mEditText.setText(date_value);
            }
        }
        mCurrentPosition = getIntent().getIntExtra(EXTRA_INDEX, -1);
    }

    @Override
    public void onDataPass(int data) {
        Log.i("LOG", "hello " + data);
        mCurrentPosition = data;
    }

    @Override
    public int getIndex() {
        return getIntent().getIntExtra(EXTRA_INDEX, -1);
    }
}